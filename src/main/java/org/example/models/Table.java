package org.example.models;

import java.util.UUID;

public class Table {
    private final String tableId;
    private final int numberOfSeats;
    private String status;
    private String location;

    // Constructor
    public Table(int numberOfSeats, String location) {
        this.tableId = UUID.randomUUID().toString();
        this.numberOfSeats = numberOfSeats;
        this.status = "liber";
        this.location = location;
    }

    // Getters și Setters
    public String getTableId() {
        return tableId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean reserve() {
        if (status.equals("liber")) {
            status = "rezervat";
            return true;
        } else {
            System.out.println("Masa nu este disponibilă pentru rezervare.");
            return false;
        }
    }

    public boolean occupy() {
        if (status.equals("rezervat") || status.equals("liber")) {
            status = "ocupat";
            System.out.println("Masa a fost ocupată.");
            return true;
        } else {
            System.out.println("Masa nu poate fi ocupată.");
            return false;
        }
    }

    public boolean free() {
        if (status.equals("ocupat")) {
            status = "liber";
            System.out.println("Masa a fost eliberată.");
            return true;
        } else {
            System.out.println("Masa nu poate fi eliberată.");
            return false;
        }
    }

    @Override
    public String toString() {
        return "Masa " + tableId + " (" + location + ") - " + status + " - Locuri: " + numberOfSeats;
    }
}
