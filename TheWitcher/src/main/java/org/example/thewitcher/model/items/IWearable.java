package org.example.thewitcher.model.items;

public interface IWearable {
    String getName();
    String getType();
    void setType(String type);
    int getPrice();
    void setPrice(int price);
    double getWeight();
    void setWeight(double weight);
    int getCondition();
    void setCondition(int condition);
    int getBonus();
    void setBonus(int bonus);
    String inspect();
}
