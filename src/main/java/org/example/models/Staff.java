package org.example.models;

import java.util.*;
import java.time.LocalDate;

public class Staff {
    private ArrayList<Employee> employees;
    private Map<Integer,Integer> employeesById;
    private Map<String,List<Integer>> employeesByName;
    private Map<String,List<Integer>> employeesByRole;

    public Staff() {
        employees = new ArrayList<>();
        employeesById = new HashMap<>();
        employeesByName = new HashMap<>();
        employeesByRole = new HashMap<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        int index = employees.size() - 1;
        employeesById.put(employee.getID(),index);
        employeesByName.computeIfAbsent(employee.getName(), k -> new ArrayList<>()).add(index);
        employeesByRole.computeIfAbsent(employee.getRole(), k -> new ArrayList<>()).add(index);
    }

    public Employee getEmployee(int id) {
        Integer index = employeesById.get(id);
        return index != null ? employees.get(index) : null;
    }

    public <T> void updateEmployee(int id, T value, String field) {
        Employee employee = getEmployee(id);
        if (employee == null) {
            return;
        }
        switch (field) {
            case "name":
                employeesByName.get(employee.getName()).remove(employeesById.get(id));
                employee.setName((String) value);
                employeesByName.computeIfAbsent(employee.getName(), k -> new ArrayList<>()).add(employeesById.get(id));
                break;
            case "surname":
                employee.setSurname((String) value);
                break;
            case "age":
                employee.setAge((int) value);
                break;
            case "email":
                employee.setEmail((String) value);
                break;
            case "phoneNumber":
                employee.setPhoneNumber((String) value);
                break;
            case "dateOfBirth":
                employee.setDateOfBirth((LocalDate) value);
                break;
            case "role":
                employeesByRole.get(employee.getRole()).remove(employeesById.get(id));
                employee.setRole((String) value);
                employeesByRole.computeIfAbsent(employee.getRole(), k -> new ArrayList<>()).add(employeesById.get(id));
                break;
            case "salary":
                employee.setSalary((double) value);
                break;
            case "hireDate":
                employee.setHireDate((LocalDate) value);
                break;
            case "workSchedule":
                employee.setWorkSchedule((WorkSchedule) value);
                break;
            case "ID":
                employeesById.remove(id);
                employee.setID((int) value);
                employeesById.put(employee.getID(),employeesById.get(id));
                break;
            default:
                break;
        }
    }

    private <T> List<Employee> getEmployeesByField(Map<T, List<Integer>> fieldMap, T field) {
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

    public List<Employee> getEmployeesByName(String name) {
        return getEmployeesByField(employeesByName, name);
    }

    public List<Employee> getEmployeesByRole(String role) {
        return getEmployeesByField(employeesByRole, role);
    }
}
