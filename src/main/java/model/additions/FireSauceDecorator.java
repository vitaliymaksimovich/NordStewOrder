package model.additions;

import model.Dish;
import model.DishDecorator;

public class FireSauceDecorator extends DishDecorator {

    public FireSauceDecorator(Dish dish) {
        super(dish);
    }

    @Override
    public String getDescription() {
        return dish.getDescription() + " + Огненный соус";
    }

    @Override
    public int getCost() {
        return dish.getCost() + 10;
    }
}
