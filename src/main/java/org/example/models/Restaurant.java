package org.example.models;

public class Restaurant {
    private RestaurantData restaurantData;
    private Menu menu;
    private Staff staff;

    public Restaurant() {
        restaurantData = new RestaurantData();
        menu = new Menu();
        staff = new Staff();
    }

    public Restaurant(RestaurantData restaurantData, Menu menu, Staff staff) {
        this.restaurantData = restaurantData;
        this.menu = menu;
        this.staff = staff;
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
}
