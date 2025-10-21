import java.util.Arrays;

public class CodingBat {

    public static void main(String[] args) {
        CodingBat cb = new CodingBat();

        System.out.println("endUp(\"hello\") → " + cb.endUp("hello"));
        System.out.println("array667({6, 6, 2, 6, 7, 7}) → " + cb.array667(new int[]{6, 6, 2, 6, 7, 7}));
        System.out.println("evenOdd({1, 2, 3, 4, 5, 6}) → " + Arrays.toString(cb.evenOdd(new int[]{1, 2, 3, 4, 5, 6})));
        System.out.println("withoutX2(\"xHi\") → " + cb.withoutX2("xHi"));
    }

    // Warmup-1
    public String endUp(String str) {
        if (str.length() <= 3) {
            return str.toUpperCase();
        } else {
            return str.substring(0, str.length()-3) + str.substring(str.length()-3).toUpperCase();
        }
    }

    // Warmup-2
    public int array667(int[] nums) {
        int counter = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1] && nums[i] == 6 || nums[i + 1] == 7) {
                counter++;
            }
        }
        return counter;
    }

    // Array-2
    public int[] evenOdd(int[] nums) {
        int temp, i = 0, j = nums.length - 1;
        while (i < j) {
            if (nums[i] % 2 == 1 && nums[j] % 2 == 0) {
                temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
            }
            if (nums[i] % 2 == 0) {
                i++;
            }
            if (nums[j] % 2 == 1) {
                j--;
            }
        }
        return nums;
    }

    // String-1
    public String withoutX2(String str) {
        if (str.equals("")) {
            return str;
        }
        if (str.length() > 1 && str.charAt(1) == 'x') {
            str = str.charAt(0) + str.substring(2);
        }
        if (str.charAt(0) == 'x') {
            str = str.substring(1);
        }
        if (str.equals("")) {
            return str;
        }
        return str;
    }

}
