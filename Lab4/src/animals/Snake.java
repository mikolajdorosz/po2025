package animals;

public class Snake extends Animal {

    public Snake(String name) {
        super(name, 0);
    }

    public String getDescription() {
        return name + "has no legs";
    }
    public void makeSound() {
        System.out.println("Sssss!");
    }
}
