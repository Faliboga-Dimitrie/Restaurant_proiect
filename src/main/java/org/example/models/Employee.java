package org.example.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee extends Person{
    private double salary;
    private LocalDate hireDate;
    @JsonProperty("id")
    private int ID;

    public Employee() {
        super();
        salary = 0;
        hireDate = LocalDate.now();
    }

    public Employee(Person person, double salary, LocalDate hireDate, int ID) {
        super(person.getName(), person.getSurname(), person.getAge(), person.getPhoneNumber(), person.getDateOfBirth());
        this.salary = salary;
        this.hireDate = hireDate;
        this.ID = ID;
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

    public int getID() {
        return ID;
    }
}
