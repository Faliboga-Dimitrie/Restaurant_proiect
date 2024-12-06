package org.example.models;

import java.time.LocalDate;
import java.time.Period;
import java.time.DayOfWeek;

public class Employee extends Person{
    private String role;
    private double salary;
    private LocalDate hireDate;
    private WorkSchedule workSchedule;
    private int ID;

    public Employee() {
        super();
        role = "";
        salary = 0;
        hireDate = LocalDate.now();
        workSchedule = new WorkSchedule();
        ID = 0;
    }

    public Employee(String name, String surname, int age, String email, String phoneNumber, LocalDate dateOfBirth, String position, double salary, LocalDate hireDate, int ID) {
        super(name, surname, age, email, phoneNumber, dateOfBirth);
        this.role = position;
        this.salary = salary;
        this.hireDate = hireDate;
        workSchedule = new WorkSchedule();
        this.ID = ID;
    }

    public Employee(Person person, String position, double salary, LocalDate hireDate, int ID) {
        super(person.getName(), person.getSurname(), person.getAge(), person.getEmail(), person.getPhoneNumber(), person.getDateOfBirth());
        this.role = position;
        this.salary = salary;
        this.hireDate = hireDate;
        workSchedule = new WorkSchedule();
        this.ID = ID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", position='" + role + '\'' +
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

    public void setWorkDay(DayOfWeek day, int startHour, int endHour, int breakStart, int breakEnd) {
        workSchedule.addWorkDay(day, startHour, endHour, breakStart, breakEnd);
    }

    public WorkDay getWorkDay(DayOfWeek day) {
        return workSchedule.getWorkDay(day);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }
}
