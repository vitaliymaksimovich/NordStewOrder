package model.additions;

import model.Dish;
import model.DishDecorator;

public class FlatbreadDecorator extends DishDecorator {

    public FlatbreadDecorator(Dish dish) {
        super(dish);
    }

    @Override
    public String getDescription() {
        return dish.getDescription() + " + Нордская лепёшка";
    }

    @Override
    public int getCost() {
        return dish.getCost() + 7;
    }
}