public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private boolean isVerified;

    public User(String userId, String name, String email, String phone, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isVerified = false;
    }

    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            System.out.println("[GoPro] " + name + " logged in successfully.");
            return true;
        }
        System.out.println("[GoPro] Login failed. Invalid credentials.");
        return false;
    }

    public void verifyOTP(String otp) {
        if (otp != null && otp.length() == 6) {
            this.isVerified = true;
            System.out.println("[GoPro] " + name + " verified via OTP.");
        } else {
            System.out.println("[GoPro] Invalid OTP.");
        }
    }

    public void updateProfile(String name, String phone) {
        this.name = name;
        this.phone = phone;
        System.out.println("[GoPro] Profile updated for " + this.name);
    }

    public void logout() {
        System.out.println("[GoPro] " + name + " logged out.");
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public boolean isVerified() { return isVerified; }

    @Override
    public String toString() {
        return "User{name='" + name + "', email='" + email + "', phone='" + phone + "', verified=" + isVerified + "}";
    }
}
