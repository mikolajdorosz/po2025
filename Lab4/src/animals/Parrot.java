package animals;

public class Parrot extends Animal {

    public Parrot(String name) {
        super(name, 2);
    }

    public String getDescription() {
        return name + "has" + legs + "legs";
    }
    public void makeSound() {
        System.out.println("Kra kra!");
    }
}
