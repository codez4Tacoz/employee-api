package com.example.rqchallenge.employees;


import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController implements IEmployeeController {

    private final EmployeeService employeeService;
    Logger logger = LoggerFactory.getLogger(EmployeeController.class); 

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            logger.info("getAllEmployees:: {} employees were found", employees.size());
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            logger.error("getAllEmployees:: An exception has been caught.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        try {
            List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
            logger.info("getEmployeesByNameSearch:: {} employees with name {} were found", employees.size(), searchString);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            logger.error("getEmployeesByNameSearch:: An exception has been caught.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            if (employee != null) {
                logger.info("getEmployeeById:: employee with Id: {} has been successfully found", id);
                return ResponseEntity.ok(employee);
            } else {
                logger.info("getEmployeeById:: employee with Id: {} was not found", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("getEmployeeById:: An exception has been caught.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
            logger.info("getHighestSalaryOfEmployees: successfully returned the highest salary.");
            return ResponseEntity.ok(highestSalary);
        } catch (Exception e) {
            logger.error("getHighestSalaryOfEmployees:: An exception has been caught.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();
            logger.info("getTopTenHighestEarningEmployeeNames: there were {} top earners found", topTenNames.size());
            return ResponseEntity.ok(topTenNames);
        } catch (Exception e) {
            logger.error("getTopTenHighestEarningEmployeeNames:: An exception has been caught.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        try {
            Employee employee = employeeService.createEmployee(employeeInput);
            logger.info("createEmployee:: Employee with Name: {} has been successfully created", employee.getEmployeeName());
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            logger.error("createEmployee:: An IllegalArgumentException has been caught.", e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            logger.error("createEmployee:: An exception has been caught.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        try {
            String message = employeeService.deleteEmployeeById(id);
            logger.info("deleteEmployeeById:: Employee with ID: {} has been successfully deleted", id);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            logger.error("deleteEmployeeById:: Failed to delete employee with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
