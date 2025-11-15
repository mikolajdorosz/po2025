import symulator.*;

public class Main {
    public static void main(String[] args) {
        Samochod samochod1 = new Samochod("RPR10101", "Ford", 180,
                new Pozycja(0, 0),
                new Silnik(3200, "Cummins 6.7L TurboDiesel", 444.97, 15000, "Cummins", "6.7L ISB"),
                new SkrzyniaBiegow(6, "TorqShift 6R140", 220, 8000, "Ford", "6R140",
                        new Sprzeglo("Sprzeglo", 35, 2000, "Ford", "HDClutch6.7L")));
    }
}
