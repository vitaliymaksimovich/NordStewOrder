package model;

public class NordStew implements Dish {

    @Override
    public String getDescription() {
        return "Нордское рагу";
    }

    @Override
    public int getCost() {
        return 50;
    }
}