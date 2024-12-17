package org.example.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class Employee extends Person{
    private double salary;
    private LocalDate hireDate;
    private final String ID;

    public Employee() {
        super();
        salary = 0;
        hireDate = LocalDate.now();
        ID = UUID.randomUUID().toString();
    }

    public Employee(String name, String surname, int age, String phoneNumber, LocalDate dateOfBirth, double salary, LocalDate hireDate) {
        super(name, surname, age, phoneNumber, dateOfBirth);
        this.salary = salary;
        this.hireDate = hireDate;
        this.ID = UUID.randomUUID().toString();
    }

    public Employee(Person person, double salary, LocalDate hireDate) {
        super(person.getName(), person.getSurname(), person.getAge(), person.getPhoneNumber(), person.getDateOfBirth());
        this.salary = salary;
        this.hireDate = hireDate;
        this.ID = UUID.randomUUID().toString();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                '}';
    }

    public void longevity() {
        System.out.println("Employee " + name + " " + surname + " has been working for " + (LocalDate.now().getYear() - hireDate.getYear()) + " years, and " + (LocalDate.now().getMonthValue() - hireDate.getMonthValue()) + " months.");
    }

    public Period getLongevity() {
        return Period.between(hireDate, LocalDate.now());
    }

    public int getID() {
        return Integer.parseInt(ID);
    }
}
