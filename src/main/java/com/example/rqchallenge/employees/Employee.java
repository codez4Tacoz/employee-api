package com.example.rqchallenge.employees;

import java.util.Objects;

import com.example.rqchallenge.employees.remoteDtos.CreatedEmployee;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Note: 
 * There are some inconsistencies in the dummy api documentation.  Their api docs describe the id types as string, but the api returns ints.
 * Our IEmployeeController interface passes in String ids to the getById method, so making the type a String on our end.  Jackson will convert without issue.
 * Additionally, their api appears to allow nullable fields on create and update, so using Boxed Integer type for age and salary.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Employee {
    private String id;      
    private String employeeName;
    private Integer employeeAge;
    private Integer employeeSalary;     
    
    public Employee() {
    }
    
    public Employee(String id, String name, Integer age, Integer salary) {
        this.id = id;
        this.employeeName = name;
        this.employeeAge = age;
        this.employeeSalary = salary;
    }

    public Employee(CreatedEmployee employee) {
        this.id = employee.getId();
        this.employeeName = employee.getName();
        this.employeeAge = employee.getAge();
        this.employeeSalary = employee.getSalary();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(Integer employeeAge) {
        this.employeeAge = employeeAge;
    }

    public Integer getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(Integer employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(employeeName, employee.employeeName) &&
                Objects.equals(employeeAge, employee.employeeAge) &&
                Objects.equals(employeeSalary, employee.employeeSalary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeName, employeeAge, employeeSalary);
    }
    
}
