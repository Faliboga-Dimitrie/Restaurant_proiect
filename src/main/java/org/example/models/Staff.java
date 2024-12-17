package org.example.models;

import java.util.*;
import java.time.LocalDate;
import org.example.enums.EmployeeUpdateType;

public class Staff {
    private final ArrayList<Employee> employees;
    private final HashMap<Integer,Integer> employeesById = new HashMap<>();
    private final HashMap<String,Integer> employeesByName = new HashMap<>();

    public Staff() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        int index = employees.size() - 1;
        employeesById.put(employee.getID(),index);
        employeesByName.put(employee.getName() + employee.getSurname(),index);
    }

    public void removeEmployee(int id) {
        Employee employee = findEmployeeByName(id);
        if (employee == null) {
            return;
        }
        employees.remove((int) employeesById.get(id));
        employeesById.remove(id);
        employeesByName.get(employee.getName());
    }

    public void removeEmployee(String name) {
        Employee employee = findEmployeeByName(name);
        if (employee == null) {
            return;
        }
        employees.remove((int) employeesByName.get(name));
        employeesById.remove(employee.getID());
        employeesByName.remove(name);
    }

    public Employee findEmployeeByName(int id) {
        Integer index = employeesById.get(id);
        return index != null ? employees.get(index) : null;
    }

    public Employee findEmployeeByName(String name) {
        Integer index = employeesByName.get(name);
        return index != null ? employees.get(index) : null;
    }

    public <T> void updateEmployee(String Name, String Surname, T value, EmployeeUpdateType field) {
        Employee employee = findEmployeeByName(Name+Surname);
        int index;
        if (employee == null) {
            return;
        }
        switch (field) {
            case NAME:
                index = employeesByName.get(employee.getName() + employee.getSurname());
                employeesByName.remove(employee.getName() + employee.getSurname());
                employee.setName((String) value);
                employeesByName.put(employee.getName() + employee.getSurname(), index);
                break;
            case SURNAME:
                index = employeesByName.get(employee.getName() + employee.getSurname());
                employeesByName.remove(employee.getName() + employee.getSurname());
                employee.setSurname((String) value);
                employeesByName.put(employee.getName() + employee.getSurname(), index);
                break;
            case AGE:
                employee.setAge((int) value);
                break;
            case PHONE_NUMBER:
                employee.setPhoneNumber((String) value);
                break;
            case DATE_OF_BIRTH:
                employee.setDateOfBirth((LocalDate) value);
                break;
            case SALARY:
                employee.setSalary((double) value);
                break;
            case HIRED_DATE:
                employee.setHireDate((LocalDate) value);
                break;
            default:
                break;
        }
    }

    private <T> List<Employee> getEmployeesByField(HashMap<T, List<Integer>> fieldMap, T field) {
        List<Integer> indices = fieldMap.get(field);
        if (indices == null) {
            return null;
        }
        List<Employee> employees = new ArrayList<>();
        for (int index : indices) {
            employees.add(this.employees.get(index));
        }
        return employees;
    }
}
