package org.example.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private static int nextId = 1;
    private final int reservationId;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime reservationDateTime;
    private int numberOfPeople;
    private int tableId;

    public Reservation() {
        this.reservationId = nextId++;
    }

    public Reservation(ReservationData reservationData) {
        this.reservationId = nextId++;
        this.fullName = reservationData.getFullName();
        this.phoneNumber = reservationData.getPhoneNumber();
        this.reservationDateTime = reservationData.getReservationDateTime();
        this.numberOfPeople = reservationData.getNumberOfPeople();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    @Override
    public String toString() {
        return "Rezervare: " + fullName + " (" + numberOfPeople + " persoane) - " + reservationDateTime +
                "\nStatus: " + tableId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}

