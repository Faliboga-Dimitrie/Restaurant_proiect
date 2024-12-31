package org.example.interfaces;

import org.example.enums.EmployeeUpdateType;
import org.example.models.Employee;

import java.util.ArrayList;

public interface IStaff {
    void addEmployee(Employee employee, boolean fromJson);
    void removeEmployee(String name);
    Employee findEmployeeByName(String name);
    ArrayList<Employee> getEmployees();
}
