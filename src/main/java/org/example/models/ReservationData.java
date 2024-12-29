package org.example.models;

import java.time.LocalDateTime;

public class ReservationData {
    private String fullName;
    private String phoneNumber;
    private LocalDateTime reservationDateTime;
    private int numberOfPeople;

    ReservationData() {
        fullName = "";
        phoneNumber = "";
        reservationDateTime = LocalDateTime.now();
        numberOfPeople = 0;
    }

    public ReservationData(String fullName,String phoneNumber, LocalDateTime reservationDateTime, int numberOfPeople) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.reservationDateTime = reservationDateTime;
        this.numberOfPeople = numberOfPeople;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
