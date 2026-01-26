package simulator;

public abstract class Component {
    private String name;
    private double weight;
    private double price;

    public Component(String name, double weight, double price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() { return name; }
    public double getWeight() { return weight; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() { return name; }
}
