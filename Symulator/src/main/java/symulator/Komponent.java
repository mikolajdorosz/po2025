package symulator;

public abstract class Komponent {
    private String nazwa;
    private int waga;
    private int cena;
    private String producent;
    private String model;

    public String getNazwa() { return nazwa; }
    public int getWaga() { return waga; }
    public int getCena() { return cena; }
    public String getProducent() { return producent; }
    public String getModel() { return model; }
}
