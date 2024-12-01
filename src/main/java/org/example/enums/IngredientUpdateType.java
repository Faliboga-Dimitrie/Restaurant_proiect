package org.example.enums;

public enum IngredientUpdateType {
    NAME,
    QUANTITY,
    UNIT,
    IS_VEGETARIAN,
    IS_ALLERGEN,
    NONE,
    ALL;

    public String enumToString(IngredientUpdateType type)
    {
        switch (type){
            case NAME:
                return "Name";
            case QUANTITY:
                return "Quantity";
            case UNIT:
                return "Unit";
            case IS_VEGETARIAN:
                return "IsVegetarian";
            case IS_ALLERGEN:
                return "IsAllergen";
            case NONE:
                return "None";
            case ALL:
                return "NAME " + " QUANTITY " + " UNIT " + " IS_VEGETARIAN " + " IS_ALLERGEN" + " ALL ";
        }
        return "";
    }

    public void print(){
        System.out.println(enumToString(IngredientUpdateType.ALL));
    }
}


