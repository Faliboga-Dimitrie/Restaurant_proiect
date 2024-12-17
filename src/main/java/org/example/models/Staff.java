package org.example.models;

import java.util.*;
import java.time.LocalDate;
import org.example.enums.EmployeeUpdateType;

public class Staff {
    private ArrayList<Employee> employees;
    private final HashMap<Integer,Integer> employeesById = new HashMap<>();
    private final HashMap<String,List<Integer>> employeesByName = new HashMap<>();
    private final HashMap<String,List<Integer>> employeesByRole = new HashMap<>();

    public Staff() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        int index = employees.size() - 1;
        employeesById.put(employee.getID(),index);
        employeesByName.computeIfAbsent(employee.getName(), k -> new ArrayList<>()).add(index);
        employeesByRole.computeIfAbsent(employee.getRole(), k -> new ArrayList<>()).add(index);
    }

    public void removeEmployee(int id) {
        Employee employee = getEmployee(id);
        if (employee == null) {
            return;
        }
        employees.remove((int) employeesById.get(id));
        employeesById.remove(id);
        employeesByName.get(employee.getName()).remove(employeesById.get(id));
        employeesByRole.get(employee.getRole()).remove(employeesById.get(id));
    }

    public Employee getEmployee(int id) {
        Integer index = employeesById.get(id);
        return index != null ? employees.get(index) : null;
    }

    public <T> void updateEmployee(int id, T value, EmployeeUpdateType field) {
        Employee employee = getEmployee(id);
        if (employee == null) {
            return;
        }
        switch (field) {
            case NAME:
                employeesByName.get(employee.getName()).remove(employeesById.get(id));
                employee.setName((String) value);
                employeesByName.computeIfAbsent(employee.getName(), k -> new ArrayList<>()).add(employeesById.get(id));
                break;
            case SURNAME:
                employee.setSurname((String) value);
                break;
            case AGE:
                employee.setAge((int) value);
                break;
            case EMAIL:
                employee.setEmail((String) value);
                break;
            case PHONE_NUMBER:
                employee.setPhoneNumber((String) value);
                break;
            case DATE_OF_BIRTH:
                employee.setDateOfBirth((LocalDate) value);
                break;
            case ROLE:
                employeesByRole.get(employee.getRole()).remove(employeesById.get(id));
                employee.setRole((String) value);
                employeesByRole.computeIfAbsent(employee.getRole(), k -> new ArrayList<>()).add(employeesById.get(id));
                break;
            case SALARY:
                employee.setSalary((double) value);
                break;
            case HIRED_DATE:
                employee.setHireDate((LocalDate) value);
                break;
            case ID:
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
