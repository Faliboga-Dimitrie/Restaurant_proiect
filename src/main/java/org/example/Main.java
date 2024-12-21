package org.example;

import org.example.models.*;
import org.example.enums.*;

import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class Main {
    public static <E extends Enum<E>> void displayEnumOptions(Class<E> enumClass) {
        System.out.println("Please choose an option:");
        int index = 1;
        for (E option : enumClass.getEnumConstants()) {
            System.out.println(index + ". " + option);
            index++;
        }
    }

    public static <T extends Number> T safeNumericInput(Class<T> type) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Please input a value of type " + type.getSimpleName() + ":");
                String input = scanner.nextLine();

                if (type == Integer.class) {
                    return type.cast(Integer.parseInt(input));
                } else if (type == Double.class) {
                    return type.cast(Double.parseDouble(input));
                } else if (type == Float.class) {
                    return type.cast(Float.parseFloat(input));
                } else if (type == Long.class) {
                    return type.cast(Long.parseLong(input));
                } else if (type == Short.class) {
                    return type.cast(Short.parseShort(input));
                } else if (type == Byte.class) {
                    return type.cast(Byte.parseByte(input));
                } else {
                    throw new IllegalArgumentException("Unsupported numeric type: " + type.getSimpleName());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    public static boolean safeBooleanInput(){
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
                    displayEnumOptions(Role.class);
                    int roleOption = safeNumericInput(Integer.class);
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

    public static void adminAuthOptions(AuthSystem authSystem) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do{
            displayEnumOptions(AuthAdminOptions.class);
            option = safeNumericInput(Integer.class);
            try{
                AuthAdminOptions authAdminOptions = AuthAdminOptions.values()[option - 1];
                switch (authAdminOptions) {
                    case ADD_USER:
                        AuthData authData = new AuthData();
                        authOptions(Interogationoptions.REGISTER, authData,true);
                        authSystem.register(authData.getUsername(), authData.getPassword(), authData.getRole());
                        System.out.println("User added successfully!");
                        break;

                    case REMOVE_USER:
                        System.out.println("Enter the username of the user you want to remove:");
                        String username = scanner.nextLine();
                        authSystem.removeUser(username);
                        System.out.println("User removed successfully!");
                        break;

                    case VIEW_USERS:
                        authSystem.getUsers().forEach((key, value) -> System.out.println(value));
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid option!");
            }
        }while (true);
    }

    public static boolean adminUser(){
        int option;
        System.out.println("Are you and admin or other?");
        System.out.println("1. Admin");
        System.out.println("2. Other");
        option = safeNumericInput(Integer.class);
        return option == 1;
    }

    public static boolean beginConsoleIO(AuthSystem authSystem) {
        User currentUser = null;
        int option;
        boolean selectedAdmin;

        loginStartScreen();
        option = safeNumericInput(Integer.class);

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
                                adminAuthOptions(authSystem);
                            } else {
                                System.out.println("Only ADMIN can register STAFF or other ADMIN users!");
                                return false;
                            }
                        } else {
                            if(selectedAdmin) {
                                System.out.println("Invalid username or password!");
                                return false;
                            }
                            System.out.println("Choose a username and password!");
                            authOptions(Interogationoptions.LOGIN, authDataRegister,false);
                            authSystem.register(authDataRegister.getUsername(), authDataRegister.getPassword(), Role.CLIENT);
                            System.out.println("User added successfully!");
                            return false;
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

    public static LocalDate createLocalDate() {
        System.out.println("Enter the date:");
        System.out.println("Year:");
        int year = safeNumericInput(Integer.class);
        System.out.println("Month:");
        int month = safeNumericInput(Integer.class);
        System.out.println("Day:");
        int day = safeNumericInput(Integer.class);
        return LocalDate.of(year, month, day);
    }

    public static LocalDateTime createDateTime() {
        LocalDate localDate = createLocalDate();
        System.out.println("Hour:");
        int hour = safeNumericInput(Integer.class);
        System.out.println("Minute:");
        int minute = safeNumericInput(Integer.class);
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), hour, minute);
    }

    public static HashMap<String,Ingredient> setIngredientList(){
        Scanner scanner = new Scanner(System.in);
        HashMap<String,Ingredient> ingredients = new HashMap<>();
        int option;
        do {
            System.out.println("Please choose an option:");
            System.out.println("1. Add ingredient");
            System.out.println("2. Exit");
            option = safeNumericInput(Integer.class);
            switch (option) {
                case 1:
                    Ingredient ingredient = new Ingredient();
                    System.out.println("Enter the name of the ingredient:");
                    ingredient.setName(scanner.nextLine());
                    System.out.println("Enter the quantity of the ingredient:");
                    ingredient.setQuantity(safeNumericInput(Integer.class));
                    System.out.println("Enter the unit of the ingredient:");
                    ingredient.setUnit(scanner.nextLine());
                    System.out.println("Is the ingredient vegetarian?");
                    ingredient.setVegetarian(safeBooleanInput());
                    System.out.println("Is the ingredient an allergen?");
                    ingredient.setAllergen(safeBooleanInput());
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
        double price = safeNumericInput(Double.class);
        System.out.println("Enter the description of the item:");
        String description = scanner.nextLine();
        System.out.println("Enter the calories of the item:");
        int calories = safeNumericInput(Integer.class);
        System.out.println("Is the item available (true/false):");
        boolean isAvailable = safeBooleanInput();
        return new MenuItem(ingredients, name, description, price, calories, isAvailable);
    }

    public static Person populatePersonSubClass(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the person:");
        String name = scanner.nextLine();
        System.out.println("Enter the surname of the person:");
        String surname = scanner.nextLine();
        System.out.println("Enter the age of the person:");
        int age = safeNumericInput(Integer.class);
        System.out.println("Enter the phone number of the person:");
        String phoneNumber = scanner.nextLine();
        LocalDate dateTime = createLocalDate();
        return new Person(name, surname, age, phoneNumber, dateTime);
    }

    public static ReservationData createReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your full name:");
        String fullName = scanner.nextLine();
        System.out.println("Enter your phone number:");
        String phoneNumber = scanner.nextLine();
        LocalDateTime reservationDateTime = createDateTime();
        System.out.println("Enter the number of people:");
        int numberOfPeople = safeNumericInput(Integer.class);

        return new ReservationData(fullName, phoneNumber, reservationDateTime, numberOfPeople);
    }

    public static void modifyMenuOptions(Restaurant restaurant) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            ModifyMenu[] modifyMenus = ModifyMenu.values();
            displayEnumOptions(ModifyMenu.class);
            option = safeNumericInput(Integer.class);
            try{
                ModifyMenu modifyMenu = modifyMenus[option - 1];
                switch (modifyMenu){
                    case ADD_ITEM:
                        MenuItem menuItem = populateMenuItemSubClass();
                        MenuItemType[] menuItemTypes = MenuItemType.values();
                        displayEnumOptions(MenuItemType.class);
                        int menuItemTypeOption = safeNumericInput(Integer.class);
                        MenuItemType menuItemType = menuItemTypes[menuItemTypeOption - 1];
                        switch (menuItemType){
                            case DRINK:
                                System.out.println("Enter the quantity of the drink:");
                                int quantity = safeNumericInput(Integer.class);
                                System.out.println("Is the drink alcoholic?");
                                boolean isAlcoholic = safeBooleanInput();
                                restaurant.getMenu().addDrink(new Drink(menuItem, isAlcoholic, quantity),false);
                                break;
                            case FOOD:
                                System.out.println("Enter the cuisine of the food:");
                                String cuisine = scanner.nextLine();
                                System.out.println("Is the food a main course?");
                                boolean isMainCourse = safeBooleanInput();
                                System.out.println("Is the food a dessert?");
                                boolean isDessert = safeBooleanInput();
                                restaurant.getMenu().addFood(new Food(menuItem, cuisine, isMainCourse, isDessert),false);
                                break;
                        }

                        break;
                    case REMOVE_ITEM:
                        System.out.println("Enter the name of the item you want to remove:");
                        String itemName = scanner.nextLine();
                        MenuItemType[] menuItemTypesRemove = MenuItemType.values();
                        displayEnumOptions(MenuItemType.class);
                        int menuItemTypeOptionRemove = safeNumericInput(Integer.class);
                        MenuItemType menuItemTypeRemove = menuItemTypesRemove[menuItemTypeOptionRemove - 1];
                        switch (menuItemTypeRemove){
                            case DRINK:
                                restaurant.getMenu().removeDrink(restaurant.getMenu().findDrinkByName(itemName));
                                break;
                            case FOOD:
                                restaurant.getMenu().removeFood(restaurant.getMenu().findFoodByName(itemName));
                                break;
                        }
                        break;
                    case UPDATE_ITEM:
                        System.out.println("Enter the name of the item you want to update:");
                        String itemNameUpdate = scanner.nextLine();
                        MenuItemType[] menuItemTypesUpdate = MenuItemType.values();
                        displayEnumOptions(MenuItemType.class);
                        int menuItemTypeOptionUpdate = safeNumericInput(Integer.class);
                        MenuItemType menuItemTypeUpdate = menuItemTypesUpdate[menuItemTypeOptionUpdate - 1];
                        MenuUpdateType[] menuUpdateTypes = MenuUpdateType.values();
                        displayEnumOptions(MenuUpdateType.class);
                        int menuUpdateTypeOption = safeNumericInput(Integer.class);
                        MenuUpdateType menuUpdateType = menuUpdateTypes[menuUpdateTypeOption - 1];
                        restaurant.getMenu().updateMenuItem(itemNameUpdate, menuUpdateType, menuItemTypeUpdate, menuItemTypeUpdate);
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
        do {
            ModifyStaff[] modifyStaffs = ModifyStaff.values();
            displayEnumOptions(ModifyStaff.class);
            option = safeNumericInput(Integer.class);
            try{
                ModifyStaff modifyStaff = modifyStaffs[option - 1];
                switch (modifyStaff){
                    case ADD_STAFF:
                        AuthData authData = new AuthData();
                        authOptions(Interogationoptions.REGISTER, authData,true);
                        authSystem.register(authData.getUsername(), authData.getPassword(), authData.getRole());
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
                        displayEnumOptions(Role.class);
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
            option = safeNumericInput(Integer.class);
            try{
                ModifyStaff modifyStaff = modifyStaffs[option - 1];
                switch (modifyStaff){
                    case ADD_STAFF:
                        Person person = populatePersonSubClass();
                        System.out.println("Enter the salary of the staff:");
                        double salary = safeNumericInput(Double.class);
                        System.out.println("Enter the hire date of the staff:");
                        LocalDate hireDate = createLocalDate();
                        restaurant.getStaff().addEmployee(new Employee(person, salary,hireDate),false);
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
                        int employeeUpdateTypeOption = safeNumericInput(Integer.class);
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
                                int newAge = safeNumericInput(Integer.class);
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newAge, EmployeeUpdateType.AGE);
                                break;
                            case PHONE_NUMBER:
                                System.out.println("Enter the new phone number of the staff:");
                                String newPhoneNumber = scanner.nextLine();
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newPhoneNumber, EmployeeUpdateType.PHONE_NUMBER);
                                break;
                            case DATE_OF_BIRTH:
                                LocalDate newDateOfBirth = createLocalDate();
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newDateOfBirth, EmployeeUpdateType.DATE_OF_BIRTH);
                                break;
                            case SALARY:
                                System.out.println("Enter the new salary of the staff:");
                                double newSalary = safeNumericInput(Double.class);
                                restaurant.getStaff().updateEmployee(staffNameUpdate, staffSurnameUpdate, newSalary, EmployeeUpdateType.SALARY);
                                break;
                            case HIRED_DATE:
                                LocalDate newHireDate = createLocalDate();
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
            option = safeNumericInput(Integer.class);
            try{
                ModifyTable modifyTable = modifyTables[option - 1];
                switch (modifyTable){
                    case ADD_TABLE:
                        System.out.println("Enter the number of seats of the table:");
                        int seats = safeNumericInput(Integer.class);
                        restaurant.addTable(seats, false);
                        break;
                    case REMOVE_TABLE:
                        System.out.println("Enter the number (id) of the table you want to remove:");
                        int tableNumber = safeNumericInput(Integer.class);
                        restaurant.removeTable(tableNumber);
                        break;
                    case UPDATE_TABLE:
                        System.out.println("Enter the number (id) of the table you want to update:");
                        int tableNumberUpdate = safeNumericInput(Integer.class);
                        System.out.println("Enter the new number of seats of the table:");
                        int newSeats = safeNumericInput(Integer.class);
                        displayEnumOptions(TableStatus.class);
                        int tableStatusOption = safeNumericInput(Integer.class);
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
            option = safeNumericInput(Integer.class);
            try{
                ModifyRestaurant modifyRestaurant = modifyRestaurants[option - 1];
                switch (modifyRestaurant){
                    case MODIFY_STAFF:
                        System.out.println("First the data from restaurant about the staff will be modified");
                        modifyStaffOptions(restaurant);
                        System.out.println("Now the data from the auth system will be modified");
                        modifyStaffOptions(authSystem);
                        break;
                    case MODIFY_TABLES:
                        modifyTableOptions(restaurant);
                        break;
                    case MODIFY_MENU:
                        modifyMenuOptions(restaurant);
                        break;
                    case MODIFY_RESTAURANT_DATA:
                        System.out.println("Enter the new name of the restaurant:");
                        String name = new Scanner(System.in).nextLine();
                        System.out.println("Enter the new address of the restaurant:");
                        String address = new Scanner(System.in).nextLine();
                        System.out.println("Enter the new phone number of the restaurant:");
                        String phoneNumber = new Scanner(System.in).nextLine();
                        System.out.println("Enter the new email of the restaurant:");
                        String email = new Scanner(System.in).nextLine();
                        System.out.println("Enter the new website of the restaurant:");
                        String website = new Scanner(System.in).nextLine();
                        restaurant.setRestaurantData(new RestaurantData(name, address, phoneNumber, email, website));
                        JsonUtil.appendToJson(restaurant.getRestaurantData(), "restaurantData.json", RestaurantData.class);
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
                    option = safeNumericInput(Integer.class);
                    try{
                        ClientOptions clientOptions = ClientOptions.values()[option - 1];
                        System.out.println("Enter your full name:");
                        String clientName = scanner.nextLine();
                        switch (clientOptions){
                            case MAKE_RESERVATION:
                                ReservationData reservationData = createReservation();
                                restaurant.makeReservation(reservationData);
                                break;
                            case UPDATE_RESERVATION:
                                displayEnumOptions(UpdateReservationType.class);
                                int updateOption = safeNumericInput(Integer.class);
                                UpdateReservationType updateReservationType = UpdateReservationType.values()[updateOption - 1];
                                Reservation oldReservation = restaurant.findReservationByPersonName(clientName);
                                ReservationData reservationDataUpdate = new ReservationData(oldReservation.getFullName(), oldReservation.getPhoneNumber(), oldReservation.getReservationDateTime(), oldReservation.getNumberOfPeople());
                                switch (updateReservationType){
                                    case FULL_NAME:
                                        System.out.println("Enter the new full name:");
                                        String newFullName = scanner.nextLine();
                                        reservationDataUpdate.setFullName(newFullName);
                                        restaurant.updateReservation(reservationDataUpdate);

                                        break;
                                    case PHONE_NUMBER:
                                        System.out.println("Enter the new phone number:");
                                        String newPhoneNumber = scanner.nextLine();
                                        reservationDataUpdate.setPhoneNumber(newPhoneNumber);
                                        restaurant.updateReservation(reservationDataUpdate);
                                        break;
                                    case RESERVATION_DATE_TIME:
                                        LocalDateTime newReservationDateTime = createDateTime();
                                        reservationDataUpdate.setReservationDateTime(newReservationDateTime);
                                        restaurant.updateReservation(reservationDataUpdate);
                                        break;
                                    case NUMBER_OF_PEOPLE:
                                        System.out.println("Enter the new number of people:");
                                        int newNumberOfPeople = safeNumericInput(Integer.class);
                                        reservationDataUpdate.setNumberOfPeople(newNumberOfPeople);
                                        restaurant.updateReservation(reservationDataUpdate);
                                        break;
                                    case ALL:
                                        ReservationData reservationDataAll = createReservation();
                                        restaurant.updateReservation(reservationDataAll);
                                        break;
                                }
                                break;
                            case CANCEL_RESERVATION:
                                restaurant.cancelReservation(clientName);
                                break;
                            case VIEW_MENU:
                                restaurant.getMenu().displayMenu();
                                break;
                            case MAKE_ORDER:
                                ArrayList<ClientOrder> orders = new ArrayList<>();
                                ClientOrder clientOrder = new ClientOrder();
                                do {
                                    displayEnumOptions(ClientOrderOptions.class);
                                    option = safeNumericInput(Integer.class);
                                    try{
                                        ClientOrderOptions clientOrderOptions = ClientOrderOptions.values()[option - 1];
                                        switch (clientOrderOptions){
                                            case ADD_ITEM:
                                                restaurant.getMenu().displayMenu();
                                                System.out.println("Enter the name of the item you want to add:");
                                                String itemName = scanner.nextLine();
                                                System.out.println("Enter the quantity of the item you want to add:");
                                                int quantity = safeNumericInput(Integer.class);
                                                clientOrder.addItem(restaurant.getMenu().findMenuItemByName(itemName), quantity);
                                                break;
                                            case REMOVE_ITEM:
                                                System.out.println("Enter the name of the item you want to remove:");
                                                String itemNameRemove = scanner.nextLine();
                                                int quantityRemove = clientOrder.getQuantity(restaurant.getMenu().findMenuItemByName(itemNameRemove));
                                                clientOrder.removeItem(restaurant.getMenu().findMenuItemByName(itemNameRemove), quantityRemove);
                                                break;
                                            case CHANGE_QUANTITY:
                                                System.out.println("Enter the name of the item you want to change the quantity:");
                                                String itemNameChange = scanner.nextLine();
                                                System.out.println("Enter the new quantity of the item:");
                                                int newQuantity = safeNumericInput(Integer.class);
                                                clientOrder.changeQuantity(restaurant.getMenu().findMenuItemByName(itemNameChange), newQuantity);
                                                break;
                                            case EXIT:
                                                System.out.println("Exiting...");
                                                break;
                                        }
                                    }
                                    catch (ArrayIndexOutOfBoundsException e){
                                        System.out.println("Invalid option!");
                                        break;
                                    }
                                }while (true);
                                restaurant.addOrder(LocalDateTime.now(), clientOrder);
                                orders.add(clientOrder);
                                JsonUtil.appendToJson(orders, "orders.json", ClientOrder.class);
                                break;
                            case VIEW_ORDERS:
                                restaurant.getOrders().forEach(System.out::println);
                                break;
                            case CANCEL_ORDER:
                                restaurant.removeOrder(clientName);
                                JsonUtil.removeFromJson("orders.json", ClientOrder.class, item -> item.getClientName().equals(clientName));
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
                    option = safeNumericInput(Integer.class);
                    try{
                        StaffOptions staffOptions = StaffOptions.values()[option - 1];
                        switch (staffOptions){
                            case VIEW_RESERVATIONS:
                                restaurant.getReservations().forEach(System.out::println);
                                break;
                            case VIEW_ORDERS:
                                restaurant.getOrders().forEach(System.out::println);
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
                    option = safeNumericInput(Integer.class);
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
                                restaurant.getOrders().forEach(System.out::println);
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

    public static void loadDataFromJson(AuthSystem authSystem, Restaurant restaurant)
    {
        List<User> users = JsonUtil.loadFromJson("users.json", User.class);
        if (users != null) {
            users.forEach(authSystem::addUser);
        }
        List<RestaurantData> restaurantData = JsonUtil.loadFromJson("restaurantData.json", RestaurantData.class);
        if (restaurantData != null) {
            restaurantData.forEach(restaurant::setRestaurantData);
        }
        List<Employee> employees = JsonUtil.loadFromJson("employees.json", Employee.class);
        if (employees != null) {
            employees.forEach(employee -> restaurant.getStaff().addEmployee(employee, true));
        }
        List<Table> tables = JsonUtil.loadFromJson("tables.json", Table.class);
        if (tables != null) {
            tables.forEach(restaurant::addTable);
        }
        List<Drink> drinks = JsonUtil.loadFromJson("drinks.json", Drink.class);
        if (drinks != null) {
            drinks.forEach(drink -> restaurant.getMenu().addDrink(drink, true));
        }
        List<Food> foods = JsonUtil.loadFromJson("foods.json", Food.class);
        if (foods != null) {
            foods.forEach(food -> restaurant.getMenu().addFood(food, true));
        }
        List<Reservation> reservations = JsonUtil.loadFromJson("reservations.json", Reservation.class);
        if (reservations != null) {
            reservations.forEach(reservation -> restaurant.makeReservation(new ReservationData(reservation.getFullName(), reservation.getPhoneNumber(), reservation.getReservationDateTime(), reservation.getNumberOfPeople())));
        }
        List<ClientOrder> orders = JsonUtil.loadFromJson("orders.json", ClientOrder.class);
        if (orders != null) {
            orders.forEach(clientOrder -> restaurant.addOrder(clientOrder.getOrderTime(), clientOrder));
        }
    }

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        AuthSystem authSystem = new AuthSystem();
        loadDataFromJson(authSystem, restaurant);
        while (!beginConsoleIO(authSystem)) {
           beginConsoleIO(authSystem);
        }

        if (authSystem.getCurrentUser() != null) {
            mainConsoleIO(authSystem.getCurrentUser().getRole(), restaurant, authSystem);
        }
    }
}