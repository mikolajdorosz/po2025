import symulator.*;

public class Main {
    public static void main(String[] args) {

        Pozycja docelowaPozycja = new Pozycja(100, 10);

        Silnik silnik = new Silnik(10000);
        SkrzyniaBiegow skrzynia = new SkrzyniaBiegow();
        Samochod samochod = new Samochod();
        samochod.wlacz();



        samochod.wylacz();
    }
}
