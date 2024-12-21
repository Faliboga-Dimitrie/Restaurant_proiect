package org.example.models;

import java.util.HashMap;
import org.example.enums.IngredientUpdateType;
import java.util.Scanner;

public class MenuItem   {
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

    public void modifyIngredients(Ingredient ingredient, boolean toAdd, boolean toIngredient, Ingredient myIngredient) {
        if (toIngredient) {
            modifyIngredientProperties(ingredient, myIngredient);
        } else {
            modifyIngredientList(ingredient, toAdd, myIngredient);
        }

        updateIngredientInJson(myIngredient);
    }

    private void modifyIngredientList(Ingredient ingredient, boolean toAdd, Ingredient myIngredient) {
        if (toAdd) {
            ingredients.remove(myIngredient.getName());
        } else {
            ingredients.put(ingredient.getName(), ingredient);
        }
    }

    private void modifyIngredientProperties(Ingredient ingredient, Ingredient myIngredient) {
        interogateUser();

        switch (updateType) {
            case NAME -> myIngredient.setName(ingredient.getName());
            case UNIT -> myIngredient.setUnit(ingredient.getUnit());
            case QUANTITY -> myIngredient.setQuantity(ingredient.getQuantity());
            case IS_ALLERGEN -> myIngredient.setAllergen(ingredient.isAllergen());
            case IS_VEGETARIAN -> myIngredient.setVegetarian(ingredient.isVegetarian());
            case ALL -> myIngredient = ingredient;
        }
    }

    private void interogateUser() {
        try (Scanner scanner = new Scanner(System.in)) {
            updateType.print();
            updateType = askUser(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private IngredientUpdateType askUser(Scanner scanner) {
        System.out.println("Type one of the above, in caps:");
        while (true) {
            try {
                return IngredientUpdateType.valueOf(scanner.next());
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid option from the list above.");
                updateType.print();
            }
        }
    }

    private void updateIngredientInJson(Ingredient myIngredient) {
        JsonUtil.updateElementInJson("ingredients.json", Ingredient.class,
                ingredient -> ingredient.getName().equals(myIngredient.getName()),
                ingredient -> {
                    ingredient.setName(myIngredient.getName());
                    ingredient.setUnit(myIngredient.getUnit());
                    ingredient.setQuantity(myIngredient.getQuantity());
                    ingredient.setAllergen(myIngredient.isAllergen());
                    ingredient.setVegetarian(myIngredient.isVegetarian());
                });

    }
}
