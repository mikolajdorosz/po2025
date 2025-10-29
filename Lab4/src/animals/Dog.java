package animals;

public class Dog extends Animal {
    public Dog(String name) {
        super(name, 4);
    }

    @Override
    public String getDescription() {
        return name + " has " + legs + " legs.";
    }
    @Override
    public void makeSound() {
        System.out.println("Hau hau!");
    }
}
