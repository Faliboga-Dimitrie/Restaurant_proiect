package org.example.models;

import java.util.*;
import java.time.LocalDate;
import org.example.enums.EmployeeUpdateType;
import org.example.interfaces.IStaff;

public class Staff implements IStaff {
    private final ArrayList<Employee> employees;
    private final HashMap<Integer,Integer> employeesById = new HashMap<>();
    private final HashMap<String,Integer> employeesByName = new HashMap<>();

    public Staff() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee, boolean fromJson) {
        employees.add(employee);
        int index = employees.size() - 1;
        employeesById.put(employee.getID(),index);
        employeesByName.put(employee.getName() + employee.getSurname(),index);
        if (!fromJson) {
            JsonUtil.appendToJson(employee, "employees.json", Employee.class);
        }
    }

    public void removeEmployee(int id) {
        Employee employee = findEmployeeByName(id);
        if (employee == null) {
            return;
        }

        JsonUtil.removeFromJson("employees.json", Employee.class, item -> item.getID() == id);
        employees.remove((int) employeesById.get(id));
        employeesById.remove(id);
        employeesByName.get(employee.getName());
    }

    public void removeEmployee(String name) {
        Employee employee = findEmployeeByName(name);
        if (employee == null) {
            return;
        }

        JsonUtil.removeFromJson("employees.json", Employee.class, item -> item.getName().equals(name));
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

    public <T> void updateEmployee(String name, String surname, T value, EmployeeUpdateType field) {
        Employee employee = findEmployeeByName(name + surname);
        int index;

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        switch (field) {
            case NAME:
                index = employeesByName.get(employee.getName() + employee.getSurname());
                employeesByName.remove(employee.getName() + employee.getSurname());
                employee.setName((String) value);
                employeesByName.put(employee.getName() + employee.getSurname(), index);

                JsonUtil.updateElementInJson(
                        "employees.json",
                        Employee.class,
                        item -> item.getName().equals(name) && item.getSurname().equals(surname),
                        item -> item.setName((String) value)
                );
                break;

            case SURNAME:
                index = employeesByName.get(employee.getName() + employee.getSurname());
                employeesByName.remove(employee.getName() + employee.getSurname());
                employee.setSurname((String) value);
                employeesByName.put(employee.getName() + employee.getSurname(), index);

                JsonUtil.updateElementInJson(
                        "employees.json",
                        Employee.class,
                        item -> item.getName().equals(name) && item.getSurname().equals(surname),
                        item -> item.setSurname((String) value)
                );
                break;

            case AGE:
                employee.setAge((int) value);
                JsonUtil.updateElementInJson(
                        "employees.json",
                        Employee.class,
                        item -> item.getName().equals(name) && item.getSurname().equals(surname),
                        item -> item.setAge((int) value)
                );
                break;

            case PHONE_NUMBER:
                employee.setPhoneNumber((String) value);
                JsonUtil.updateElementInJson(
                        "employees.json",
                        Employee.class,
                        item -> item.getName().equals(name) && item.getSurname().equals(surname),
                        item -> item.setPhoneNumber((String) value)
                );
                break;

            case DATE_OF_BIRTH:
                employee.setDateOfBirth((LocalDate) value);
                JsonUtil.updateElementInJson(
                        "employees.json",
                        Employee.class,
                        item -> item.getName().equals(name) && item.getSurname().equals(surname),
                        item -> item.setDateOfBirth((LocalDate) value)
                );
                break;

            case SALARY:
                employee.setSalary((double) value);
                JsonUtil.updateElementInJson(
                        "employees.json",
                        Employee.class,
                        item -> item.getName().equals(name) && item.getSurname().equals(surname),
                        item -> item.setSalary((double) value)
                );
                break;

            case HIRED_DATE:
                employee.setHireDate((LocalDate) value);
                JsonUtil.updateElementInJson(
                        "employees.json",
                        Employee.class,
                        item -> item.getName().equals(name) && item.getSurname().equals(surname),
                        item -> item.setHireDate((LocalDate) value)
                );
                break;

            default:
                System.out.println("Unknown field.");
                break;
        }
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }
}
