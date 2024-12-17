package org.example.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private final String reservationId;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime reservationDateTime;
    private int numberOfPeople;
    private String tableId;

    public Reservation(String fullName, String phoneNumber, LocalDateTime reservationDateTime,
                       int numberOfPeople, String specialRequests) {
        this.reservationId = UUID.randomUUID().toString();
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.reservationDateTime = reservationDateTime;
        this.numberOfPeople = numberOfPeople;
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

    public String getReservationId() {
        return reservationId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}

