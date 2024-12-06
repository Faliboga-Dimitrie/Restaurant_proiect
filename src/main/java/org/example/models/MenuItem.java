package org.example.models;

import java.util.HashMap;
import org.example.enums.IngredientUpdateType;
import java.util.Scanner;

public class MenuItem extends Ingredient{
    protected HashMap<String,Ingredient> ingredients;
    protected String name;
    protected String description;
    protected double price;
    protected int calories;
    protected boolean isAvailable;
    private IngredientUpdateType updateType;

    public MenuItem() {
        name = "";
        description = "";
        price = 0;
        calories = 0;
        ingredients = new HashMap<>();
        isAvailable = false;
    }

    public MenuItem(HashMap<String,Ingredient> ingredients, String name, String description, double price, int calories, boolean isAvailable) {
        this.ingredients = ingredients;
        this.name = name;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.isAvailable = isAvailable;
    }

    public MenuItem(Ingredient ingredient, String name, String description, double price, int calories, boolean isAvailable) {
        this.ingredients = new HashMap<>();
        this.ingredients.put(ingredient.getName(),ingredient);
        this.name = name;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getCalories() {
        return calories;
    }

    public HashMap<String,Ingredient> getIngredients() {
        return ingredients;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setIngredients(HashMap<String,Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Ingredient findIngredientByName(String ingredientName) {
        return ingredients.get(ingredientName);
    }

    private void interogateUser(){
        String input;
        try (Scanner scanner = new Scanner(System.in)){
            updateType.print();
            updateType = askUser(scanner, "type one of the above, in caps");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private IngredientUpdateType askUser(Scanner scanner, String question) {
        System.out.println(question);
        while (true){
            try{
                return IngredientUpdateType.valueOf(scanner.next());
            }
            catch (IllegalArgumentException e){
                System.out.println("Please enter a valid option from below");
                updateType.print();
            }
        }
    }

    public void modifyIngredients(Ingredient ingredient, boolean toAdd, boolean toIngredient, Ingredient myIngredient) {
        if(!toIngredient) {
            if(!toAdd) {
                ingredients.put(ingredient.getName(),ingredient);
            }
            else {
                ingredients.remove(myIngredient);
            }
        }
        else {
            interogateUser();
            switch(updateType){
                case NAME -> myIngredient.setName(ingredient.getName());
                case UNIT -> myIngredient.setUnit(ingredient.getUnit());
                case QUANTITY -> myIngredient.setQuantity(ingredient.getQuantity());
                case IS_ALLERGEN -> myIngredient.setAllergen(ingredient.isAllergen());
                case IS_VEGETARIAN -> myIngredient.setVegetarian(ingredient.isVegetarian());
                case ALL -> myIngredient = ingredient;
            }
        }
    }
}
