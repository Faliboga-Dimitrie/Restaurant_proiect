package org.example.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void findIngredientByName() {
        MenuItem menuItem = new MenuItem();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");
        menuItem.getIngredients().put("Salt", ingredient);
        assertEquals(ingredient, menuItem.findIngredientByName("Salt"));
    }
}