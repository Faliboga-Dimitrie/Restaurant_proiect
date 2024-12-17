package org.example;

import org.example.models.*;
import org.example.enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

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

    public static int assureIntOption(){
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

    public static boolean assureBooleanOption(){
        Scanner scanner = new Scanner(System.in);
        boolean option;
        do{
            try {
                option = scanner.nextBoolean();
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

    public static double assureDoubleOption(){
        Scanner scanner = new Scanner(System.in);
        double option;
        do{
            try {
                option = scanner.nextDouble();
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
                    option = assureIntOption();
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
        int option;
        System.out.println("Are you and admin or other?");
        System.out.println("1. Admin");
        System.out.println("2. Other");
        option = assureIntOption();
        return option == 1;
    }

    public static boolean beginConsoleIO(AuthSystem authSystem) {
        ArrayList<User> users = new ArrayList<>();
        User currentUser = null;
        int option;
        boolean selectedAdmin;

        loginStartScreen();
        option = assureIntOption();

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

    public static HashMap<String,Ingredient> setIngredientList(){
        Scanner scanner = new Scanner(System.in);
        HashMap<String,Ingredient> ingredients = new HashMap<>();
        int option;
        do {
            System.out.println("Please choose an option:");
            System.out.println("1. Add ingredient");
            System.out.println("2. Exit");
            option = assureIntOption();
            switch (option) {
                case 1:
                    Ingredient ingredient = new Ingredient();
                    System.out.println("Enter the name of the ingredient:");
                    ingredient.setName(scanner.nextLine());
                    System.out.println("Enter the quantity of the ingredient:");
                    ingredient.setQuantity(assureIntOption());
                    System.out.println("Enter the unit of the ingredient:");
                    ingredient.setUnit(scanner.nextLine());
                    System.out.println("Is the ingredient vegetarian?");
                    ingredient.setVegetarian(assureBooleanOption());
                    System.out.println("Is the ingredient an allergen?");
                    ingredient.setAllergen(assureBooleanOption());
                    ingredients.put(ingredient.getName(), ingredient);
                    break;
                case 2:
                    System.out.println("Exiting...");
                    return ingredients;
                default:
                    System.out.println("Invalid option!");
                    break;
            }

        }while(true);
    }

    public static MenuItem populateMenuItemSubClass(){
        Scanner scanner = new Scanner(System.in);
        HashMap<String,Ingredient> ingredients = setIngredientList();
        System.out.println("Enter the name of the item:");
        String name = scanner.nextLine();
        System.out.println("Enter the price of the item:");
        double price = assureDoubleOption();
        System.out.println("Enter the description of the item:");
        String description = scanner.nextLine();
        System.out.println("Enter the calories of the item:");
        int calories = assureIntOption();
        System.out.println("Is the item available (true/false):");
        boolean isAvailable = assureBooleanOption();
        return new MenuItem(ingredients, name, description, price, calories, isAvailable);
    }

    public static Person populatePersonSubClass(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the person:");
        String name = scanner.nextLine();
        System.out.println("Enter the surname of the person:");
        String surname = scanner.nextLine();
        System.out.println("Enter the age of the person:");
        int age = assureIntOption();
        System.out.println("Enter the phone number of the person:");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter the date of birth of the person:");
        System.out.println("Year:");
        int year = assureIntOption();
        System.out.println("Month:");
        int month = assureIntOption();
        System.out.println("Day:");
        int day = assureIntOption();
        return new Person(name, surname, age, phoneNumber, LocalDate.of(year, month, day));
    }

    public static void modifyMenuOptions(Restaurant restaurant) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            ModifyMenu[] modifyMenus = ModifyMenu.values();
            displayEnumOptions(ModifyMenu.class);
            option = assureIntOption();
            try{
                ModifyMenu modifyMenu = modifyMenus[option - 1];
                switch (modifyMenu){
                    case ADD_ITEM:
                        MenuItem menuItem = populateMenuItemSubClass();
                        MenuItemType[] menuItemTypes = MenuItemType.values();
                        displayEnumOptions(MenuItemType.class);
                        int menuItemTypeOption = assureIntOption();
                        MenuItemType menuItemType = menuItemTypes[menuItemTypeOption - 1];
                        switch (menuItemType){
                            case DRINK:
                                System.out.println("Enter the quantity of the drink:");
                                int quantity = assureIntOption();
                                System.out.println("Is the drink alcoholic?");
                                boolean isAlcoholic = assureBooleanOption();
                                restaurant.getMenu().addDrink(new Drink(menuItem, isAlcoholic, quantity));
                                break;
                            case FOOD:
                                System.out.println("Enter the cuisine of the food:");
                                String cuisine = scanner.nextLine();
                                System.out.println("Is the food an appetizer?");
                                boolean isAppetizer = assureBooleanOption();
                                System.out.println("Is the food a main course?");
                                boolean isMainCourse = assureBooleanOption();
                                System.out.println("Is the food a dessert?");
                                boolean isDessert = assureBooleanOption();
                                System.out.println("Is the food a side dish?");
                                boolean isSideDish = assureBooleanOption();
                                restaurant.getMenu().addFood(new Food(menuItem, cuisine, isAppetizer, isMainCourse, isDessert, isSideDish));
                                break;
                            case SPECIAL:
                                System.out.println("Is the special a chef special?");
                                boolean isChefSpecial = assureBooleanOption();
                                System.out.println("Is the special an event special?");
                                boolean isEventSpecial = assureBooleanOption();
                                restaurant.getMenu().addSpecial(new Special(menuItem, isChefSpecial, isEventSpecial));
                                break;
                        }

                        break;
                    case REMOVE_ITEM:
                        System.out.println("Enter the name of the item you want to remove:");
                        String itemName = scanner.nextLine();
                        MenuItemType[] menuItemTypesRemove = MenuItemType.values();
                        displayEnumOptions(MenuItemType.class);
                        int menuItemTypeOptionRemove = assureIntOption();
                        MenuItemType menuItemTypeRemove = menuItemTypesRemove[menuItemTypeOptionRemove - 1];
                        switch (menuItemTypeRemove){
                            case DRINK:
                                restaurant.getMenu().removeDrink(restaurant.getMenu().findDrinkByName(itemName));
                                break;
                            case FOOD:
                                restaurant.getMenu().removeFood(restaurant.getMenu().findFoodByName(itemName));
                                break;
                            case SPECIAL:
                                restaurant.getMenu().removeSpecial(restaurant.getMenu().findSpecialByName(itemName));
                                break;
                        }
                        break;
                    case UPDATE_ITEM:
                        System.out.println("Enter the name of the item you want to update:");
                        String itemNameUpdate = scanner.nextLine();
                        MenuItemType[] menuItemTypesUpdate = MenuItemType.values();
                        displayEnumOptions(MenuItemType.class);
                        int menuItemTypeOptionUpdate = assureIntOption();
                        MenuItemType menuItemTypeUpdate = menuItemTypesUpdate[menuItemTypeOptionUpdate - 1];
                        MenuUpdateType[] menuUpdateTypes = MenuUpdateType.values();
                        displayEnumOptions(MenuUpdateType.class);
                        int menuUpdateTypeOption = assureIntOption();
                        MenuUpdateType menuUpdateType = menuUpdateTypes[menuUpdateTypeOption - 1];


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
        int option;
        ArrayList<User> users = new ArrayList<>();
        do {
            ModifyStaff[] modifyStaffs = ModifyStaff.values();
            displayEnumOptions(ModifyStaff.class);
            option = assureIntOption();
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
        int option;
        do {
            ModifyStaff[] modifyStaffs = ModifyStaff.values();
            displayEnumOptions(ModifyStaff.class);
            option = assureIntOption();
            try{
                ModifyStaff modifyStaff = modifyStaffs[option - 1];
                switch (modifyStaff){
                    case ADD_STAFF:
                        Person person = populatePersonSubClass();
                        System.out.println("Enter the salary of the staff:");
                        double salary = assureDoubleOption();
                        System.out.println("Enter the hire date of the staff:");
                        System.out.println("Year:");
                        int year = assureIntOption();
                        System.out.println("Month:");
                        int month = assureIntOption();
                        System.out.println("Day:");
                        int day = assureIntOption();
                        LocalDate hireDate = LocalDate.of(year, month, day);
                        restaurant.getStaff().addEmployee(new Employee(person, salary, hireDate));
                        break;
                    case REMOVE_STAFF:
                        System.out.println("Enter the name of the staff you want to update:");
                        String staffName = scanner.nextLine();
                        System.out.println("Enter the surname of the staff you want to update:");
                        String staffSurname = scanner.nextLine();
                        restaurant.getStaff().removeEmployee(staffName + staffSurname);
                        System.out.println("Staff removed successfully!");
                        break;
                    case UPDATE_STAFF:
                        System.out.println("Enter the name of the staff you want to update:");
                        String staffNameUpdate = scanner.nextLine();
                        System.out.println("Enter the surname of the staff you want to update:");
                        String staffSurnameUpdate = scanner.nextLine();
                        displayEnumOptions(EmployeeUpdateType.class);
                        int employeeUpdateTypeOption = assureIntOption();
                        EmployeeUpdateType employeeUpdateType = EmployeeUpdateType.values()[employeeUpdateTypeOption - 1];
                        switch (employeeUpdateType) {
                            case NAME:
                                System.out.println("Enter the new name of the staff:");
                                String newName = scanner.nextLine();
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newName, EmployeeUpdateType.NAME);
                                break;
                            case SURNAME:
                                System.out.println("Enter the new surname of the staff:");
                                String newSurname = scanner.nextLine();
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newSurname, EmployeeUpdateType.SURNAME);
                                break;
                            case AGE:
                                System.out.println("Enter the new age of the staff:");
                                int newAge = assureIntOption();
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newAge, EmployeeUpdateType.AGE);
                                break;
                            case PHONE_NUMBER:
                                System.out.println("Enter the new phone number of the staff:");
                                String newPhoneNumber = scanner.nextLine();
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newPhoneNumber, EmployeeUpdateType.PHONE_NUMBER);
                                break;
                            case DATE_OF_BIRTH:
                                System.out.println("Enter the new date of birth of the staff:");
                                System.out.println("Year:");
                                int newYear = assureIntOption();
                                System.out.println("Month:");
                                int newMonth = assureIntOption();
                                System.out.println("Day:");
                                int newDay = assureIntOption();
                                LocalDate newDateOfBirth = LocalDate.of(newYear, newMonth, newDay);
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newDateOfBirth, EmployeeUpdateType.DATE_OF_BIRTH);
                                break;
                            case SALARY:
                                System.out.println("Enter the new salary of the staff:");
                                double newSalary = assureDoubleOption();
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newSalary, EmployeeUpdateType.SALARY);
                                break;
                            case HIRED_DATE:
                                System.out.println("Enter the new hire date of the staff:");
                                System.out.println("Year:");
                                int newHireYear = assureIntOption();
                                System.out.println("Month:");
                                int newHireMonth = assureIntOption();
                                System.out.println("Day:");
                                int newHireDay = assureIntOption();
                                LocalDate newHireDate = LocalDate.of(newHireYear, newHireMonth, newHireDay);
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newHireDate, EmployeeUpdateType.HIRED_DATE);
                                break;
                        }
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

    public static void modifyTableOptions(Restaurant restaurant){
        int option;
        do {
            ModifyTable[] modifyTables = ModifyTable.values();
            displayEnumOptions(ModifyTable.class);
            option = assureIntOption();
            try{
                ModifyTable modifyTable = modifyTables[option - 1];
                switch (modifyTable){
                    case ADD_TABLE:
                        System.out.println("Enter the number of seats of the table:");
                        int seats = assureIntOption();
                        restaurant.addTable(seats);
                        break;
                    case REMOVE_TABLE:
                        System.out.println("Enter the number (id) of the table you want to remove:");
                        int tableNumber = assureIntOption();
                        restaurant.removeTable(tableNumber);
                        break;
                    case UPDATE_TABLE:
                        System.out.println("Enter the number (id) of the table you want to update:");
                        int tableNumberUpdate = assureIntOption();
                        System.out.println("Enter the new number of seats of the table:");
                        int newSeats = assureIntOption();
                        displayEnumOptions(TableStatus.class);
                        int tableStatusOption = assureIntOption();
                        TableStatus tableStatus = TableStatus.values()[tableStatusOption - 1];
                        restaurant.updateTable(tableNumberUpdate, newSeats, tableStatus);
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

    public static void modifyRestaurantOptions(Restaurant restaurant, AuthSystem authSystem) {
        int option;
        do {
            ModifyRestaurant[] modifyRestaurants = ModifyRestaurant.values();
            displayEnumOptions(ModifyRestaurant.class);
            option = assureIntOption();
            try{
                ModifyRestaurant modifyRestaurant = modifyRestaurants[option - 1];
                switch (modifyRestaurant){
                    case MODIFY_STAFF:
                        System.out.println("First the data from restaurant about the staff will be modified");
                        modifyStaffOptions(restaurant);
                        System.out.println("Now the data from the authentification system will be modified");
                        modifyStaffOptions(authSystem);
                        break;
                    case MODIFY_TABLES:
                        modifyTableOptions(restaurant);
                        break;
                    case MODIFY_MENU:
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

    public static void mainConsoleIO(Role role, Restaurant restaurant, AuthSystem authSystem) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            switch (role) {
                case CLIENT:
                    displayEnumOptions(ClientOptions.class);
                    option = assureIntOption();
                    try{
                        ClientOptions clientOptions = ClientOptions.values()[option - 1];
                        switch (clientOptions){
                            case MAKE_RESERVATION:
                                System.out.println("Enter your full name:");
                                String fullName = scanner.nextLine();
                                System.out.println("Enter your phone number:");
                                String phoneNumber = scanner.nextLine();
                                System.out.println("Enter the date and time of the reservation:");
                                System.out.println("Year:");
                                int year = assureIntOption();
                                System.out.println("Month:");
                                int month = assureIntOption();
                                System.out.println("Day:");
                                int day = assureIntOption();
                                System.out.println("Hour:");
                                int hour = assureIntOption();
                                System.out.println("Minute:");
                                int minute = assureIntOption();
                                System.out.println("Enter the number of people:");
                                int numberOfPeople = assureIntOption();
                                LocalDateTime reservationDateTime = LocalDateTime.of(year, month, day, hour, minute);
                                restaurant.getReservations().add(new Reservation(fullName, phoneNumber, reservationDateTime, numberOfPeople));
                                break;
                            case UPDATE_RESERVATION:
                                System.out.println("To be implemented...");
                                break;
                            case CANCEL_RESERVATION:
                                System.out.println("To be implemented... 1");
                                break;
                            case VIEW_MENU:
                                restaurant.getMenu().displayMenu();
                                break;
                            case MAKE_ORDER:
                                System.out.println("To be implemented... 2");
                                break;
                            case VIEW_ORDERS:
                                System.out.println("To be implemented... 3");
                                break;
                            case CANCEL_ORDER:
                                System.out.println("To be implemented... 4");
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
                    option = assureIntOption();
                    try{
                        StaffOptions staffOptions = StaffOptions.values()[option - 1];
                        switch (staffOptions){
                            case VIEW_RESERVATIONS:
                                break;
                            case VIEW_ORDERS:
                                System.out.println("To be implemented... 5");
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
                    option = assureIntOption();
                    try{
                        AdminOptions adminOptions = AdminOptions.values()[option - 1];
                        switch (adminOptions){
                            case MODIFY_RESTAURANT:
                                modifyRestaurantOptions(restaurant, authSystem);
                                break;
                            case VIEW_ALL_RESERVATIONS:
                                restaurant.getReservations().forEach(System.out::println);
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
            users.forEach(authSystem::addUser);
        }
        while (!beginConsoleIO(authSystem)) {
           beginConsoleIO(authSystem);
        }

        if (authSystem.getCurrentUser() != null) {
            mainConsoleIO(authSystem.getCurrentUser().getRole(), restaurant, authSystem);
        }
    }
}