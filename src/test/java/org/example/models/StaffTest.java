package org.example.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StaffTest {

    @Test
    void addEmployee() {
        LocalDate date = LocalDate.of(1996, 1, 1);
        Person person = new Person("John", "Doe", 25, "123456789", date);
        Employee employee = new Employee(person, 1000, LocalDate.now(), 1);
        Staff staff = new Staff();
        staff.addEmployee(employee, false);
        assertEquals(1, staff.getEmployees().size());
    }

    @Test
    void removeEmployee() {
        LocalDate date = LocalDate.of(1996, 1, 1);
        Person person = new Person("John", "Doe", 25, "123456789", date);
        Employee employee = new Employee(person, 1000, LocalDate.now(), 1);
        Staff staff = new Staff();
        staff.addEmployee(employee, false);
        staff.removeEmployee("John" + "Doe");
        assertEquals(0, staff.getEmployees().size());
    }

    @Test
    void findEmployeeByName() {
        LocalDate date = LocalDate.of(1996, 1, 1);
        Person person = new Person("John", "Doe", 25, "123456789", date);
        Employee employee = new Employee(person, 1000, LocalDate.now(), 1);
        Staff staff = new Staff();
        staff.addEmployee(employee, false);
        assertEquals(employee, staff.findEmployeeByName("John" + "Doe"));
    }
}