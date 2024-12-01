package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodItem extends MenuItem{
    private String cuisineType;
    private boolean isAppetizer;
    private boolean isMainCourse;
    private boolean isDessert;
    private boolean isSideDish;

    public FoodItem() {
        super();
        cuisineType = "";
        isAppetizer = false;
        isMainCourse = false;
        isDessert = false;
        isSideDish = false;
    }

    public FoodItem(HashMap<String,Ingredient> ingredients, String name, String description, int price, int calories, boolean isAvailable, String cuisineType, boolean isAppetizer, boolean isMainCourse, boolean isDessert, boolean isSideDish) {
        super(ingredients, name, description, price, calories, isAvailable);
        this.cuisineType = cuisineType;
        this.isAppetizer = isAppetizer;
        this.isMainCourse = isMainCourse;
        this.isDessert = isDessert;
        this.isSideDish = isSideDish;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public boolean isAppetizer() {
        return isAppetizer;
    }

    public void setAppetizer(boolean appetizer) {
        isAppetizer = appetizer;
    }

    public boolean isMainCourse() {
        return isMainCourse;
    }

    public void setMainCourse(boolean mainCourse) {
        isMainCourse = mainCourse;
    }

    public boolean isDessert() {
        return isDessert;
    }

    public void setDessert(boolean dessert) {
        isDessert = dessert;
    }

    public boolean isSideDish() {
        return isSideDish;
    }

    public void setSideDish(boolean sideDish) {
        isSideDish = sideDish;
    }

    @Override
    public String toString() {
        String type = "";
        if(isAppetizer)
            type = "Appetizer";
        else if (isMainCourse) {
            type = "Main Course";
        }
        else if (isDessert) {
            type = "Dessert";
        }
        else if (isSideDish) {
            type = "Side Dish";
        }
        return "FoodItem{" +
                "isAvailable=" + isAvailable +
                "Type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", price=" + price +
                ", calories=" + calories +
                '}';
    }
}
