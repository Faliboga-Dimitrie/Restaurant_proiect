package org.example;

import org.example.models.AuthSystem;
import org.example.models.Restaurant;
import org.example.models.User;
import org.example.models.JsonUtil;
import org.example.models.AuthData;
import org.example.enums.Role;
import org.example.enums.Interogationoptions;

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
                        }
                        return false;

                    case LOGIN:
                        AuthData authDataLogin = new AuthData();
                        authOptions(interogationoptions, authDataLogin,false);
                        currentUser = authSystem.authenticate(authDataLogin.getUsername(), authDataLogin.getPassword());
                        if (currentUser != null) {
                            System.out.println("User authenticated successfully!");
                            System.out.println("Logged in as: " + currentUser.getRole());
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

//    public static void clientMainOptionsDisplay() {
//        System.out.println("Please choose an option:");
//        System.out.println("1. View menu");
//        System.out.println("2. View orders");
//        System.out.println("3. Exit");
//    }
//
//    public static void staffMainOptionsDisplay() {
//        System.out.println("Please choose an option:");
//        System.out.println("1. View menu");
//        System.out.println("2. View orders");
//        System.out.println("3. Add order");
//        System.out.println("4. Exit");
//    }
//
//    public static void adminMainOptionsDisplay() {
//        System.out.println("Please choose an option:");
//        System.out.println("1. View menu");
//        System.out.println("2. View orders");
//        System.out.println("3. Add order");
//        System.out.println("4. Add menu item");
//        System.out.println("5. Remove menu item");
//        System.out.println("6. Exit");
//    }
//
//    public static void mainConsoleIO(Role role, Restaurant restaurant) {
//        Scanner scanner = new Scanner(System.in);
//        int option = 0;
//        do {
//            switch (role) {
//                case CLIENT:
//                    System.out.println("Please choose an option:");
//                    System.out.println("1. View menu");
//                    System.out.println("2. View orders");
//                    System.out.println("3. Exit");
//                    option = asureOption();
//                    switch (option) {
//                        case 1:
//                            restaurant.getMenu().forEach((key, value) -> System.out.println(value));
//                            break;
//                        case 2:
//                            restaurant.getOrders().forEach((key, value) -> System.out.println(value));
//                            break;
//                        case 3:
//                            System.out.println("Exiting...");
//                            return;
//                        default:
//                            System.out.println("Invalid option!");
//                            break;
//                    }
//                    break;
//                case STAFF:
//                    System.out.println("Please choose an option:");
//                    System.out.println("1. View menu");
//                    System.out.println("2. View orders");
//                    System.out.println("3. Add order");
//                    System.out.println("4. Exit");
//                    option = asureOption();
//                    switch (option) {
//                        case 1:
//                            restaurant.getMenu().forEach((key, value) -> System.out.println(value));
//                            break;
//                        case 2:
//                            restaurant.getOrders().forEach((key, value) -> System.out.println(value));
//                            break;
//                        case 3:
//                            System.out.println("Enter the order:");
//                            String order = scanner.nextLine();
//                            restaurant.addOrder(order);
//                            System.out.println("Order added successfully!");
//                            break;
//                        case 4:
//                            System.out.println("Exiting...");
//                            return;
//                        default:
//                            System.out.println("Invalid option!");
//                            break;
//                    }
//                    break;
//                case ADMIN:
//                    System.out.println("Please choose an option:");
//                    System.out.println("1. View menu");
//                    System.out.println("2. View orders");
//                    System.out.println("3. Add order");
//                    System.out.println("4. Add menu item");
//                    System.out.println("5. Remove menu item");
//                    System.out.println("6. Exit");
//                    option = asureOption();
//                    switch (option) {
//                        case 1:
//                            restaurant.getMenu().forEach((key, value) -> System.out.println(value));
//                            break;
//                        case 2:
//                            restaurant.getOrders().forEach((key, value) -> System.out.println(value));
//                            break;
//                        case 3:
//                            System.out.println("Enter the order:");
//                            String order = scanner.nextLine();
//                            restaurant.addOrder(order);
//                            System.out.println("Order added successfully!");
//                            break;
//                        case 4:
//                            System.out.println("Enter the menu item:");
//                            String menuItem = scanner.nextLine();
//                            restaurant.addMenuItem(menuItem);
//                            System.out.println("Menu item added successfully!");
//                            break;
//                        case 5:
//                            System.out.println("Enter the menu item:");
//                            String menuItemToRemove = scanner.nextLine();
//                            restaurant.removeMenuItem(menuItemToRemove);
//                            System.out.println("Menu item removed successfully!");
//                            break;
//                        case 6:
//                            System.out.println("Exiting...");
//                            return;
//                        default:
//                            System.out.println("Invalid option!");
//                            break;
//                    }
//                    break;
//                default:
//                    System.out.println("Invalid role!");
//                    break;
//            }
//        }while (true);
//    }

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
    }
}