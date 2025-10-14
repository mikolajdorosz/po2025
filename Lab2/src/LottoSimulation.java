import java.util.ArrayList;
import java.util.Random;

public class LottoSimulation {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<Integer> numbers = new ArrayList<>();
        int guessedNumbers = 0;
        int counter = 0;

        long startTime = System.currentTimeMillis();
        while (guessedNumbers < 6) {
            guessedNumbers = 0;
            numbers.clear();

            // Generating 6 unique numbers
            while (numbers.size() < 6) {
                int number = random.nextInt(49) + 1; // gives 1–49
                while (numbers.contains(number)) {
                    number = random.nextInt(49) + 1;
                }
                numbers.add(number);
            }

            // Comparing generated and user's numbers
            for (int num : numbers) {
                for (int j = 0; j < 6; j++) {
                    if (num == Integer.parseInt(args[j]) ) {
                        guessedNumbers += 1;
                        break;
                    }
                }
            }
            counter++;
        }
        System.out.println("Czas dzialania: " + (System.currentTimeMillis() - startTime));
        System.out.print("Liczba losowan: " + counter);
    }
}
