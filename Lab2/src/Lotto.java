import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Lotto {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<Integer> numbers = new ArrayList<>();
        int guessedNumbers = 0;

        // Generating 6 unique numbers
        while (numbers.size() < 6) {
            int number = random.nextInt(49) + 1; // gives 1–49
            while (numbers.contains(number)) {
                number = random.nextInt(49) + 1;
            }
            numbers.add(number);
        }

        // User's numbers
        System.out.println("Liczby wygrywajace: " + numbers);
        System.out.println("Twoje liczby:       " + Arrays.toString(args));

        // Comparing generated and user's numbers
        for (int num : numbers) {
            for (int j = 0; j < 6; j++) {
                if (num == Integer.parseInt(args[j]) ) {
                    guessedNumbers += 1;
                    break;
                }
            }
        }
        System.out.print("Ilosc liczb trafionych: " +  guessedNumbers);
    }
}
