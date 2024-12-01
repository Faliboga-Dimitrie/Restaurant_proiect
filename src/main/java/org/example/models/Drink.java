package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Drink extends MenuItem{
    private int quantity;
    private boolean isAlcoholic;
    private int volume;

    public Drink() {
        super();
        quantity = 0;
        isAlcoholic = false;
        volume = 0;
    }

    public Drink(HashMap<String,Ingredient> ingredients, String name, String description, int price, int calories, boolean isAvailable, int quantity, boolean isAlcoholic, int volume) {
        super(ingredients, name, description, price, calories,isAvailable);
        this.quantity = quantity;
        this.isAlcoholic = isAlcoholic;
        this.volume = volume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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