package org.example.models;

import java.util.HashMap;

public class Drink extends MenuItem{
    private boolean isAlcoholic;
    private int volume;

    public Drink() {
        super();
        isAlcoholic = false;
        volume = 0;
    }

    public Drink(HashMap<String,Ingredient> ingredients, String name, String description, int price, int calories, boolean isAvailable, boolean isAlcoholic, int volume) {
        super(ingredients, name, description, price, calories,isAvailable);
        this.isAlcoholic = isAlcoholic;
        this.volume = volume;
    }

    public Drink(MenuItem menuItem, boolean isAlcoholic, int volume) {
        super(menuItem.getIngredients(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.getCalories(), menuItem.isAvailable());
        this.isAlcoholic = isAlcoholic;
        this.volume = volume;
    }

    public boolean isAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        isAlcoholic = alcoholic;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "isAvailable=" + isAvailable +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isAlcoholic=" + isAlcoholic +
                ", volume=" + volume +
                ", ingredients=" + ingredients +
                ", calories=" + calories +
                ", price=" + price +
                '}';
    }
}
