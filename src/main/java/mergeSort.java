public class mergeSort {
    public static void sort(int[] arr,int[] brr, int [] crr){
        int i = 0, j = 0, k = 0;
        while(i<arr.length && j<brr.length){
            if(arr[i]<=brr[j]) crr[k++] = arr[i++];
            else crr[k++] = brr[j++];
        }
        while(i<arr.length) arr[i++] = crr[k++];
        while(j<brr.length) brr[j++] = crr[k++];
    }
    public static void merge(int[]arr){
        int n = arr.length;
        int[] a = new int[n/2];
        int[] b = new int[n-n/2];
        for (int i = 0; i < n/2; i++) {
            a[i] = arr[i];
        }
        for (int i = 0; i < n-n/2; i++) {
            b[i] = arr[i+n/2];
        }
        merge(a);
        merge(b);
        sort(a, b, arr);
    }
}
