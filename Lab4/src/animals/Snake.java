package animals;

public class Snake extends Animal {
    public Snake(String name) {
        super(name, 0);
    }

    @Override
    public String getDescription() {
        return name + " has no legs.";
    }
    @Override
    public void makeSound() {
        System.out.println("Sssss!");
    }
}
