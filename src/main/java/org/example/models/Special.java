package org.example.models;

import java.util.HashMap;

public class Special extends MenuItem{
        private boolean chefSpecial;
        private boolean foodSpecial;
        private boolean eventSpecial;

    public Special() {
        super();
        chefSpecial = false;
        foodSpecial = false;
        eventSpecial = false;
    }

    public Special(HashMap<String,Ingredient> ingredients, String name, String description, int price, int calories, boolean isAvailable, boolean chefSpecial, boolean foodSpecial, boolean eventSpecial) {
        super(ingredients, name, description, price, calories, isAvailable);
        this.chefSpecial = chefSpecial;
        this.foodSpecial = foodSpecial;
        this.eventSpecial = eventSpecial;
    }

    public Special(MenuItem menuItem, boolean chefSpecial, boolean foodSpecial, boolean eventSpecial) {
        super(menuItem.getIngredients(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.getCalories(), menuItem.isAvailable());
        this.chefSpecial = chefSpecial;
        this.foodSpecial = foodSpecial;
        this.eventSpecial = eventSpecial;
    }

    public boolean isChefSpecial() {
        return chefSpecial;
    }

    public void setChefSpecial(boolean chefSpecial) {
        this.chefSpecial = chefSpecial;
    }

    public boolean isFoodSpecial() {
        return foodSpecial;
    }

    public void setFoodSpecial(boolean foodSpecial) {
        this.foodSpecial = foodSpecial;
    }

    public boolean isEventSpecial() {
        return eventSpecial;
    }

    public void setEventSpecial(boolean eventSpecial) {
        this.eventSpecial = eventSpecial;
    }

    @Override
    public String toString() {
        String type = "";
        if(chefSpecial)
            type = "Chef Special";
        else if(foodSpecial)
            type = "Food Special";
        else if(eventSpecial)
            type = "Event Special";

        return "Special{" +
                "isAvailable=" + isAvailable +
                "Type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", price=" + price +
                ", calories=" + calories +
                '}';
    }
}
