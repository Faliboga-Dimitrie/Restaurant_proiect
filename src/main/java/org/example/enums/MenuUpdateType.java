package org.example.enums;

public enum MenuUpdateType {
    NAME,
    PRICE,
    INGREDIENTS,
    DESCRIPTION,
    CALORIES,
    AVAILABILITY,
    NONE,
    ALL;

    public String enumToString(MenuUpdateType type) {
        switch (type) {
            case NAME:
                return "Name";
            case PRICE:
                return "Price";
            case INGREDIENTS:
                return "Ingredients";
            case DESCRIPTION:
                return "Description";
            case NONE:
                return "None";
            case ALL:
                return "NAME " + " PRICE " + " INGREDIENTS " + " DESCRIPTION " + " ALL ";
        }
        return "";
    }

    public void print(){
        System.out.println(enumToString(MenuUpdateType.ALL));
    }

}
