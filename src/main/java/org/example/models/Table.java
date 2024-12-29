package org.example.models;

import org.example.enums.TableStatus;

public class Table {
    private int tableNumber;
    private int numberOfSeats;
    private TableStatus status;

    public Table() {
        this.status = TableStatus.FREE;
    }

    public Table(int numberOfSeats,int tableNumber) {
        this.tableNumber = tableNumber;
        this.numberOfSeats = numberOfSeats;
        this.status = TableStatus.FREE;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public void reserveTable() {
        if (status.equals(TableStatus.FREE)) {
            status = TableStatus.RESERVED;
            return;
        }
        System.out.println("Masa nu este disponibilă pentru rezervare.");
    }

    public boolean occupyTable() {
        if (status.equals(TableStatus.RESERVED) || status.equals(TableStatus.FREE)) {
            status = TableStatus.OCCUPIED;
            System.out.println("Masa a fost ocupată.");
            return true;
        }
        System.out.println("Masa nu poate fi ocupată.");
        return false;
    }

    public void freeTable() {
        if (status.equals(TableStatus.OCCUPIED) || status.equals(TableStatus.RESERVED)) {
            status = TableStatus.FREE;
            System.out.println("Masa a fost eliberată.");
            return;
        }
        System.out.println("Masa nu poate fi eliberată.");
    }

    @Override
    public String toString() {
        return "Masa " + tableNumber + " " + status + " - Locuri: " + numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
