package zadania;

import animals.*;
import java.util.Random;

public class Zoo {
    private Animal[] animals = new Animal[100];
    private Random random = new Random();

    public void fillAnimalsArray() {
        for (int i = 0; i < animals.length; i++) {
            int choice = random.nextInt(3);
            switch (choice) {
                case 0 -> animals[i] = new Dog("Dog" + i);
                case 1 -> animals[i] = new Parrot("Parrot" + i);
                case 2 -> animals[i] = new Snake("Snake" + i);
            }
        }
    }
    public void printAnimals() {
        for (Animal animal : animals) {
            System.out.println(animal.getDescription());
            animal.makeSound();
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