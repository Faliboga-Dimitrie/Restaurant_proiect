package org.example.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientOrder {
    private ArrayList<Pair<Food,Integer>> foodItems;
    private ArrayList<Pair<Drink,Integer>> drinkItems;
    private String clientName;
    private LocalDateTime orderTime;
    private static int nextId = 1;
    private final int orderId;

    public ClientOrder() {
        foodItems = new ArrayList<>();
        drinkItems = new ArrayList<>();
        this.orderId = nextId++;
    }

    public ClientOrder(String clientName, LocalDateTime orderTime) {
        this.clientName = clientName;
        this.orderTime = orderTime;
        foodItems = new ArrayList<>();
        drinkItems = new ArrayList<>();
        this.orderId = nextId++;
    }

    public ArrayList<Pair<Food,Integer>> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<Pair<Food,Integer>> foodItems) {
        this.foodItems = foodItems;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Pair<Food, Integer> pair : foodItems) {
            totalPrice += pair.getFirst().getPrice() * pair.getSecond();
        }
        for (Pair<Drink, Integer> pair : drinkItems) {
            totalPrice += pair.getFirst().getPrice() * pair.getSecond();
        }
        return totalPrice;
    }

    public int getQuantity(MenuItem item) {
        switch (item) {
            case Food food -> {
                return getTotalQuantity(foodItems);
            }
            case Drink drink -> {
                return getTotalQuantity(drinkItems);
            }
            default -> throw new IllegalStateException("Unexpected value: " + item);
        }
    }

    private <T extends MenuItem> int getTotalQuantity(ArrayList<Pair<T, Integer>> items) {
        int quantity = 0;
        for (Pair<T, Integer> pair : items) {
            quantity += pair.getSecond();
        }
        return quantity;
    }

    public void addItem(MenuItem item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cantitatea trebuie să fie mai mare decât 0.");
        }

        switch (item) {
            case Food food -> addToItems(foodItems, food, quantity);
            case Drink drink -> addToItems(drinkItems, drink, quantity);
            default -> throw new IllegalStateException("Unexpected value: " + item);
        }
    }

    private <T extends MenuItem> void addToItems(ArrayList<Pair<T, Integer>> items, T item, int quantity) {
        items.add(new Pair<>(item, quantity));
    }

    public void removeItem(MenuItem item, int quantity) {
        switch (item) {
            case Food food -> removeFromItems(foodItems, food, quantity);
            case Drink drink -> removeFromItems(drinkItems, drink, quantity);
            default -> throw new IllegalStateException("Unexpected value: " + item);
        }
    }

    private <T extends MenuItem> void removeFromItems(ArrayList<Pair<T, Integer>> items, T item, int quantity) {
        Iterator<Pair<T, Integer>> iterator = items.iterator();
        while (iterator.hasNext()) {
            Pair<T, Integer> pair = iterator.next();
            if (pair.getFirst().equals(item)) {
                int newQuantity = pair.getSecond() - quantity;
                if (newQuantity <= 0) {
                    iterator.remove(); // Îndepărtează itemul din colecție
                } else {
                    pair.setSecond(newQuantity); // Actualizează cantitatea
                }
                return;
            }
        }
        System.out.println("Itemul nu a fost găsit în colecție.");
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public void changeQuantity(MenuItem item, int quantity) {
        switch (item) {
            case Food food -> { // Dacă item este de tipul Food
                for (Pair<Food, Integer> pair : foodItems) {
                    if (pair.getFirst().equals(food)) {
                        pair.setSecond(quantity);
                        return;
                    }
                }
            }
            case Drink drink -> { // Dacă item este de tipul Drink
                for (Pair<Drink, Integer> pair : drinkItems) {
                    if (pair.getFirst().equals(drink)) {
                        pair.setSecond(quantity);
                        return;
                    }
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + item); // Dacă tipul nu este Food sau Drink
        }
    }

    public ArrayList<Pair<Drink, Integer>> getDrinkItems() {
        return drinkItems;
    }

    public void setDrinkItems(ArrayList<Pair<Drink, Integer>> drinkItems) {
        this.drinkItems = drinkItems;
    }

    @Override
    public String toString() {
        return "ClientOrder{" +
                "clientName='" + clientName + '\'' +
                ", foodItems=" + foodItems +
                ", drinkItems=" + drinkItems +
                ", orderTime=" + orderTime +
                '}';
    }
}
