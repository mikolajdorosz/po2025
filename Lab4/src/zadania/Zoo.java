package zadania;
import java.util.Random;
import animals.*;

public class Zoo {
    private Animal[] animals = new Animal[100];
    private Random random = new Random();

    public Zoo() {
        fillAnimalsArray();
        animals[0].makeSound();
    }

    private void fillAnimalsArray() {
        for (int i = 0; i < animals.length; i++) {
            int choice = random.nextInt(3);
            if (choice == 0) {
                animals[i] = new Parrot("Parrot" + i);
            } else if (choice == 1) {
                animals[i] = new Dog("Dog" + i);
            } else {
                animals[i] = new Snake("Snake" + i);
            }
        }
    }

    public int legsSum() {
        int sum = 0;
        for (Animal animal : animals) {
            sum += animal.getLegs();
        }
        return sum;
    }
}