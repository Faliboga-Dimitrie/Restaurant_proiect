package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;
import org.example.enums.MenuUpdateType;
import org.example.enums.MenuItemType;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Consumer;
import org.example.interfaces.IMenu;

public class Menu implements IMenu {
    private final ArrayList<Drink> drinks;
    private final ArrayList<Food> foods;
    private final HashMap<String, Integer> drinksByName = new HashMap<>();
    private final HashMap<String, Integer> foodsByName = new HashMap<>();
    private final HashMap<Double, Integer> drinksByPrice = new HashMap<>();
    private final HashMap<Double, Integer> foodsByPrice = new HashMap<>();
    private final HashMap<Integer, Integer> drinksByCalories = new HashMap<>();
    private final HashMap<Integer, Integer> foodsByCalories = new HashMap<>();
    private boolean toAdd = false;
    private boolean toIngredient = false;

    public Menu() {
        drinks = new ArrayList<>();
        foods = new ArrayList<>();
    }

    public void addDrink(Drink drink, boolean fromJson) {
        drinks.add(drink);
        drinksByName.put(drink.getName(),drinks.size()-1);
        drinksByPrice.put(drink.getPrice(),drinks.size()-1);
        drinksByCalories.put(drink.getCalories(),drinks.size()-1);
        if (!fromJson) {
            JsonUtil.appendToJson(drink, "drinks.json", Drink.class);
        }
    }

    public void addFood(Food food, boolean fromJson) {
        foods.add(food);
        foodsByName.put(food.getName(),foods.size()-1);
        foodsByPrice.put(food.getPrice(),foods.size()-1);
        foodsByCalories.put(food.getCalories(),foods.size()-1);
        if (!fromJson) {
            JsonUtil.appendToJson(food, "foods.json", Food.class);
        }
    }

    public void removeDrink(Drink drink) {
        drinks.remove(drink);
        drinksByName.remove(drink.getName());
        drinksByPrice.remove(drink.getPrice());
        drinksByCalories.remove(drink.getCalories());
        JsonUtil.removeFromJson("drinks.json", Drink.class, item -> item.getName().equals(drink.getName()));
    }

    public void removeFood(Food food) {
        foods.remove(food);
        foodsByName.remove(food.getName());
        foodsByPrice.remove(food.getPrice());
        foodsByCalories.remove(food.getCalories());
        JsonUtil.removeFromJson("foods.json", Food.class, item -> item.getName().equals(food.getName()));
    }

    public Drink findDrinkByName(String name) {
        Integer index = drinksByName.get(name);
        return index != null ? drinks.get(index) : null;
    }

    public Food findFoodByName(String name) {
        Integer index = foodsByName.get(name);
        return index != null ? foods.get(index) : null;
    }

    public Drink findDrinkByPrice(double price) {
        Integer index = drinksByPrice.get(price);
        return index != null ? drinks.get(index) : null;
    }

    public Food findFoodByPrice(double price) {
        Integer index = foodsByPrice.get(price);
        return index != null ? foods.get(index) : null;
    }

    public Drink findDrinkByCalories(int calories) {
        Integer index = drinksByCalories.get(calories);
        return index != null ? drinks.get(index) : null;
    }

    public Food findFoodByCalories(int calories) {
        Integer index = foodsByCalories.get(calories);
        return index != null ? foods.get(index) : null;
    }

    public MenuItem findMenuItemByName(String name) {
        MenuItem item = findDrinkByName(name);
        if (item == null) {
            item = findFoodByName(name);
        }
        return item;
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
            default -> throw new UnsupportedOperationException("Unsupported menu item type: " + type);

        }
    }

    public <T> void updateMenuItem(String itemName, MenuUpdateType updateType, MenuItemType menuItemType, T newValue) {
        MenuItem item = findMenuItemByNameAndType(itemName, menuItemType);

        if (item == null) {
            System.out.println(menuItemType + " not found.");
            return;
        }

        try {
            updateMenuItem(item, updateType, newValue);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    private <T extends MenuItem> void updateMenuItem(T item, MenuUpdateType updateType, Object newValue) {
        if (item == null) {
            throw new UnsupportedOperationException("Item not found.");
        }
        updateCommonMenuItemFields(item, updateType, newValue, item instanceof Drink);
        handleSpecificMenuItemUpdate(item, updateType, newValue);
    }

    private <T extends MenuItem> void updateCommonMenuItemFields(T item, MenuUpdateType updateType, Object newValue, boolean isDrink) {
        switch (updateType) {
            case NAME:
                ensureType(newValue, String.class);
                item.setName((String) newValue);
                break;
            case PRICE:
                ensureType(newValue, Double.class);
                item.setPrice((Double) newValue);
                break;
            case INGREDIENTS:
                ensureType(newValue, Ingredient.class);
                Ingredient ingredient = (Ingredient) newValue;
                interogateUser();
                item.modifyIngredients(ingredient, toAdd, toIngredient, item.findIngredientByName(ingredient.getName()));
                break;
            case DESCRIPTION:
                ensureType(newValue, String.class);
                item.setDescription((String) newValue);
                break;
            case CALORIES:
                ensureType(newValue, Integer.class);
                item.setCalories((Integer) newValue);
                break;
            case AVAILABILITY:
                ensureType(newValue, Boolean.class);
                item.setAvailable((Boolean) newValue);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported menu update type: " + updateType);
        }
        if(updateType == MenuUpdateType.INGREDIENTS)
            return;

        updateMenuItemInJson(isDrink ? "drinks.json" : "foods.json", item.getClass(), item.getName(), newValue, updateType);
    }

    private void handleSpecificMenuItemUpdate(MenuItem item, MenuUpdateType updateType, Object newValue) {
        switch (item) {
            case Drink drink -> handleDrinkUpdate(drink, updateType, newValue);
            case Food food -> handleFoodUpdate(food, updateType, newValue);
            default -> throw new UnsupportedOperationException("Unsupported menu item type: " + item.getClass().getSimpleName());
        }
    }

    private void handleDrinkUpdate(Drink drink, MenuUpdateType updateType, Object newValue) {
        switch (updateType) {
            case IS_ALCOHOLIC:
                ensureType(newValue, Boolean.class);
                drink.setAlcoholic((Boolean) newValue);
                break;
            case VOLUME:
                ensureType(newValue, Integer.class);
                drink.setVolume((Integer) newValue);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported drink update type: " + updateType);
        }
        updateDrinkItemInJson("drinks.json", Drink.class, drink.getName(), newValue, updateType);
    }

    private void handleFoodUpdate(Food food, MenuUpdateType updateType, Object newValue) {
        switch (updateType) {
            case CUISINE_TYPE:
                ensureType(newValue, String.class);
                food.setCuisineType((String) newValue);
                break;
            case IS_MAIN_COURSE:
                ensureType(newValue, Boolean.class);
                food.setMainCourse((Boolean) newValue);
                break;
            case IS_DESSERT:
                ensureType(newValue, Boolean.class);
                food.setDessert((Boolean) newValue);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported food update type: " + updateType);
        }
        updateFoodItemInJson("foods.json", Food.class, food.getName(), newValue, updateType);
    }

    private static void ensureType(Object value, Class<?> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException("Invalid value type. Expected " + expectedType.getSimpleName() + ".");
        }
    }

    private static <T> void updateMenuItemInJson(String fileName, Class<T> clazz, String itemName, Object newValue, MenuUpdateType updateType) {
        Predicate<T> condition = item -> {
            if (item instanceof MenuItem menuItem) {
                return menuItem.getName().equals(itemName);
            }
            return false;
        };

        Consumer<T> updater = item -> {
            if (item instanceof MenuItem menuItem) {
                switch (updateType) {
                    case NAME -> {
                        ensureType(newValue, String.class);
                        menuItem.setName((String) newValue);
                    }
                    case PRICE -> {
                        ensureType(newValue, Double.class);
                        menuItem.setPrice((Double) newValue);
                    }
                    case DESCRIPTION -> {
                        ensureType(newValue, String.class);
                        menuItem.setDescription((String) newValue);
                    }
                    case CALORIES -> {
                        ensureType(newValue, Integer.class);
                        menuItem.setCalories((Integer) newValue);
                    }
                    case AVAILABILITY -> {
                        ensureType(newValue, Boolean.class);
                        menuItem.setAvailable((Boolean) newValue);
                    }
                    default -> throw new UnsupportedOperationException("Unsupported menu update type: " + updateType);
                }
            }
        };

        JsonUtil.updateElementInJson(fileName, clazz, condition, updater);
    }

    private static <T> void updateDrinkItemInJson(String fileName, Class<T> clazz, String itemName, Object newValue, MenuUpdateType updateType) {
        Predicate<T> condition = item -> {
            if (item instanceof Drink drink) {
                return drink.getName().equals(itemName);
            }
            return false;
        };

        Consumer<T> updater = item -> {
            if (item instanceof Drink drink) {
                switch (updateType) {
                    case IS_ALCOHOLIC -> {
                        ensureType(newValue, Boolean.class);
                        drink.setAlcoholic((Boolean) newValue);
                    }
                    case VOLUME -> {
                        ensureType(newValue, Integer.class);
                        drink.setVolume((Integer) newValue);
                    }
                    default -> throw new UnsupportedOperationException("Unsupported drink update type: " + updateType);
                }
            }
        };

        JsonUtil.updateElementInJson(fileName, clazz, condition, updater);
    }

    private static <T> void updateFoodItemInJson(String fileName, Class<T> clazz, String itemName, Object newValue, MenuUpdateType updateType) {
        Predicate<T> condition = item -> {
            if (item instanceof Food food) {
                return food.getName().equals(itemName);
            }
            return false;
        };

        Consumer<T> updater = item -> {
            if (item instanceof Food food) {
                switch (updateType) {
                    case CUISINE_TYPE -> {
                        ensureType(newValue, String.class);
                        food.setCuisineType((String) newValue);
                    }
                    case IS_MAIN_COURSE -> {
                        ensureType(newValue, Boolean.class);
                        food.setMainCourse((Boolean) newValue);
                    }
                    case IS_DESSERT -> {
                        ensureType(newValue, Boolean.class);
                        food.setDessert((Boolean) newValue);
                    }
                    default -> throw new UnsupportedOperationException("Unsupported food update type: " + updateType);
                }
            }
        };

        JsonUtil.updateElementInJson(fileName, clazz, condition, updater);
    }

    public void displayMenu() {
        System.out.println("Drinks:");
        for (Drink drink : drinks) {
            System.out.println(drink);
        }
        System.out.println("Foods:");
        for (Food food : foods) {
            System.out.println(food);
        }
    }
}

