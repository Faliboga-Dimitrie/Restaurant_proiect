package org.example;

import org.example.models.AuthSystem;
import org.example.models.Restaurant;
import org.example.models.User;
import org.example.models.JsonUtil;
import org.example.models.AuthData;
import org.example.enums.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void loginStartScreen() {
        System.out.println("Welcome to the restaurant management system!");
        System.out.println("Please choose an option:");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    public static void authOptions(Interogationoptions interogationoptions, AuthData authData, boolean needRole) {
        Scanner scanner = new Scanner(System.in);

        switch (interogationoptions) {
            case REGISTER:
                System.out.println("Enter username:");
                authData.setUsername(scanner.nextLine());
                System.out.println("Enter password:");
                authData.setPassword(scanner.nextLine());
                if(needRole){
                    System.out.println("Choose a role:");
                    for (int i = 0; i < Role.values().length - 1; i++) {
                        System.out.println((i + 1) + ". " + Role.values()[i]);
                    }
                    int roleOption = scanner.nextInt();
                    scanner.nextLine();
                    authData.setRole(Role.values()[roleOption - 1]);
                }
                break;

            case LOGIN:
                System.out.println("Enter username:");
                authData.setUsername(scanner.nextLine());
                System.out.println("Enter password:");
                authData.setPassword(scanner.nextLine());
                break;
            case EXIT:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    public static void adminOptionsDisplay() {
        System.out.println("Please choose an option:");
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. View all users");
        System.out.println("4. Exit");
    }

    public static int asureOption(){
        Scanner scanner = new Scanner(System.in);
        int option;
        do{
            try {
                option = scanner.nextInt();
                scanner.nextLine();
                break;
            }
            catch (Exception e){
                System.out.println("Invalid option!");
                System.out.println("Please input a valid option:");
                scanner.nextLine();
            }
        }while(true);
        return option;
    }

    public static void adminAuthOptions(AuthSystem authSystem, ArrayList<User> users) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do{
            switch (option) {
                case 0:
                    adminOptionsDisplay();
                    option = asureOption();
                    break;
                case 1:
                    AuthData authData = new AuthData();
                    authOptions(Interogationoptions.REGISTER, authData,true);
                    authSystem.register(authData.getUsername(), authData.getPassword(), authData.getRole());
                    users.add(new User(authData.getUsername(), authData.getPassword(), authData.getRole()));
                    System.out.println("User added successfully!");
                    option = 0;
                    break;

                case 2:
                    System.out.println("Enter the username of the user you want to remove:");
                    String username = scanner.nextLine();
                    authSystem.removeUser(username);
                    System.out.println("User removed successfully!");
                    option = 0;
                    break;

                case 3:
                    authSystem.getUsers().forEach((key, value) -> System.out.println(value));
                    option = 0;
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option!");
                    option = 0;
                    break;
            }
        }while (true);
    }

    public static boolean adminUser(){
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        System.out.println("Are you and admin or other?");
        System.out.println("1. Admin");
        System.out.println("2. Other");
        option = asureOption();
        return option == 1;
    }

    public static boolean beginConsoleIO(AuthSystem authSystem) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();
        User currentUser = null;
        int option = 0;
        boolean selectedAdmin;

        loginStartScreen();
        option = asureOption();

        try {
            Interogationoptions interogationoptions = Interogationoptions.values()[option - 1];
            do {
                selectedAdmin = false;
                switch (interogationoptions) {
                    case REGISTER:
                        AuthData authDataRegister = new AuthData();
                        if(adminUser()){
                            selectedAdmin = true;
                            authOptions(interogationoptions, authDataRegister,false);
                            currentUser = authSystem.authenticate(authDataRegister.getUsername(), authDataRegister.getPassword());
                        }

                        if (currentUser != null) {
                            if (authSystem.isAdmin(currentUser.getUsername())) {
                                System.out.println("Successfully registered as ADMIN!");
                                adminAuthOptions(authSystem,users);
                            } else {
                                System.out.println("Only ADMIN can register STAFF or other ADMIN users!");
                            }
                        } else {
                            if(selectedAdmin) {
                                System.out.println("Invalid username or password!");
                                return false;
                            }
                            System.out.println("Choose a username and password!");
                            authOptions(Interogationoptions.LOGIN, authDataRegister,false);
                            authSystem.register(authDataRegister.getUsername(), authDataRegister.getPassword(), Role.CLIENT);
                            users.add(new User(authDataRegister.getUsername(), authDataRegister.getPassword(), Role.CLIENT));
                            System.out.println("User added successfully!");
                            return true;
                        }
                        return false;

                    case LOGIN:
                        AuthData authDataLogin = new AuthData();
                        authOptions(interogationoptions, authDataLogin,false);
                        currentUser = authSystem.authenticate(authDataLogin.getUsername(), authDataLogin.getPassword());
                        if (currentUser != null) {
                            System.out.println("User authenticated successfully!");
                            System.out.println("Logged in as: " + currentUser.getRole());
                            authSystem.setCurrentUser(currentUser);
                            return true;
                        } else {
                            System.out.println("Invalid username or password!");
                            System.out.println("Note: If you don't have an account, please register first!");
                            return false;
                        }
                    case EXIT:
                        System.out.println("Exiting...");
                        return true;
                    default:
                        System.out.println("Invalid option!");
                }
            } while (true);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid option!");
            return false;
        }
    }

    public static <E extends Enum<E>> void displayEnumOptions(Class<E> enumClass) {
        System.out.println("Please choose an option:");
        int index = 1;
        for (E option : enumClass.getEnumConstants()) {
            System.out.println(index + ". " + option);
            index++;
        }
    }

    public static void WaiterOptions(Restaurant restaurant){
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do {
            WaiterOptions[] waiterOptions = WaiterOptions.values();
            displayEnumOptions(WaiterOptions.class);
            option = asureOption();
            try{
                WaiterOptions waiterOption = waiterOptions[option - 1];
                switch (waiterOption){
                    case VIEW_RESERVATIONS:
                        break;
                    case VIEW_ORDERS:
                        break;
                    case DELIVER_ORDER:
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid option!");
                break;
            }
        }while (true);
    }

    public static void modifyMenuOptions(Restaurant restaurant) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do {
            ModifyMenu[] modifyMenus = ModifyMenu.values();
            displayEnumOptions(ModifyMenu.class);
            option = asureOption();
            try{
                ModifyMenu modifyMenu = modifyMenus[option - 1];
                switch (modifyMenu){
                    case ADD_ITEM:
                        System.out.println("Enter the name of the item:");
                        String name = scanner.nextLine();
                        System.out.println("Enter the price of the item:");
                        double price = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Item added successfully!");
                        break;
                    case REMOVE_ITEM:
                        System.out.println("Enter the name of the item you want to remove:");
                        String itemName = scanner.nextLine();
                        System.out.println("Item removed successfully!");
                        break;
                    case UPDATE_ITEM:
                        System.out.println("Enter the name of the item you want to update:");
                        String itemNameUpdate = scanner.nextLine();
                        System.out.println("Enter the new name of the item:");
                        String newName = scanner.nextLine();
                        System.out.println("Enter the new price of the item:");
                        double newPrice = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Item updated successfully!");
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid option!");
                break;
            }
        }while (true);
    }

    public static void ChefOptions(Restaurant restaurant){
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do {
            ChefOptions[] chefOptions = ChefOptions.values();
            displayEnumOptions(ChefOptions.class);
            option = asureOption();
            try{
                ChefOptions chefOption = chefOptions[option - 1];
                switch (chefOption){
                    case VIEW_ORDERS:
                        break;
                    case PREPARE_ORDER:
                        break;
                    case UPDATE_MENU:
                         modifyMenuOptions(restaurant);
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid option!");
                break;
            }
        }while (true);
    }

    public static void modifyStaffOptions(AuthSystem authSystem) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        ArrayList<User> users = new ArrayList<>();
        do {
            ModifyStaff[] modifyStaffs = ModifyStaff.values();
            displayEnumOptions(ModifyStaff.class);
            option = asureOption();
            try{
                ModifyStaff modifyStaff = modifyStaffs[option - 1];
                switch (modifyStaff){
                    case ADD_STAFF:
                        AuthData authData = new AuthData();
                        authOptions(Interogationoptions.REGISTER, authData,true);
                        authSystem.register(authData.getUsername(), authData.getPassword(), authData.getRole());
                        users.add(new User(authData.getUsername(), authData.getPassword(), authData.getRole()));
                        JsonUtil.appendToJson(users, "users.json", User.class);
                        System.out.println("Staff added successfully!");
                        break;
                    case REMOVE_STAFF:
                        System.out.println("Enter the username of the staff you want to remove:");
                        String username = scanner.nextLine();
                        authSystem.removeUser(username);
                        JsonUtil.removeFromJson("users.json", User.class, item -> item.getUsername().equals(username));
                        System.out.println("Staff removed successfully!");
                        break;
                    case UPDATE_STAFF:
                        System.out.println("Enter the username of the staff you want to update:");
                        String usernameUpdate = scanner.nextLine();
                        System.out.println("Enter the new username of the staff:");
                        String newUsername = scanner.nextLine();
                        System.out.println("Enter the new password of the staff:");
                        String newPassword = scanner.nextLine();
                        System.out.println("Enter the new role of the staff:");
                        for (int i = 0; i < Role.values().length - 1; i++) {
                            System.out.println((i + 1) + ". " + Role.values()[i]);
                        }
                        int roleOption = scanner.nextInt();
                        scanner.nextLine();
                        authSystem.updateUsername(usernameUpdate, newUsername);
                        authSystem.updatePassword(newUsername, newPassword);
                        authSystem.updateRole(newUsername, Role.values()[roleOption - 1]);
                        JsonUtil.updateElementInJson("users.json", User.class, item -> item.getUsername().equals(usernameUpdate), item -> {
                            item.setUsername(newUsername);
                            item.setPassword(newPassword);
                            item.setRole(Role.values()[roleOption - 1]);
                        });
                        System.out.println("Staff updated successfully!");
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid option!");
                break;
            }
        }while (true);
    }

    public static void modifyStaffOptions(Restaurant restaurant){
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do {
            ModifyStaff[] modifyStaffs = ModifyStaff.values();
            displayEnumOptions(ModifyStaff.class);
            option = asureOption();
            try{
                ModifyStaff modifyStaff = modifyStaffs[option - 1];
                switch (modifyStaff){
                    case ADD_STAFF:
                        System.out.println("Enter the username of the staff:");
                        String username = scanner.nextLine();
                        System.out.println("Staff added successfully!");
                        break;
                    case REMOVE_STAFF:
                        System.out.println("Enter the username of the staff you want to remove:");
                        String staffUsername = scanner.nextLine();
                        System.out.println("Staff removed successfully!");
                        break;
                    case UPDATE_STAFF:
                        System.out.println("Enter the username of the staff you want to update:");
                        String staffUsernameUpdate = scanner.nextLine();
                        System.out.println("Enter the new username of the staff:");
                        String newStaffUsername = scanner.nextLine();
                        System.out.println("Staff updated successfully!");
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid option!");
                break;
            }
        }while (true);
    }

    public static void modifyRestaurantOptions(Restaurant restaurant) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do {
            ModifyRestaurant[] modifyRestaurants = ModifyRestaurant.values();
            displayEnumOptions(ModifyRestaurant.class);
            option = asureOption();
            try{
                ModifyRestaurant modifyRestaurant = modifyRestaurants[option - 1];
                switch (modifyRestaurant){
                    case ADD_TABLE:
                        System.out.println("Enter the number of the table:");
                        int number = scanner.nextInt();
                        scanner.nextLine();
                        restaurant.addTable(number, "liber");
                        System.out.println("Table added successfully!");
                        break;
                    case REMOVE_TABLE:
                        System.out.println("Enter the number of the table you want to remove:");
                        int tableNumber = scanner.nextInt();
                        scanner.nextLine();
                        restaurant.removeTable(tableNumber);
                        System.out.println("Table removed successfully!");
                        break;
                    case UPDATE_TABLE:
                        System.out.println("Enter the number of the table you want to update:");
                        int tableNumberUpdate = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter the new number of seats to the table:");
                        int newSeatsNumber = scanner.nextInt();
                        scanner.nextLine();
                        restaurant.updateTable(tableNumberUpdate, newSeatsNumber, "liber");
                        System.out.println("Table updated successfully!");
                        break;
                    case ADD_STAFF:
                        System.out.println("Enter the username of the staff:");
                        String username = scanner.nextLine();
                        System.out.println("Staff added successfully!");
                        break;
                    case REMOVE_STAFF:
                        System.out.println("Enter the username of the staff you want to remove:");
                        String staffUsername = scanner.nextLine();
                        System.out.println("Staff removed successfully!");
                        break;
                    case UPDATE_STAFF:
                        System.out.println("Enter the username of the staff you want to update:");
                        String staffUsernameUpdate = scanner.nextLine();
                        System.out.println("Enter the new username of the staff:");
                        String newStaffUsername = scanner.nextLine();
                        System.out.println("Staff updated successfully!");
                        break;
                    case ADD_MENU_ITEM:
                        System.out.println("Enter the name of the item:");
                        String name = scanner.nextLine();
                        System.out.println("Enter the price of the item:");
                        double price = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Menu item added successfully!");
                        break;
                    case REMOVE_MENU:
                        System.out.println("Enter the name of the item you want to remove:");
                        String itemName = scanner.nextLine();
                        System.out.println("Menu item removed successfully!");
                        break;
                    case UPDATE_MENU:
                        System.out.println("Enter the name of the item you want to update:");
                        String itemNameUpdate = scanner.nextLine();
                        System.out.println("Enter the new name of the item:");
                        String newName = scanner.nextLine();
                        System.out.println("Enter the new price of the item:");
                        double newPrice = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Menu item updated successfully!");
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid option!");
                break;
            }
        }while (true);
    }

    public static void mainConsoleIO(Role role, Restaurant restaurant, AuthSystem authSystem) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do {
            switch (role) {
                case CLIENT:
                    displayEnumOptions(ClientOptions.class);
                    option = asureOption();
                    try{
                        ClientOptions clientOptions = ClientOptions.values()[option - 1];
                        switch (clientOptions){
                            case MAKE_RESERVATION:
                                break;
                            case UPDATE_RESERVATION:
                                break;
                            case CANCEL_RESERVATION:
                                break;
                            case VIEW_MENU:
                                restaurant.getMenu().displayMenu();
                                break;
                            case MAKE_ORDER:
                                break;
                            case VIEW_ORDERS:
                                break;
                            case CANCEL_ORDER:
                                break;
                            case EXIT:
                                System.out.println("Exiting...");
                                return;
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Invalid option!");
                        break;
                    }

                    break;
                case STAFF:
                    displayEnumOptions(StaffOptions.class);
                    option = asureOption();
                    try{
                        StaffOptions staffOptions = StaffOptions.values()[option - 1];
                        switch (staffOptions){
                            case WAITER:
                                WaiterOptions(restaurant);
                                break;
                            case CHEF:
                                ChefOptions(restaurant);
                                break;
                            case EXIT:
                                System.out.println("Exiting...");
                                return;
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Invalid option!");
                        break;
                    }
                    break;
                case ADMIN:
                    displayEnumOptions(AdminOptions.class);
                    option = asureOption();
                    try{
                        AdminOptions adminOptions = AdminOptions.values()[option - 1];
                        switch (adminOptions){
                            case MODIFY_MENU:
                                modifyMenuOptions(restaurant);
                                break;
                            case MODIFY_STAFF:
                                modifyStaffOptions(authSystem);
                                modifyStaffOptions(restaurant);
                                break;
                            case MODIFY_RESTAURANT:
                                modifyRestaurantOptions(restaurant);
                                break;
                            case VIEW_ALL_RESERVATIONS:
                                restaurant.getReservations().forEach((item) -> System.out.println(item));
                                break;
                            case VIEW_ALL_ORDERS:
                                break;
                            case EXIT:
                                System.out.println("Exiting...");
                                return;
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Invalid option!");
                        break;
                    }
                    break;
                default:
                    System.out.println("Invalid role!");
                    break;
            }
        }while (true);
    }

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        AuthSystem authSystem = new AuthSystem();
       List<User> users = JsonUtil.loadFromJson("users.json", User.class);
        if (users != null) {
            users.forEach(user -> authSystem.addUser(user));
        }
        while (!beginConsoleIO(authSystem)) {
           beginConsoleIO(authSystem);
        }

        if (authSystem.getCurrentUser() != null) {
            mainConsoleIO(authSystem.getCurrentUser().getRole(), restaurant, authSystem);
        }
    }
}