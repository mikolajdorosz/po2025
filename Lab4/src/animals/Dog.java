package animals;

public class Dog extends Animal {

    public Dog(String name) {
        super(name, 4);
    }

    public String getDescription() {
        return name + "has" + legs + "legs";
    }
    public void makeSound() {
        System.out.println("Hau hau!");
    }
}
