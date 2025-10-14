import java.util.ArrayList;
import java.util.Random;

public class Lotto {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<Integer> numbers = new ArrayList<>();

        while (numbers.size() < 6) {
            int number = random.nextInt(49) + 1; // gives 1–49
            while (numbers.contains(number)) {
                number = random.nextInt(49) + 1;
            }
            numbers.add(number);
        }
        System.out.print(numbers);
    }
}
