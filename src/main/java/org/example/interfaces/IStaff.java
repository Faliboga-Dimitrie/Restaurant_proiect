package org.example.interfaces;

import org.example.enums.EmployeeUpdateType;
import org.example.models.Employee;

import java.util.ArrayList;

public interface IStaff {
    void addEmployee(Employee employee, boolean fromJson);
    void removeEmployee(int id);
    void removeEmployee(String name);
    Employee findEmployeeByName(int id);
    Employee findEmployeeByName(String name);
    <T> void updateEmployee(String name, String surname, T value, EmployeeUpdateType field);
    ArrayList<Employee> getEmployees();
}
