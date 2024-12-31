package org.example.interfaces;

import java.time.LocalDateTime;

public interface IReservation {
    String getFullName();
    LocalDateTime getReservationDateTime();
    String getPhoneNumber();
    int getNumberOfPeople();
    int getTableId();
}
