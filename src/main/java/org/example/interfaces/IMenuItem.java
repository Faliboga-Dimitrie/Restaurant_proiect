package org.example.interfaces;

import org.example.models.Ingredient;

import java.util.HashMap;

public interface IMenuItem {
    String getName();
    double getPrice();
    HashMap<String, Ingredient> getIngredients();
    Ingredient findIngredientByName(String ingredientName);
    void modifyIngredients(Ingredient ingredient, boolean toAdd, boolean toIngredient, Ingredient myIngredient);
}
