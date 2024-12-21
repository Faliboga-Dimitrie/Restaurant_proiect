package org.example.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Consumer;

public class JsonUtil {

    public static <T> void saveToJson(List<T> items, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(fileName), items);
            System.out.println("Datele au fost salvate în fisierul " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void appendToJson(List<T> items, String fileName, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);

        try {
            List<T> existingItems = new ArrayList<>();
            if (file.exists() && file.length() > 0) {
                existingItems = mapper.readValue(
                        file,
                        mapper.getTypeFactory().constructCollectionType(List.class, clazz)
                );
            }
            existingItems.addAll(items);
            mapper.writeValue(file, existingItems);

            System.out.println("Datele au fost adăugate în fișierul " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void appendToJson(T item, String fileName, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);

        try {
            List<T> existingItems = new ArrayList<>();

            // Verificăm dacă fișierul există și nu este gol
            if (file.exists() && file.length() > 0) {
                // Citim datele existente din fișier
                existingItems = mapper.readValue(
                        file,
                        mapper.getTypeFactory().constructCollectionType(List.class, clazz)
                );
            }

            // Adăugăm noul element la lista existentă
            existingItems.add(item);

            // Scriem lista actualizată înapoi în fișier
            mapper.writeValue(file, existingItems);

            System.out.println("Obiectul a fost adăugat în fișierul " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> loadFromJson(String fileName, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);

        if (file.exists() && file.length() > 0) {
            try {
                return mapper.readValue(
                        file,
                        mapper.getTypeFactory().constructCollectionType(List.class, clazz)
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fișierul JSON este gol sau nu există.");
        }
        return null;
    }

    public static <T> void removeFromJson(String fileName, Class<T> clazz, Predicate<T> condition) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);

        try {
            List<T> items = new ArrayList<>();

            // Verificăm dacă fișierul există și nu este gol
            if (file.exists() && file.length() > 0) {
                // Citește lista de elemente existente
                items = mapper.readValue(
                        file,
                        mapper.getTypeFactory().constructCollectionType(List.class, clazz)
                );
            }

            // Filtrăm elementele, păstrând doar cele care nu satisfac condiția
            items.removeIf(condition);

            // Rescriem lista actualizată în fișier
            mapper.writeValue(file, items);

            System.out.println("Elementul a fost eliminat (dacă a existat) din fișierul " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void updateElementInJson(
            String fileName,
            Class<T> clazz,
            Predicate<T> condition,
            Consumer<T> updater) {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);

        try {
            List<T> items = mapper.readValue(file, new TypeReference<>() {});

            boolean updated = false;
            for (T item : items) {
                if (condition.test(item)) {
                    updater.accept(item);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                mapper.writeValue(file, items);
                System.out.println("Element updated successfully!");
            } else {
                System.out.println("No element matching the condition was found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
