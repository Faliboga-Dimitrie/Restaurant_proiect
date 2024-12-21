package org.example.models;

import org.example.enums.TableStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    private RestaurantData restaurantData;
    private Menu menu;
    private Staff staff;
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    private final ArrayList<Table> tables = new ArrayList<>();
    private final ArrayList<ClientOrder> orders = new ArrayList<>();
    private final HashMap<LocalDateTime,Integer> ordersByDateTime = new HashMap<>();
    private final HashMap<String,Integer> ordersByName = new HashMap<>();
    private final HashMap<Integer,ArrayList<Integer>> tablesByCapacity = new HashMap<>();
    private final HashMap<Integer, Integer> tablesByID = new HashMap<>();
    private final HashMap<String,ArrayList<Integer>> tablesByStatus = new HashMap<>();
    private final HashMap<String,Integer> reservationByPersonName = new HashMap<>();
    private final HashMap<LocalDateTime,Integer> reservationByDateTime = new HashMap<>();

    public Restaurant() {
        this.restaurantData = new RestaurantData();
        this.menu = new Menu();
        this.staff = new Staff();
    }

    public Menu getMenu() {
        return menu;
    }

    public Staff getStaff() {
        return staff;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void makeReservation(ReservationData reservationData){
        String FullName = reservationData.getFullName();
        LocalDateTime ReservationDateTime = reservationData.getReservationDateTime();
        int NumberOfPeople = reservationData.getNumberOfPeople();

        if(reservationByPersonName.containsKey(FullName)){
            System.out.println("Rezervarea nu a putut fi efectuată. O rezervare cu acest nume există deja.");
            return;
        }
        if(reservationByDateTime.containsKey(ReservationDateTime)){
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
            if(tables.get(tableIndex).getNumberOfSeats()>=NumberOfPeople)
            {
                tables.get(tableIndex).reserveTable();
                Reservation newReservation = new Reservation(reservationData);
                newReservation.setTableId(String.valueOf(tables.get(tableIndex).getTableId()));
                reservations.add(newReservation);
                reservationByPersonName.put(FullName,reservations.size()-1);
                reservationByDateTime.put(ReservationDateTime,reservations.size()-1);
                tablesByStatus.get("liber").remove(tableIndex);
                tablesByStatus.get("rezervat").add(tableIndex);
                JsonUtil.appendToJson(newReservation,"reservations.json",Reservation.class);
                return;
            }
        }
        System.out.println("Rezervarea nu a putut fi efectuată. Nu există mese cu numărul de locuri necesar.");
    }

    public void cancelReservation(String fullName){
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
        JsonUtil.removeFromJson("reservations.json",Reservation.class,reservation->reservation.getFullName().equals(fullName));
    }

    private void updateReservation(
            Reservation reservation,
            String fullName,
            String phoneNumber,
            LocalDateTime reservationDateTime,
            int numberOfPeople,
            String tableId
    ) {
        reservation.setFullName(fullName);
        reservation.setPhoneNumber(phoneNumber);
        reservation.setReservationDateTime(reservationDateTime);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setTableId(tableId);

        JsonUtil.updateElementInJson(
                "reservations.json",
                Reservation.class,
                item -> item.getReservationDateTime().equals(reservation.getReservationDateTime()),
                item -> {
                    item.setFullName(fullName);
                    item.setPhoneNumber(phoneNumber);
                    item.setReservationDateTime(reservationDateTime);
                    item.setNumberOfPeople(numberOfPeople);
                    item.setTableId(tableId);
                }
        );
    }


    public void updateReservation(ReservationData reservationData){
        String fullName = reservationData.getFullName();
        String phoneNumber = reservationData.getPhoneNumber();
        LocalDateTime reservationDateTime = reservationData.getReservationDateTime();
        int numberOfPeople = reservationData.getNumberOfPeople();

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
            if (tables.get(tableIndex).getNumberOfSeats() >= numberOfPeople && tableIndex != currentTableIndex) {
                tables.get(tableIndex).reserveTable();

                Reservation reservation = reservations.get(reservationByPersonName.get(fullName));

                LocalDateTime oldReservationDateTime = reservation.getReservationDateTime();

                updateReservation(reservation, fullName, phoneNumber, reservationDateTime, numberOfPeople, String.valueOf(tables.get(tableIndex).getTableId()));

                reservationByDateTime.remove(oldReservationDateTime);
                reservationByDateTime.put(reservationDateTime, reservationByPersonName.get(fullName));

                return;
            }

        }
        System.out.println("Rezervarea nu a putut fi actualizată. Nu există mese cu numărul de locuri necesar.");
    }

    public void addTable(int numberOfSeats, boolean fromJson){
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
        if(!fromJson)
            JsonUtil.appendToJson(newTable,"tables.json",Table.class);
    }

    public void addTable(Table table)
    {
        addTable(table.getNumberOfSeats(),true);
    }

    public void removeTable(int tableId){
        if(!tablesByID.containsKey(tableId)){
            System.out.println("Masa nu a putut fi ștearsă. Masa cu acest ID nu există.");
            return;
        }
        int tableIndex = tablesByID.get(tableId);
        JsonUtil.removeFromJson("tables.json",Table.class,table->table.getTableId()==tableId);
        tablesByID.remove(tableId);
        tablesByCapacity.get(tables.get(tableIndex).getNumberOfSeats()).remove(tableIndex);
        if(tablesByStatus.containsKey(tables.get(tableIndex).getStatus().toString())){
            tablesByStatus.get(tables.get(tableIndex).getStatus().toString()).remove(tableIndex);
        }
        tables.remove(tableIndex);
        JsonUtil.removeFromJson("tables.json",Table.class,table->table.getTableId()==tableId);
    }

    public void updateTable(int tableId, int newNumberOfSeats, TableStatus newStatus) {
        if (!tablesByID.containsKey(tableId)) {
            System.out.println("Masa nu a putut fi actualizată. Masa cu acest ID nu există.");
            return;
        }

        int tableIndex = tablesByID.get(tableId);

        updateTableSeats(tableIndex, newNumberOfSeats);

        updateTableStatus(tableIndex, newStatus);

        saveTableToJson(tables.get(tableIndex));
    }

    private void updateTableSeats(int tableIndex, int newNumberOfSeats) {
        Table table = tables.get(tableIndex);

        if (table.getNumberOfSeats() != newNumberOfSeats) {
            tablesByCapacity.get(table.getNumberOfSeats()).remove(tableIndex);
            table.setNumberOfSeats(newNumberOfSeats);
            tablesByCapacity.computeIfAbsent(newNumberOfSeats, k -> new ArrayList<>()).add(tableIndex);
        }
    }

    private void updateTableStatus(int tableIndex, TableStatus newStatus) {
        Table table = tables.get(tableIndex);

        if (!table.getStatus().equals(newStatus)) {
            tablesByStatus.get(table.getStatus().toString()).remove(tableIndex);
            table.setStatus(newStatus);
            tablesByStatus.computeIfAbsent(newStatus.toString(), k -> new ArrayList<>()).add(tableIndex);
        }
    }

    private void saveTableToJson(Table table) {
        JsonUtil.updateElementInJson(
                "tables.json",
                Table.class,
                t -> t.getTableId() == table.getTableId(),
                t -> {
                    t.setNumberOfSeats(table.getNumberOfSeats());
                    t.setStatus(table.getStatus());
                }
        );
    }

    public Table findTableById(int tableId){
        if(!tablesByID.containsKey(tableId)){
            System.out.println("Masa cu acest ID nu există.");
            return null;
        }
        return tables.get(tablesByID.get(tableId));
    }

    public ClientOrder findOrderByClientName(String clientName){
        if(!ordersByName.containsKey(clientName)){
            System.out.println("Comanda cu acest nume nu există.");
            return null;
        }
        return orders.get(ordersByName.get(clientName));
    }

    public ClientOrder findOrderByDateTime(LocalDateTime dateTime){
        if(!ordersByDateTime.containsKey(dateTime)){
            System.out.println("Comanda la această dată și oră nu există.");
            return null;
        }
        return orders.get(ordersByDateTime.get(dateTime));
    }

    public void addOrder(LocalDateTime dateTime, ClientOrder order){
        orders.add(order);
        ordersByDateTime.put(dateTime,orders.size()-1);
        ordersByName.put(order.getClientName(),orders.size()-1);
        JsonUtil.appendToJson(order,"orders.json",ClientOrder.class);
    }

    public void removeOrder(Object dateTime_or_Name) {
        if (dateTime_or_Name instanceof LocalDateTime) {
            removeOrderByDateTime((LocalDateTime) dateTime_or_Name);
        } else if (dateTime_or_Name instanceof String) {
            removeOrderByName((String) dateTime_or_Name);
        } else {
            System.out.println("Tipul de identificator nu este valid.");
        }
        JsonUtil.removeFromJson("orders.json",ClientOrder.class,
                order->order.getOrderTime().equals(dateTime_or_Name)
                        || order.getClientName().equals(dateTime_or_Name));
    }

    private void removeOrderByDateTime(LocalDateTime dateTime) {
        if (!ordersByDateTime.containsKey(dateTime)) {
            System.out.println("Comanda la această dată și oră nu există.");
            return;
        }
        int orderIndex = ordersByDateTime.get(dateTime);
        removeOrderByIndex(orderIndex);
    }

    private void removeOrderByName(String name) {
        if (!ordersByName.containsKey(name)) {
            System.out.println("Comanda cu acest nume nu există.");
            return;
        }
        int orderIndex = ordersByName.get(name);
        removeOrderByIndex(orderIndex);
    }

    private void removeOrderByIndex(int orderIndex) {
        String clientName = orders.get(orderIndex).getClientName();
        LocalDateTime orderTime = orders.get(orderIndex).getOrderTime();

        ordersByName.remove(clientName);
        ordersByDateTime.remove(orderTime);

        orders.remove(orderIndex);
        System.out.println("Comanda a fost eliminată cu succes.");
    }

    public Reservation findReservationByPersonName(String fullName){
        if(!reservationByPersonName.containsKey(fullName)){
            System.out.println("Rezervarea cu acest nume nu există.");
            return null;
        }
        return reservations.get(reservationByPersonName.get(fullName));
    }

    public Reservation findReservationByDateTime(LocalDateTime dateTime){
        if(!reservationByDateTime.containsKey(dateTime)){
            System.out.println("Rezervarea la această dată și oră nu există.");
            return null;
        }
        return reservations.get(reservationByDateTime.get(dateTime));
    }

    public ArrayList<ClientOrder> getOrders() {
        return orders;
    }

    public void setRestaurantData(RestaurantData restaurantData) {
        this.restaurantData = restaurantData;
    }

    public RestaurantData getRestaurantData() {
        return restaurantData;
    }
}
