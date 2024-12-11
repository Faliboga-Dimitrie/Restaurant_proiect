package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;
import org.example.enums.MenuUpdateType;
import org.example.enums.MenuItemType;
import java.util.Scanner;

public class Menu {
    private ArrayList<Drink> drinks;
    private ArrayList<Food> foods;
    private ArrayList<Special> specials;
    private final HashMap<String, Integer> drinksByName = new HashMap<>();
    private final HashMap<String, Integer> foodsByName = new HashMap<>();
    private final HashMap<String, Integer> specialsByName = new HashMap<>();
    private final HashMap<Double, Integer> drinksByPrice = new HashMap<>();
    private final HashMap<Double, Integer> foodsByPrice = new HashMap<>();
    private final HashMap<Double, Integer> specialsByPrice = new HashMap<>();
    private final HashMap<Integer, Integer> drinksByCalories = new HashMap<>();
    private final HashMap<Integer, Integer> foodsByCalories = new HashMap<>();
    private final HashMap<Integer, Integer> specialsByCalories = new HashMap<>();
    private boolean toAdd = false;
    private boolean toIngredient = false;

    public Menu() {}

    public Menu(ArrayList<Drink> drinks, ArrayList<Food> foods, ArrayList<Special> specials) {
        this.drinks = drinks;
        this.foods = foods;
        this.specials = specials;

        for (int i = 0; i < drinks.size(); i++) {
            drinksByName.put(drinks.get(i).getName(),i);
            drinksByPrice.put(drinks.get(i).getPrice(),i);
            drinksByCalories.put(drinks.get(i).getCalories(),i);
        }
        for (int i = 0; i < foods.size(); i++) {
            foodsByName.put(foods.get(i).getName(),i);
            foodsByPrice.put(foods.get(i).getPrice(),i);
            foodsByCalories.put(foods.get(i).getCalories(),i);
        }
        for (int i = 0; i < specials.size(); i++) {
            specialsByName.put(specials.get(i).getName(), i);
            specialsByPrice.put(specials.get(i).getPrice(), i);
            specialsByCalories.put(specials.get(i).getCalories(), i);
        }
    }

    public void addDrink(Drink drink) {
        drinks.add(drink);
        drinksByName.put(drink.getName(),drinks.size()-1);
        drinksByPrice.put(drink.getPrice(),drinks.size()-1);
        drinksByCalories.put(drink.getCalories(),drinks.size()-1);
    }

    public void addFood(Food food) {
        foods.add(food);
        foodsByName.put(food.getName(),foods.size()-1);
        foodsByPrice.put(food.getPrice(),foods.size()-1);
        foodsByCalories.put(food.getCalories(),foods.size()-1);
    }

    public void addSpecial(Special special) {
        specials.add(special);
        specialsByName.put(special.getName(),specials.size()-1);
        specialsByPrice.put(special.getPrice(),specials.size()-1);
        specialsByCalories.put(special.getCalories(),specials.size()-1);
    }

    public void removeDrink(Drink drink) {
        drinks.remove(drink);
        drinksByName.remove(drink.getName());
        drinksByPrice.remove(drink.getPrice());
        drinksByCalories.remove(drink.getCalories());
    }

    public void removeFood(Food food) {
        foods.remove(food);
        foodsByName.remove(food.getName());
        foodsByPrice.remove(food.getPrice());
        foodsByCalories.remove(food.getCalories());
    }

    public void removeSpecial(Special special) {
        specials.remove(special);
        specialsByName.remove(special.getName());
        specialsByPrice.remove(special.getPrice());
        specialsByCalories.remove(special.getCalories());
    }

    public Drink findDrinkByName(String name) {
        Integer index = drinksByName.get(name);
        return index != null ? drinks.get(index) : null;
    }

    public Food findFoodByName(String name) {
        Integer index = foodsByName.get(name);
        return index != null ? foods.get(index) : null;
    }

    public Special findSpecialByName(String name) {
        Integer index = specialsByName.get(name);
        return index != null ? specials.get(index) : null;
    }

    public Drink findDrinkByPrice(double price) {
        Integer index = drinksByPrice.get(price);
        return index != null ? drinks.get(index) : null;
    }

    public Food findFoodByPrice(double price) {
        Integer index = foodsByPrice.get(price);
        return index != null ? foods.get(index) : null;
    }

    public Special findSpecialByPrice(double price) {
        Integer index = specialsByPrice.get(price);
        return index != null ? specials.get(index) : null;
    }

    public Drink findDrinkByCalories(int calories) {
        Integer index = drinksByCalories.get(calories);
        return index != null ? drinks.get(index) : null;
    }

    public Food findFoodByCalories(int calories) {
        Integer index = foodsByCalories.get(calories);
        return index != null ? foods.get(index) : null;
    }

    public Special findSpecialByCalories(int calories) {
        Integer index = specialsByCalories.get(calories);
        return index != null ? specials.get(index) : null;
    }

    private void interogateUser() {
        try (Scanner scanner = new Scanner(System.in)) {
            toAdd = askUser(scanner, "Do you want to update " +
                    "the menu items? Enter 'true' or 'false':");
            if (!toAdd) {
                toIngredient = askUser(scanner, "Do you want to update" +
                        " the item? Enter 'true' or 'false':");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean askUser(Scanner scanner, String question) {
        System.out.println(question);
        while (true) {
            try {
                return scanner.nextBoolean();
            } catch (Exception e) {
                System.out.println("Please enter 'true' or 'false'.");
                scanner.nextLine();
            }
        }
    }

    private MenuItem findMenuItemByNameAndType(String name, MenuItemType type) {
        switch (type) {
            case DRINK -> {
                return findDrinkByName(name);
            }
            case FOOD -> {
                return findFoodByName(name);
            }
            case SPECIAL -> {
                return findSpecialByName(name);
            }
            default -> throw new UnsupportedOperationException("Unsupported menu item type: " + type);

        }
    }

    public <T> void updateMenuItem(String itemName, MenuUpdateType updateType, MenuItemType type, T newValue) {
        MenuItem item = findMenuItemByNameAndType(itemName, type);

        if (item == null) {
            System.out.println(type + " not found.");
            return;
        }

        try {
            updateMenuItem(item, updateType, newValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private <T extends MenuItem> void updateMenuItem(T item, MenuUpdateType updateType, Object newValue) {
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        switch (updateType) {
            case NAME:
                if (newValue instanceof String) {
                    item.setName((String) newValue);
                } else {
                    throw new IllegalArgumentException("Invalid value type for NAME. Expected String.");
                }
                break;
            case PRICE:
                if (newValue instanceof Double) {
                    item.setPrice((Double) newValue);
                } else {
                    throw new IllegalArgumentException("Invalid value type for PRICE. Expected Double.");
                }
                break;
            case INGREDIENTS:
                if (newValue instanceof Ingredient ingredient) {
                    interogateUser();
                    item.modifyIngredients(ingredient, toAdd, toIngredient, item.findIngredientByName(ingredient.getName()));
                } else {
                    throw new IllegalArgumentException("Invalid value type for INGREDIENTS. Expected Ingredient.");
                }
                break;
            case DESCRIPTION:
                if (newValue instanceof String) {
                    item.setDescription((String) newValue);
                } else {
                    throw new IllegalArgumentException("Invalid value type for DESCRIPTION. Expected String.");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported update type: " + updateType);
        }
    }
}

