package org.example.models;

public class Food extends MenuItem{
    private String cuisineType;
    private boolean isMainCourse;
    private boolean isDessert;

    public Food() {
        super();
        cuisineType = "";
        isMainCourse = false;
        isDessert = false;
    }

    public Food(MenuItem menuItem, String cuisineType, boolean isMainCourse, boolean isDessert) {
        super(menuItem.getIngredients(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.getCalories(), menuItem.isAvailable());
        this.cuisineType = cuisineType;
        this.isMainCourse = isMainCourse;
        this.isDessert = isDessert;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
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

    @Override
    public String toString() {
        String type = "";
         if (isMainCourse) {
            type = "Main Course";
        }
        else if (isDessert) {
            type = "Dessert";
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
