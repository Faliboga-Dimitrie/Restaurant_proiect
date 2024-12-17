package org.example.models;

public class Ingredient {
    private String name;
    private int quantity;
    private String unit;
    private boolean isVegetarian;
    private boolean isAllergen;


    public Ingredient() {
        quantity = 0;
        isVegetarian = false;
        isAllergen = false;
        name = "";
        unit = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public boolean isAllergen() {
        return isAllergen;
    }

    public void setAllergen(boolean allergen) {
        isAllergen = allergen;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", isVegetarian=" + isVegetarian +
                ", isAllergen=" + isAllergen +
                '}';
    }
}
