public class SubsetSum {

    public static boolean hasSubsetSum(int[] arr, int target) {
        if (target == 0) return true;
        if (arr == null || arr.length == 0) return false;
        
        return checkSubset(arr, 0, target);
    }

    private static boolean checkSubset(int[] arr, int index, int target) {
        if (target == 0) return true;
        
        if (index >= arr.length || target < 0) return false;

        boolean exclude = checkSubset(arr, index + 1, target);

        boolean include = false;
        if (arr[index] <= target) {
            include = checkSubset(arr, index + 1, target - arr[index]);
        }

        return include || exclude;
    }
}
