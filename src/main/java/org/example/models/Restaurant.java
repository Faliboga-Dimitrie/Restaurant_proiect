package org.example.models;

import org.example.enums.TableStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.example.interfaces.IRestaurant;

public class Restaurant implements IRestaurant {
    private RestaurantData restaurantData;
    private Menu menu;
    private Staff staff;
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private final ArrayList<Table> tables = new ArrayList<>();
    private final ArrayList<ClientOrder> orders = new ArrayList<>();
    private final HashMap<LocalDateTime,Integer> ordersByDateTime = new HashMap<>();
    private final HashMap<String,Integer> ordersByName = new HashMap<>();
    private final HashMap<Integer,ArrayList<String>> tablesByCapacity = new HashMap<>();
    private final HashMap<Integer, Integer> tablesByNumber = new HashMap<>();
    private final HashMap<TableStatus,ArrayList<String>> tablesByStatus = new HashMap<>();
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

    public void makeReservation(ReservationData reservationData) {
        String fullName = reservationData.getFullName();
        LocalDateTime reservationDateTime = reservationData.getReservationDateTime();
        int numberOfPeople = reservationData.getNumberOfPeople();

        if (reservationByPersonName.containsKey(fullName) || reservationByDateTime.containsKey(reservationDateTime)) {
            System.out.println("The reservation could not be made. " +
                    "There is already a reservation with the same name or the same date and time.");
            return;
        }

        if (!tablesByStatus.containsKey(TableStatus.FREE) || tablesByStatus.get(TableStatus.FREE).isEmpty()) {
            System.out.println("The reservation could not be made. There are no available tables.");
            return;
        }

        for (String tableIndexStr : tablesByStatus.get(TableStatus.FREE)) {
            int tableIndex = Integer.parseInt(tableIndexStr);
            if (tables.get(tableIndex).getNumberOfSeats() >= numberOfPeople) {
                tables.get(tableIndex).reserveTable();

                Reservation newReservation = new Reservation(reservationData);
                newReservation.setTableId(tables.get(tableIndex).getTableNumber());
                reservations.add(newReservation);

                reservationByPersonName.put(fullName, reservations.size() - 1);
                reservationByDateTime.put(reservationDateTime, reservations.size() - 1);

                updateTableStatus(tableIndex);

                JsonUtil.appendToJson(newReservation, "reservations.json", Reservation.class);

                System.out.println("The reservation has been successfully made.");
                return;
            }
        }
        System.out.println("The reservation could not be made. There are no tables with the required number of seats.");
    }

    private void updateTableStatus(int tableIndex) {
        tablesByStatus.get(TableStatus.FREE).remove(String.valueOf(tableIndex));
        tablesByStatus.computeIfAbsent(TableStatus.RESERVED, k -> new ArrayList<>()).add(String.valueOf(tableIndex));
    }

    public void cancelReservation(String fullName) {
        if (!reservationByPersonName.containsKey(fullName)) {
            System.out.println("The reservation could not be canceled. A reservation with this name does not exist.");
            return;
        }

        int reservationIndex = reservationByPersonName.get(fullName);
        Reservation reservation = reservations.get(reservationIndex);

        LocalDateTime reservationDateTime = reservation.getReservationDateTime();
        int tableIndex = reservation.getTableId();

        // Actualizarea stării mesei
        Table table = findTableByNumber(tableIndex);
        table.freeTable();

        // Gestionarea statusului meselor
        if (tablesByStatus.containsKey(TableStatus.RESERVED)) {
            tablesByStatus.get(TableStatus.RESERVED).remove(String.valueOf(tableIndex));
        }
        tablesByStatus.computeIfAbsent(TableStatus.FREE, k -> new ArrayList<>()).add(String.valueOf(tableIndex));

        // Ștergerea din liste și map-uri
        reservations.remove(reservationIndex);
        reservationByPersonName.remove(fullName);
        reservationByDateTime.remove(reservationDateTime);

        // Reindexarea rezervărilor
        for (Map.Entry<String, Integer> entry : reservationByPersonName.entrySet()) {
            if (entry.getValue() > reservationIndex) {
                reservationByPersonName.put(entry.getKey(), entry.getValue() - 1);
            }
        }

        JsonUtil.removeFromJson("reservations.json", Reservation.class, r -> r.getFullName().equals(fullName));
    }

    public void addTable(int numberOfSeats,int tableNumber, boolean fromJson) {
        Table newTable = new Table(numberOfSeats,tableNumber);

        // Adăugăm în lista principală de tabele
        tables.add(newTable);
        tablesByNumber.put(newTable.getTableNumber(), tables.size() - 1);

        // Adăugăm în mapările pentru capacitate și status
        addToMapByCapacity(numberOfSeats, tables.size() - 1);
        addToMapByStatus(newTable.getStatus(), tables.size() - 1);

        // Dacă nu vine din JSON, adăugăm în fișierul JSON
        if (!fromJson) {
            JsonUtil.appendToJson(newTable, "tables.json", Table.class);
        }
    }

    private void addToMapByCapacity(int numberOfSeats, int tableIndex) {
        if (tablesByCapacity.containsKey(numberOfSeats)) {
            tablesByCapacity.get(numberOfSeats).add(String.valueOf(tableIndex));
        } else {
            ArrayList<String> newTableList = new ArrayList<>();
            newTableList.add(String.valueOf(tableIndex));
            tablesByCapacity.put(numberOfSeats, newTableList);
        }
    }

    private void addToMapByStatus(TableStatus status, int tableIndex) {
        if (tablesByStatus.containsKey(status)) {
            tablesByStatus.get(status).add(String.valueOf(tableIndex));
        } else {
            ArrayList<String> newTableList = new ArrayList<>();
            newTableList.add(String.valueOf(tableIndex));
            tablesByStatus.put(status, newTableList);
        }
    }

    public void addTable(Table table)
    {
        addTable(table.getNumberOfSeats(), table.getTableNumber(), true);
    }

    public void removeTable(int tableId) {
        if (!tablesByNumber.containsKey(tableId)) {
            System.out.println("The table could not be deleted. A table with this ID does not exist.");
            return;
        }

        int tableIndex = tablesByNumber.get(tableId);
        Table table = tables.get(tableIndex);

        // Ștergerea din JSON
        JsonUtil.removeFromJson("tables.json", Table.class, t -> t.getTableNumber() == tableId);

        // Actualizarea colecțiilor
        tablesByNumber.remove(tableId);
        if(tablesByCapacity.containsKey(table.getNumberOfSeats()))
        {
            tablesByCapacity.get(table.getNumberOfSeats()).remove(String.valueOf(tableIndex));
        }

        if (tablesByStatus.containsKey(table.getStatus())) {
            tablesByStatus.get(table.getStatus()).remove(String.valueOf(tableIndex));
        }

        tables.remove(tableIndex);

        // Reindexarea tablesByID
        for (Map.Entry<Integer, Integer> entry : tablesByNumber.entrySet()) {
            if (entry.getValue() > tableIndex) {
                tablesByNumber.put(entry.getKey(), entry.getValue() - 1);
            }
        }

        // Reindexarea tablesByCapacity
        for (Map.Entry<Integer, ArrayList<String>> entry : tablesByCapacity.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (Integer.parseInt(entry.getValue().get(i)) > tableIndex) {
                    entry.getValue().set(i, String.valueOf(Integer.parseInt(entry.getValue().get(i)) - 1));
                }
            }
        }

        // Reindexarea tablesByStatus
        for (Map.Entry<TableStatus, ArrayList<String>> entry : tablesByStatus.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (Integer.parseInt(entry.getValue().get(i)) > tableIndex) {
                    entry.getValue().set(i, String.valueOf(Integer.parseInt(entry.getValue().get(i)) - 1));
                }
            }
        }
    }

    public void updateTableStatus(int tableId, TableStatus newStatus) {
        if (!tablesByNumber.containsKey(tableId)) {
            System.out.println("The table could not be updated. A table with this ID does not exist.");
            return;
        }

        int tableIndex = tablesByNumber.get(tableId);
        Table table = tables.get(tableIndex);

        if (!table.getStatus().equals(newStatus)) {
            tablesByStatus.get(table.getStatus()).remove(String.valueOf(tableIndex));
            table.setStatus(newStatus);
            tablesByStatus.computeIfAbsent(newStatus, k -> new ArrayList<>()).add(String.valueOf(tableIndex));
        }

        saveTableToJson(tables.get(tableIndex));
    }

    private void saveTableToJson(Table table) {
        JsonUtil.updateElementInJson(
                "tables.json",
                Table.class,
                t -> t.getTableNumber() == table.getTableNumber(),
                t -> t.setStatus(table.getStatus())
        );
    }

    public Table findTableByNumber(int tableId){
        if(!tablesByNumber.containsKey(tableId)){
            System.out.println("Table not found. Can be added.");
            return null;
        }
        return tables.get(tablesByNumber.get(tableId));
    }

    public ClientOrder findOrderByClientName(String clientName){
        if(!ordersByName.containsKey(clientName)){
            System.out.println("The order with this name does not exist.");
            return null;
        }
        return orders.get(ordersByName.get(clientName));
    }

    public ClientOrder findOrderByDateTime(LocalDateTime dateTime){
        if(!ordersByDateTime.containsKey(dateTime)){
            System.out.println("The order for this date and time does not exist.");
            return null;
        }
        return orders.get(ordersByDateTime.get(dateTime));
    }

    public void addOrder(LocalDateTime dateTime, ClientOrder order, boolean fromJson) {
        orders.add(order);
        ordersByDateTime.put(dateTime,orders.size()-1);
        ordersByName.put(order.getClientName(),orders.size()-1);
        if(!fromJson)
        {
            JsonUtil.appendToJson(order, "orders.json", ClientOrder.class);
        }
    }

    public void removeOrder(Object dateTime_or_Name) {
        if (dateTime_or_Name instanceof LocalDateTime dateTime) {
            removeOrderByDateTime(dateTime);
        } else if (dateTime_or_Name instanceof String name) {
            removeOrderByName(name);
        } else {
            System.out.println("The identifier type is not valid.");
            return;
        }

        JsonUtil.removeFromJson("orders.json", ClientOrder.class, order -> {
            if (dateTime_or_Name instanceof LocalDateTime dateTime) {
                return order.getOrderTime().equals(dateTime);
            } else if (dateTime_or_Name instanceof String name) {
                return order.getClientName().equals(name);
            }
            return false;
        });
    }

    private void removeOrderByDateTime(LocalDateTime dateTime) {
        if (!ordersByDateTime.containsKey(dateTime)) {
            System.out.println("There is no order for this date and time.");
            return;
        }
        int orderIndex = ordersByDateTime.get(dateTime);
        removeOrderByIndex(orderIndex);
    }

    private void removeOrderByName(String name) {
        if (!ordersByName.containsKey(name)) {
            System.out.println("No order exists with this name.");
            return;
        }
        int orderIndex = ordersByName.get(name);
        removeOrderByIndex(orderIndex);
    }

    private void removeOrderByIndex(int orderIndex) {
        ClientOrder order = orders.get(orderIndex);
        String clientName = order.getClientName();
        LocalDateTime orderTime = order.getOrderTime();

        // Ștergerea din mapări
        ordersByName.remove(clientName);
        ordersByDateTime.remove(orderTime);

        // Eliminarea din listă
        orders.remove(orderIndex);

        // Reindexare
        reindexOrders();

        System.out.println("The order has been successfully removed.");
    }

    private void reindexOrders() {
        ordersByName.clear();
        ordersByDateTime.clear();

        for (int i = 0; i < orders.size(); i++) {
            ClientOrder order = orders.get(i);
            ordersByName.put(order.getClientName(), i);
            ordersByDateTime.put(order.getOrderTime(), i);
        }
    }

    public Reservation findReservationByPersonName(String fullName){
        if(!reservationByPersonName.containsKey(fullName)){
            System.out.println("No reservation exists with this name.");
            return null;
        }
        return reservations.get(reservationByPersonName.get(fullName));
    }

    public Reservation findReservationByDateTime(LocalDateTime dateTime){
        if(!reservationByDateTime.containsKey(dateTime)){
            System.out.println("There is no reservation for this date and time.");
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

    public ArrayList<Table> getTables() {
        return tables;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservationByPersonName.put(reservation.getFullName(), reservations.size() - 1);
        reservationByDateTime.put(reservation.getReservationDateTime(), reservations.size() - 1);
    }
}
