import zadania.Zoo;

public class Main {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        zoo.fillAnimalsArray();
        zoo.printAnimals();
        System.out.println("Suma nóg zwierząt z zoo: " + zoo.legsSum());
    }
}
