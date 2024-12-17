package org.example.models;

import org.example.enums.TableStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    private RestaurantData restaurantData;
    private Menu menu;
    private Staff staff;
    private final ArrayList<Reservation> reservations;
    private final ArrayList<Table> tables;
    private final HashMap<LocalDateTime,ClientOrder> ordersByDateTime = new HashMap<>();
    private final HashMap<Integer,ArrayList<Integer>> tablesByCapacity = new HashMap<>();
    private final HashMap<Integer, Integer> tablesByID = new HashMap<>();
    private final HashMap<String,ArrayList<Integer>> tablesByStatus = new HashMap<>();
    private final HashMap<String,Integer> reservationByPersonName = new HashMap<>();
    private final HashMap<LocalDateTime,Integer> reservationByDateTime = new HashMap<>();

    public Restaurant() {
        this.restaurantData = new RestaurantData();
        this.menu = new Menu();
        this.staff = new Staff();
        this.reservations = new ArrayList<>();
        this.tables = new ArrayList<>();
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public RestaurantData getRestaurantData() {
        return restaurantData;
    }

    public void setRestaurantData(RestaurantData restaurantData) {
        this.restaurantData = restaurantData;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void MakeReservation(String fullName, String phoneNumber, LocalDateTime reservationDateTime, int numberOfPeople){
        if(reservationByPersonName.containsKey(fullName)){
            System.out.println("Rezervarea nu a putut fi efectuată. O rezervare cu acest nume există deja.");
            return;
        }
        if(reservationByDateTime.containsKey(reservationDateTime)){
            System.out.println("Rezervarea nu a putut fi efectuată. O rezervare la această dată și oră există deja.");
            return;
        }
        if(tablesByStatus.get("liber").isEmpty()){
            System.out.println("Rezervarea nu a putut fi efectuată. Nu există mese libere.");
            return;
        }

        for(int i=0;i<tablesByStatus.get("liber").size();i++)
        {
            int tableIndex = tablesByStatus.get("liber").get(i);
            if(tables.get(tableIndex).getNumberOfSeats()>=numberOfPeople)
            {
                tables.get(tableIndex).reserveTable();
                Reservation newReservation = new Reservation(fullName,phoneNumber,reservationDateTime,numberOfPeople);
                newReservation.setTableId(String.valueOf(tables.get(tableIndex).getTableId()));
                reservations.add(newReservation);
                reservationByPersonName.put(fullName,reservations.size()-1);
                reservationByDateTime.put(reservationDateTime,reservations.size()-1);
                tablesByStatus.get("liber").remove(tableIndex);
                tablesByStatus.get("rezervat").add(tableIndex);
                return;
            }
        }
        System.out.println("Rezervarea nu a putut fi efectuată. Nu există mese cu numărul de locuri necesar.");
    }

    public void CancelReservation(String fullName){
        if(!reservationByPersonName.containsKey(fullName)){
            System.out.println("Rezervarea nu a putut fi anulată. O rezervare cu acest nume nu există.");
            return;
        }
        int reservationIndex = reservationByPersonName.get(fullName);
        LocalDateTime reservationDateTime = reservations.get(reservationIndex).getReservationDateTime();
        int tableIndex = Integer.parseInt(reservations.get(reservationIndex).getTableId());
        tables.get(tableIndex).freeTable();
        tablesByStatus.get("rezervat").remove(tableIndex);
        tablesByStatus.get("liber").add(tableIndex);
        reservations.remove(reservationIndex);
        reservationByPersonName.remove(fullName);
        reservationByDateTime.remove(reservationDateTime);
    }

    public void UpdateReservation(String fullName, String phoneNumber, LocalDateTime reservationDateTime, int numberOfPeople){
        if(!reservationByPersonName.containsKey(fullName)){
            System.out.println("Rezervarea nu a putut fi actualizată. O rezervare cu acest nume nu există.");
            return;
        }

        if(tablesByCapacity.get(numberOfPeople).isEmpty()){
            System.out.println("Rezervarea nu a putut fi actualizată. Nu există mese cu numărul de locuri necesar.");
            return;
        }

        if(reservationByDateTime.containsKey(reservationDateTime)){
            System.out.println("Rezervarea nu a putut fi actualizată. O rezervare la această dată și oră există deja.");
            return;
        }

        int currentTableIndex = Integer.parseInt(reservations.get(reservationByPersonName.get(fullName)).getTableId());

        for(int i=0;i<tablesByStatus.get("liber").size();i++)
        {
            int tableIndex = tablesByStatus.get("liber").get(i);
            if(tables.get(tableIndex).getNumberOfSeats()>=numberOfPeople && tableIndex!=currentTableIndex)
            {
                tables.get(tableIndex).reserveTable();
                LocalDateTime oldReservationDateTime = reservations.get(reservationByPersonName.get(fullName)).getReservationDateTime();
                reservations.get(reservationByPersonName.get(fullName)).setFullName(fullName);
                reservations.get(reservationByPersonName.get(fullName)).setPhoneNumber(phoneNumber);
                reservations.get(reservationByPersonName.get(fullName)).setReservationDateTime(reservationDateTime);
                reservations.get(reservationByPersonName.get(fullName)).setNumberOfPeople(numberOfPeople);
                reservations.get(reservationByPersonName.get(fullName)).setTableId(String.valueOf(tables.get(tableIndex).getTableId()));
                reservationByDateTime.remove(oldReservationDateTime);
                reservationByDateTime.put(reservationDateTime,reservationByPersonName.get(fullName));
                return;
            }
        }
        System.out.println("Rezervarea nu a putut fi actualizată. Nu există mese cu numărul de locuri necesar.");
    }

    public void addTable(int numberOfSeats){
        Table newTable = new Table(numberOfSeats);
        tables.add(newTable);
        tablesByID.put(newTable.getTableId(),tables.size()-1);
        if(tablesByCapacity.containsKey(numberOfSeats)){
            tablesByCapacity.get(numberOfSeats).add(tables.size()-1);
        }else{
            ArrayList<Integer> newTableList = new ArrayList<>();
            newTableList.add(tables.size()-1);
            tablesByCapacity.put(numberOfSeats,newTableList);
        }
        if (tablesByStatus.containsKey(newTable.getStatus().toString())) {
            tablesByStatus.get(newTable.getStatus().toString()).add(tables.size() - 1);
        } else {
            ArrayList<Integer> newTableList = new ArrayList<>();
            newTableList.add(tables.size() - 1);
            tablesByStatus.put(newTable.getStatus().toString(), newTableList);
        }

    }

    public void removeTable(int tableId){
        if(!tablesByID.containsKey(tableId)){
            System.out.println("Masa nu a putut fi ștearsă. Masa cu acest ID nu există.");
            return;
        }
        int tableIndex = tablesByID.get(tableId);
        tablesByID.remove(tableId);
        tablesByCapacity.get(tables.get(tableIndex).getNumberOfSeats()).remove(tableIndex);
        if(tablesByStatus.containsKey(tables.get(tableIndex).getStatus().toString())){
            tablesByStatus.get(tables.get(tableIndex).getStatus().toString()).remove(tableIndex);
        }
        tables.remove(tableIndex);
    }

    public void updateTable(int tableId, int newNumberOfSeats, TableStatus newStatus){
        if(!tablesByID.containsKey(tableId)){
            System.out.println("Masa nu a putut fi actualizată. Masa cu acest ID nu există.");
            return;
        }
        int tableIndex = tablesByID.get(tableId);
        if(tables.get(tableIndex).getNumberOfSeats()!=newNumberOfSeats){
            tablesByCapacity.get(tables.get(tableIndex).getNumberOfSeats()).remove(tableIndex);
            tables.get(tableIndex).setNumberOfSeats(newNumberOfSeats);
            if(tablesByCapacity.containsKey(newNumberOfSeats)){
                tablesByCapacity.get(newNumberOfSeats).add(tableIndex);
            }else{
                ArrayList<Integer> newTableList = new ArrayList<>();
                newTableList.add(tableIndex);
                tablesByCapacity.put(newNumberOfSeats,newTableList);
            }
        }
        if (!tables.get(tableIndex).getStatus().equals(newStatus)) {
            tablesByStatus.get(tables.get(tableIndex).getStatus().toString()).remove(tableIndex);
            tables.get(tableIndex).setStatus(newStatus);
            if (tablesByStatus.containsKey(newStatus.toString())) {
                tablesByStatus.get(newStatus.toString()).add(tableIndex);
            } else {
                ArrayList<Integer> newTableList = new ArrayList<>();
                newTableList.add(tableIndex);
                tablesByStatus.put(newStatus.toString(), newTableList);
            }
        }

    }

    public Table findTableById(int tableId){
        if(!tablesByID.containsKey(tableId)){
            System.out.println("Masa cu acest ID nu există.");
            return null;
        }
        return tables.get(tablesByID.get(tableId));
    }

    public HashMap<LocalDateTime, ClientOrder> getOrdersByDateTime() {
        return ordersByDateTime;
    }

    public void addOrder(LocalDateTime dateTime, ClientOrder order){
        ordersByDateTime.put(dateTime,order);
    }

    public void removeOrder(LocalDateTime dateTime){
        ordersByDateTime.remove(dateTime);
    }
}
