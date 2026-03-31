package model.additions;

import model.Dish;
import model.DishDecorator;

public class DoubleMeatDecorator extends DishDecorator {

    public DoubleMeatDecorator(Dish dish) {
        super(dish);
    }

    @Override
    public String getDescription() {
        return dish.getDescription() + " + Двойная порция оленины";
    }

    @Override
    public int getCost() {
        return dish.getCost() + 20;
    }
}
