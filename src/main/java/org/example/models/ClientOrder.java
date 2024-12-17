package org.example.models;

import java.util.ArrayList;
import java.util.UUID;

public class ClientOrder {
    private ArrayList<MenuItem> items;
    private int tableId;
    private final String orderId;

    public ClientOrder() {
        items = new ArrayList<>();
        this.orderId = UUID.randomUUID().toString();
    }

    public ClientOrder(ArrayList<MenuItem> items, int tableId) {
        this.items = items;
        this.tableId = tableId;
        this.orderId = UUID.randomUUID().toString();
    }

    public ClientOrder(ArrayList<MenuItem> items) {
        this.items = items;
        this.orderId = UUID.randomUUID().toString();
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.items = items;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getOrderId() {
        return Integer.parseInt(orderId);
    }

    @Override
    public String toString() {
        return "ClientOrder{" +
                "items=" + items +
                ", tableId=" + tableId +
                ", orderId=" + orderId +
                '}';
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }
}
