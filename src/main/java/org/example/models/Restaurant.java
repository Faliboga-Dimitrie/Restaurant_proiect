package org.example.models;

import java.time.LocalDateTime;
import java.util.*;

public class Restaurant {
    private RestaurantData restaurantData;
    private Menu menu;
    private Staff staff;
    private ArrayList<Reservation> reservations;
    private ArrayList<Table> tables;
    private final HashMap<Integer,ArrayList<Integer>> tablesByCapacity = new HashMap<>();
    private final HashMap<Integer, Integer> tablesByID = new HashMap<>();
    private final HashMap<String,ArrayList<Integer>> tablesByStatus = new HashMap<>();
    private final HashMap<String,Integer> reservationByPersonName = new HashMap<>();
    private final HashMap<LocalDateTime,Integer> reservationByDateTime = new HashMap<>();

    public Restaurant() {
    }

    private void PopulateHashTables(){
        for(int i=0;i<tables.size();i++){
            if(tablesByCapacity.containsKey(tables.get(i).getNumberOfSeats())){
                tablesByCapacity.get(tables.get(i).getNumberOfSeats()).add(i);
            }else{
                ArrayList<Integer> newTable = new ArrayList<>();
                newTable.add(i);
                tablesByCapacity.put(tables.get(i).getNumberOfSeats(),newTable);
            }
            tablesByID.put(Integer.parseInt(tables.get(i).getTableId()),i);
            if(tablesByStatus.containsKey(tables.get(i).getStatus())){
                tablesByStatus.get(tables.get(i).getStatus()).add(i);
            }else{
                ArrayList<Integer> newTable = new ArrayList<>();
                newTable.add(i);
                tablesByStatus.put(tables.get(i).getStatus(),newTable);
            }
        }
        for(int i=0;i<reservations.size();i++){
            reservationByPersonName.put(reservations.get(i).getFullName(),i);
            reservationByDateTime.put(reservations.get(i).getReservationDateTime(),i);
        }
    }

    public Restaurant(RestaurantData restaurantData, Menu menu, Staff staff, ArrayList<Reservation> reservations, ArrayList<Table> tables) {
        this.restaurantData = restaurantData;
        this.menu = menu;
        this.staff = staff;
        this.reservations = reservations;
        this.tables = tables;
        PopulateHashTables();
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

    public void MakeReservation(String fullName, String phoneNumber, LocalDateTime reservationDateTime,
                                int numberOfPeople, String specialRequests){
        Reservation newReservation = new Reservation(fullName,phoneNumber,reservationDateTime,numberOfPeople,specialRequests);
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
        else {
            int tableIndex = tablesByStatus.get("liber").get(0);
            if(tables.get(tableIndex).reserve()){
                newReservation.setTableId(tables.get(tableIndex).getTableId());
            }
            else
                return;
        }
        reservations.add(newReservation);
        reservationByPersonName.put(fullName,reservations.size()-1);
        reservationByDateTime.put(reservationDateTime,reservations.size()-1);
    }

    public void CancelReservation(String fullName){
        if(!reservationByPersonName.containsKey(fullName)){
            System.out.println("Rezervarea nu a putut fi anulată. O rezervare cu acest nume nu există.");
            return;
        }
        int reservationIndex = reservationByPersonName.get(fullName);
        LocalDateTime reservationDateTime = reservations.get(reservationIndex).getReservationDateTime();
        int tableIndex = Integer.parseInt(tables.get(tablesByID.get(Integer.parseInt(reservations.get(reservationIndex).getTableId()))).getTableId());
        tables.get(tableIndex).free();
        reservations.remove(reservationIndex);
        reservationByPersonName.remove(fullName);
        reservationByDateTime.remove(reservationDateTime);
    }

    public void UpdateReservation(String fullName, String phoneNumber, String email, LocalDateTime reservationDateTime,
                                  int numberOfPeople, String specialRequests){
        if(!reservationByPersonName.containsKey(fullName)){
            System.out.println("Rezervarea nu a putut fi actualizată. O rezervare cu acest nume nu există.");
            return;
        }
        int reservationIndex = reservationByPersonName.get(fullName);
        LocalDateTime oldReservationDateTime = reservations.get(reservationIndex).getReservationDateTime();
        int tableIndex = Integer.parseInt(tables.get(tablesByID.get(Integer.parseInt(reservations.get(reservationIndex).getTableId()))).getTableId());
        tables.get(tableIndex).free();
        reservations.get(reservationIndex).setFullName(fullName);
        reservations.get(reservationIndex).setPhoneNumber(phoneNumber);
        reservations.get(reservationIndex).setReservationDateTime(reservationDateTime);
        reservations.get(reservationIndex).setNumberOfPeople(numberOfPeople);
        if(reservationByDateTime.containsKey(reservationDateTime)){
            System.out.println("Rezervarea nu a putut fi actualizată. O rezervare la această dată și oră există deja.");
            return;
        }
        reservations.get(reservationIndex).setTableId(tables.get(tableIndex).getTableId());
        reservationByDateTime.remove(oldReservationDateTime);
        reservationByDateTime.put(reservationDateTime,reservationIndex);
    }

    public void addTable(int numberOfSeats, String status){}

    public void removeTable(int tableId){
        if(!tablesByID.containsKey(tableId)){
            System.out.println("Masa nu a putut fi ștearsă. Masa cu acest ID nu există.");
            return;
        }
        int tableIndex = tablesByID.get(tableId);
        if(tables.get(tableIndex).getStatus().equals("ocupat")){
            System.out.println("Masa nu a putut fi ștearsă. Masa este ocupată.");
            return;
        }
        tables.remove(tableIndex);
        tablesByID.remove(tableId);
        tablesByCapacity.get(tables.get(tableIndex).getNumberOfSeats()).remove(tableIndex);
        tablesByStatus.get(tables.get(tableIndex).getStatus()).remove(tableIndex);
    }

    public void updateTable(int tableId, int numberOfSeats, String status){}

    public void addStaff(String name, String surname, int age, String email, String phoneNumber, LocalDateTime dateOfBirth,
                         String role, double salary, LocalDateTime hireDate){}

    public void removeStaff(int staffId){}

    public void updateStaff(int staffId, String name, String surname, int age, String email, String phoneNumber, LocalDateTime dateOfBirth,
                            String role, double salary, LocalDateTime hireDate){}


}
