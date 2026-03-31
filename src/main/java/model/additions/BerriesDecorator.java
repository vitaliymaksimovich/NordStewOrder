package model.additions;

import model.Dish;
import model.DishDecorator;

public class BerriesDecorator extends DishDecorator {

    public BerriesDecorator(Dish dish) {
        super(dish);
    }

    @Override
    public String getDescription() {
        return dish.getDescription() + " + Снежные ягоды";
    }

    @Override
    public int getCost() {
        return dish.getCost() + 8;
    }
}