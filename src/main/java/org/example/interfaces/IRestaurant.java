package org.example.interfaces;

import org.example.enums.TableStatus;
import org.example.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IRestaurant {
    Menu getMenu();
    Staff getStaff();
    ArrayList<Reservation> getReservations();
    ArrayList<Table> getTables();
    ArrayList<ClientOrder> getOrders();
    RestaurantData getRestaurantData();

    void addReservation(Reservation reservation);
    void makeReservation(ReservationData reservationData);
    void cancelReservation(String fullName);
    Reservation findReservationByPersonName(String fullName);
    Reservation findReservationByDateTime(LocalDateTime dateTime);

    void addTable(int numberOfSeats,int tableNumber, boolean fromJson);
    void addTable(Table table);
    void updateTableStatus(int tableId, TableStatus newStatus);
    void removeTable(int tableId);
    Table findTableByNumber(int tableId);

    ClientOrder findOrderByClientName(String clientName);
    ClientOrder findOrderByDateTime(LocalDateTime dateTime);
    void addOrder(LocalDateTime dateTime, ClientOrder order, boolean fromJson);
    void removeOrder(Object dateTime_or_Name);
}
