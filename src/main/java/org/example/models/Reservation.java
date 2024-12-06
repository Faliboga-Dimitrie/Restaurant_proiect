package org.example.models;

import java.time.LocalDateTime;

public class Reservation {
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDateTime reservationDateTime;
    private int numberOfPeople;
    private String specialRequests;
    private String status;
    private boolean isPaid;
    private String paymentMethod;

    public Reservation(String fullName, String phoneNumber, String email, LocalDateTime reservationDateTime,
                       int numberOfPeople, String specialRequests) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservationDateTime = reservationDateTime;
        this.numberOfPeople = numberOfPeople;
        this.specialRequests = specialRequests;
        this.status = "confirmată";
        this.isPaid = false;
        this.paymentMethod = "N/A";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Rezervare: " + fullName + " (" + numberOfPeople + " persoane) - " + reservationDateTime +
                "\nStatus: " + status + "\nCerinte speciale: " + (specialRequests.isEmpty() ? "N/A" : specialRequests);
    }
}

