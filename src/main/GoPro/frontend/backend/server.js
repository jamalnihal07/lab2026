require('dotenv').config();

const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const fs = require('fs');
const path = require('path');
const { v4: uuidv4 } = require('uuid');
const Razorpay = require('razorpay');

const app = express();
const PORT = process.env.PORT || 3000;
const DATA_FILE = path.join(__dirname, 'data.json');
const STATIC_ROOT = path.join(__dirname, '..'); // serve frontend static files
const RAZORPAY_KEY_ID = process.env.RAZORPAY_KEY_ID || '';
const RAZORPAY_KEY_SECRET = process.env.RAZORPAY_KEY_SECRET || '';
const RAZORPAY_CURRENCY = process.env.RAZORPAY_CURRENCY || 'INR';
const HAS_RAZORPAY_CONFIG = Boolean(RAZORPAY_KEY_ID && RAZORPAY_KEY_SECRET);
const razorpay = HAS_RAZORPAY_CONFIG
  ? new Razorpay({ key_id: RAZORPAY_KEY_ID, key_secret: RAZORPAY_KEY_SECRET })
  : null;

const DEFAULT_DATA = () => ({
  users: [],
  drivers: [],
  rides: [],
  admins: [],
  payments: []
});

function ensureDataShape(data) {
  const root = { ...DEFAULT_DATA(), ...(data || {}) };
  root.users = Array.isArray(root.users) ? root.users : [];
  root.drivers = Array.isArray(root.drivers) ? root.drivers : [];
  root.rides = Array.isArray(root.rides) ? root.rides : [];
  root.admins = Array.isArray(root.admins) ? root.admins : [];
  root.payments = Array.isArray(root.payments) ? root.payments : [];
  return root;
}

function createVerificationCode() {
  return Math.floor(100000 + Math.random() * 900000).toString();
}

function sanitizeAccount(account) {
  if (!account) return account;
  const { password, verificationCode, verificationExpiresAt, ...safe } = account;
  return safe;
}

function createPaymentRecord({ data, userId, rideId, amount, method, provider, purpose, gateway, reference }) {
  const numericAmount = Number(amount);
  const account = [...data.users, ...data.drivers].find(item => item.id === userId);

  if (!account) {
    return { error: 'Account not found', status: 404 };
  }

  const payment = {
    id: `PAY-${uuidv4().slice(0, 8).toUpperCase()}`,
    userId,
    rideId: rideId || null,
    amount: numericAmount,
    method,
    provider: provider || 'demo',
    purpose: purpose || 'ride-payment',
    gateway: gateway || 'demo-gateway',
    status: 'succeeded',
    createdAt: new Date().toISOString(),
    reference: reference || `GW-${uuidv4().slice(0, 10).toUpperCase()}`
  };

  if (purpose === 'wallet-topup') {
    account.wallet = Number(account.wallet || 0) + numericAmount;
  } else if (method === 'wallet') {
    const balance = Number(account.wallet || 0);
    if (balance < numericAmount) {
      return { error: 'Insufficient wallet balance', status: 400 };
    }
    account.wallet = balance - numericAmount;
  }

  if (rideId) {
    const ride = data.rides.find(item => item.id === rideId);
    if (ride) {
      ride.paymentStatus = 'paid';
      ride.paymentMethod = method;
      ride.paymentReference = payment.reference;
      ride.paidAmount = numericAmount;
      ride.paymentGateway = payment.gateway;
    }
  }

  data.payments = data.payments || [];
  data.payments.unshift(payment);
  writeData(data);

  return { payment, walletBalance: account.wallet ?? null };
}

function verifyRazorpaySignature(orderId, paymentId, signature) {
  if (!HAS_RAZORPAY_CONFIG) {
    return false;
  }

  const expected = crypto
    .createHmac('sha256', RAZORPAY_KEY_SECRET)
    .update(`${orderId}|${paymentId}`)
    .digest('hex');

  const expectedBuffer = Buffer.from(expected, 'utf8');
  const signatureBuffer = Buffer.from(signature || '', 'utf8');

  return expectedBuffer.length === signatureBuffer.length && crypto.timingSafeEqual(expectedBuffer, signatureBuffer);
}

app.use(cors());
app.use(bodyParser.json());
app.use(express.static(STATIC_ROOT));

function readData() {
  try {
    const raw = fs.readFileSync(DATA_FILE, 'utf8');
    return ensureDataShape(JSON.parse(raw));
  } catch (err) {
    console.error('Failed to read data file', err);
    return DEFAULT_DATA();
  }
}

function writeData(data) {
  fs.writeFileSync(DATA_FILE, JSON.stringify(ensureDataShape(data), null, 2), 'utf8');
}

app.get('/api/health', (req, res) => {
  res.json({ status: 'ok' });
});

app.post('/api/register', (req, res) => {
  const { name, email, phone, password, type } = req.body;
  if (!name || !email || !phone || !password || !type) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  const accountType = type === 'driver' ? 'driver' : 'rider';
  const data = readData();
  const emailExists = [...data.users, ...data.drivers, ...data.admins].some(item => item.email === email);
  if (emailExists) {
    return res.status(409).json({ error: 'Email already registered' });
  }

  const verificationCode = createVerificationCode();
  const account = {
    id: `${accountType === 'driver' ? 'D' : 'U'}${Math.floor(Math.random() * 100000).toString().padStart(5, '0')}`,
    name,
    email,
    phone,
    password,
    type: accountType,
    totalRides: 0,
    avgRating: 0,
    wallet: 0,
    totalSpent: 0,
    joinDate: new Date().toISOString().slice(0, 7),
    verified: false,
    verificationCode,
    verificationExpiresAt: new Date(Date.now() + 10 * 60 * 1000).toISOString()
  };

  if (accountType === 'driver') {
    account.vehicle = {
      type: 'Sedan',
      registration: '',
      color: '',
      model: ''
    };
    account.earnings = { today: 0, week: 0, month: 0, total: 0 };
    account.status = 'pending-verification';
    data.drivers.push(account);
  } else {
    data.users.push(account);
  }

  writeData(data);

  return res.status(201).json({
    message: 'Registration created. Verification required.',
    user: sanitizeAccount(account),
    verificationCode
  });
});

app.post('/api/verify-registration', (req, res) => {
  const { email, code, type } = req.body;
  if (!email || !code || !type) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  const data = readData();
  const collection = type === 'driver' ? data.drivers : data.users;
  const account = collection.find(item => item.email === email);
  if (!account) {
    return res.status(404).json({ error: 'Account not found' });
  }

  if (account.verified) {
    return res.json({ message: 'Account already verified', user: sanitizeAccount(account) });
  }

  if (account.verificationCode !== code) {
    return res.status(400).json({ error: 'Invalid verification code' });
  }

  if (account.verificationExpiresAt && new Date(account.verificationExpiresAt).getTime() < Date.now()) {
    return res.status(400).json({ error: 'Verification code expired' });
  }

  account.verified = true;
  delete account.verificationCode;
  delete account.verificationExpiresAt;
  if (type === 'driver' && !account.status) {
    account.status = 'online';
  }

  writeData(data);

  return res.json({ message: 'Account verified successfully', user: sanitizeAccount(account) });
});

app.post('/api/resend-verification', (req, res) => {
  const { email, type } = req.body;
  if (!email || !type) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  const data = readData();
  const collection = type === 'driver' ? data.drivers : data.users;
  const account = collection.find(item => item.email === email);
  if (!account) {
    return res.status(404).json({ error: 'Account not found' });
  }

  const verificationCode = createVerificationCode();
  account.verificationCode = verificationCode;
  account.verificationExpiresAt = new Date(Date.now() + 10 * 60 * 1000).toISOString();
  writeData(data);

  return res.json({
    message: 'Verification code regenerated',
    verificationCode
  });
});

app.post('/api/login', (req, res) => {
  const { email, password, type } = req.body;
  const data = readData();

  if (type === 'admin') {
    const admin = data.admins.find(a => a.email === email && a.password === password);
    if (!admin) return res.status(401).json({ error: 'Invalid credentials' });
    return res.json({ ...sanitizeAccount(admin), type: 'admin' });
  }

  if (type === 'driver') {
    const driver = data.drivers.find(d => d.email === email && d.password === password);
    if (!driver) return res.status(401).json({ error: 'Invalid credentials' });
    if (driver.verified === false) {
      return res.status(403).json({ error: 'Account verification required', verificationRequired: true });
    }
    return res.json({ ...sanitizeAccount(driver), type: 'driver' });
  }

  const user = data.users.find(u => u.email === email && u.password === password);
  if (!user) return res.status(401).json({ error: 'Invalid credentials' });
  if (user.verified === false) {
    return res.status(403).json({ error: 'Account verification required', verificationRequired: true });
  }
  return res.json({ ...sanitizeAccount(user), type: 'rider' });
});

app.post('/api/payments/checkout', (req, res) => {
  const { userId, rideId, amount, method, provider, purpose } = req.body;
  const numericAmount = Number(amount);

  if (!userId || !method || !Number.isFinite(numericAmount) || numericAmount <= 0) {
    return res.status(400).json({ error: 'Missing or invalid payment details' });
  }

  const data = readData();
  const account = [...data.users, ...data.drivers].find(item => item.id === userId);
  if (!account) {
    return res.status(404).json({ error: 'Account not found' });
  }

  const result = createPaymentRecord({
    data,
    userId,
    rideId,
    amount: numericAmount,
    method,
    provider,
    purpose,
    gateway: 'demo-gateway'
  });

  if (result.error) {
    return res.status(result.status).json({ error: result.error });
  }

  return res.json({
    message: 'Payment processed successfully',
    payment: result.payment,
    walletBalance: result.walletBalance
  });
});

app.post('/api/payments/razorpay/order', async (req, res) => {
  const { userId, rideId, amount, method, purpose } = req.body;
  const numericAmount = Number(amount);

  if (!userId || !method || !Number.isFinite(numericAmount) || numericAmount <= 0) {
    return res.status(400).json({ error: 'Missing or invalid payment details' });
  }

  if (!HAS_RAZORPAY_CONFIG) {
    return res.status(503).json({ error: 'Razorpay is not configured on this server' });
  }

  try {
    const order = await razorpay.orders.create({
      amount: Math.round(numericAmount * 100),
      currency: RAZORPAY_CURRENCY,
      receipt: `rcpt_${uuidv4().slice(0, 12)}`,
      payment_capture: 1,
      notes: {
        userId,
        rideId: rideId || '',
        method,
        purpose: purpose || 'ride-payment'
      }
    });

    return res.json({
      message: 'Razorpay order created',
      keyId: RAZORPAY_KEY_ID,
      order,
      currency: RAZORPAY_CURRENCY,
      amount: numericAmount
    });
  } catch (error) {
    return res.status(500).json({ error: 'Failed to create Razorpay order', details: error.message });
  }
});

app.post('/api/payments/razorpay/verify', (req, res) => {
  const {
    userId,
    rideId,
    amount,
    method,
    purpose,
    orderId,
    paymentId,
    signature,
    provider
  } = req.body;

  const numericAmount = Number(amount);
  if (!userId || !method || !orderId || !paymentId || !signature || !Number.isFinite(numericAmount) || numericAmount <= 0) {
    return res.status(400).json({ error: 'Missing or invalid Razorpay payment details' });
  }

  if (!HAS_RAZORPAY_CONFIG) {
    return res.status(503).json({ error: 'Razorpay is not configured on this server' });
  }

  if (!verifyRazorpaySignature(orderId, paymentId, signature)) {
    return res.status(400).json({ error: 'Invalid Razorpay payment signature' });
  }

  const data = readData();
  const result = createPaymentRecord({
    data,
    userId,
    rideId,
    amount: numericAmount,
    method,
    provider: provider || 'razorpay',
    purpose,
    gateway: 'razorpay',
    reference: paymentId
  });

  if (result.error) {
    return res.status(result.status).json({ error: result.error });
  }

  return res.json({
    message: 'Razorpay payment verified successfully',
    payment: result.payment,
    walletBalance: result.walletBalance
  });
});

app.get('/api/driver/:id', (req, res) => {
  const data = readData();
  const driver = data.drivers.find(d => d.id === req.params.id);
  if (!driver) return res.status(404).json({ error: 'Driver not found' });
  res.json(driver);
});

app.get('/api/rides', (req, res) => {
  const data = readData();
  res.json(data.rides || []);
});

app.get('/api/rides/active', (req, res) => {
  const data = readData();
  const active = (data.rides || []).filter(r => r.status === 'active' || r.status === 'in-progress');
  res.json(active);
});

app.post('/api/rides', (req, res) => {
  const data = readData();
  const { userId, driverId, pickup, dropoff, distance, fare, passengerName } = req.body;
  const ride = {
    id: 'R' + Math.floor(Math.random() * 100000).toString().padStart(3, '0'),
    userId: userId || null,
    driverId: driverId || null,
    pickup: pickup || 'Unknown pickup',
    dropoff: dropoff || 'Unknown dropoff',
    distance: distance || 0,
    fare: fare || 0,
    status: 'active',
    date: new Date().toISOString(),
    passengerName: passengerName || 'Guest'
  };
  data.rides = data.rides || [];
  data.rides.unshift(ride);
  writeData(data);
  res.json(ride);
});

app.post('/api/rides/:id/start', (req, res) => {
  const data = readData();
  const ride = data.rides.find(r => r.id === req.params.id);
  if (!ride) return res.status(404).json({ error: 'Ride not found' });
  ride.status = 'in-progress';
  writeData(data);
  res.json(ride);
});

app.post('/api/rides/:id/complete', (req, res) => {
  const data = readData();
  const ride = data.rides.find(r => r.id === req.params.id);
  if (!ride) return res.status(404).json({ error: 'Ride not found' });
  ride.status = 'completed';
  ride.completedAt = new Date().toISOString();

  // update driver earnings
  if (ride.driverId) {
    const driver = data.drivers.find(d => d.id === ride.driverId);
    if (driver) {
      driver.earnings = driver.earnings || { today: 0, week: 0, month: 0, total: 0 };
      driver.earnings.today = (driver.earnings.today || 0) + (ride.fare || 0);
      driver.earnings.week = (driver.earnings.week || 0) + (ride.fare || 0);
      driver.earnings.month = (driver.earnings.month || 0) + (ride.fare || 0);
      driver.earnings.total = (driver.earnings.total || 0) + (ride.fare || 0);
      driver.totalRides = (driver.totalRides || 0) + 1;
    }
  }

  writeData(data);
  res.json(ride);
});

app.post('/api/driver/:id/status', (req, res) => {
  const data = readData();
  const driver = data.drivers.find(d => d.id === req.params.id);
  if (!driver) return res.status(404).json({ error: 'Driver not found' });
  const { status } = req.body;
  driver.status = status;
  writeData(data);
  res.json({ id: driver.id, status: driver.status });
});

// simple endpoint to reset demo data (useful for local testing)
app.post('/api/reset', (req, res) => {
  try {
    const seed = fs.readFileSync(path.join(__dirname, 'data.json.example'), 'utf8');
    fs.writeFileSync(DATA_FILE, seed, 'utf8');
    res.json({ ok: true });
  } catch (err) {
    res.status(500).json({ error: 'reset failed', details: err.message });
  }
});

app.listen(PORT, () => {
  console.log(`GoPro backend running on http://localhost:${PORT}`);
});
