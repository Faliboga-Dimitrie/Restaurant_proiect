package org.example.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class ClientOrder {
    private ArrayList<Pair<MenuItem,Integer>> items;
    private String clientName;
    private LocalDateTime orderTime;
    private final String orderId;

    public ClientOrder() {
        items = new ArrayList<>();
        this.orderId = UUID.randomUUID().toString();
    }

    public ClientOrder(String clientName, LocalDateTime orderTime) {
        this.clientName = clientName;
        this.orderTime = orderTime;
        items = new ArrayList<>();
        this.orderId = UUID.randomUUID().toString();
    }

    public ArrayList<Pair<MenuItem,Integer>> getItems() {
        return items;
    }

    public void setItems(ArrayList<Pair<MenuItem,Integer>> items) {
        this.items = items;
    }

    public int getOrderId() {
        return Integer.parseInt(orderId);
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Pair<MenuItem, Integer> pair : items) {
            totalPrice += pair.getFirst().getPrice() * pair.getSecond();
        }
        return totalPrice;
    }

    public int getQuantity(MenuItem item) {
        for (Pair<MenuItem, Integer> pair : items) {
            if (pair.getFirst().equals(item)) {
                return pair.getSecond();
            }
        }
        return 0;
    }

    public void addItem(MenuItem item, int quantity) {
        items.add(new Pair<>(item, quantity));
    }

    public void removeItem(MenuItem item, int quantity) {
        items.remove(new Pair<>(item, quantity));
    }

    @Override
    public String toString() {
        return "ClientOrder{" +
                "clientName='" + clientName + '\'' +
                ", items=" + items +
                ", orderTime=" + orderTime +
                ", orderId='" + orderId + '\'' +
                '}';
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
        for (Pair<MenuItem, Integer> pair : items) {
            if (pair.getFirst().equals(item)) {
                pair.setSecond(quantity);
                return;
            }
        }
    }
}
