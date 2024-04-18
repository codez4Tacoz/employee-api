package com.example.rqchallenge.employees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.rqchallenge.employees.remoteDtos.BaseRemoteDto;
import com.example.rqchallenge.employees.remoteDtos.CreatedEmployee;

import feign.FeignException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

@Component
public class EmployeeService {

    @Autowired
    private EmployeeClient employeeClient;

    Logger logger = LoggerFactory.getLogger(EmployeeService.class); 


    public List<Employee> getAllEmployees()  {
            try {
                return employeeClient.findAll().getData();
            } catch (FeignException e) {
                //"If you are unable to successfully receive responses from the endpoints, mocking the response calls may prove to be helpful."
                if (e.status() == 429) {
                    logger.debug("Received 429 status code. Using mocked response for getAllEmployees.");

                    Employee employee1 = new Employee("1", "John Doe", 30, 50000);
                    Employee employee2 = new Employee("2", "Janel Doe", 28, 150000);
                    Employee employee3 = new Employee("3", "Janel Doerty", 38, 100000);
                    return Arrays.asList(employee1, employee2, employee3);
                } else {
                    logger.error("Failed to retrieve employees. FeignException occurred.", e);
                    throw e;
                }
            }
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {
        var lowerSearchString = searchString.toLowerCase();

        // There is a choice here... this is the choice between a full match and a partial match.  
        // Since there were no additional specifications, I went with the approach that most closely aligns with what was requested of me (full match), 
        // but it would be as simple as switching the "equals" to a "contains" to support a partial match
        return getAllEmployees().stream().filter(e -> e.getEmployeeName().toLowerCase().equals(lowerSearchString)).toList();
    }

    public Employee getEmployeeById(String id) {
        try {
            return employeeClient.getById(id).getData();
        } catch (FeignException e) {
            //"If you are unable to successfully receive responses from the endpoints, mocking the response calls may prove to be helpful."
            if (e.status() == 429) {
                logger.debug("Received 429 status code. Using mocked response for getEmployeeById.");
                return new Employee(id, "Mocked DataUser", 30, 50000);
            } else {
                logger.error("Failed to retrieve employee by id. FeignException occurred.", e);
                throw e;
            }
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        Comparator<Employee> cmp = Comparator.comparing(Employee::getEmployeeSalary);
        Optional<Employee> highestPaidEmployee = getAllEmployees().stream().max(cmp);
        return highestPaidEmployee.map(Employee::getEmployeeSalary).orElse(null);
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployees();

        PriorityQueue<Employee> pq = new PriorityQueue<>(Comparator.comparing(Employee::getEmployeeSalary).reversed());
        pq.addAll(employees);

        var topTenNames = new ArrayList<String>(10);
        var numResults = Math.min(pq.size(), 10);
        for(int i = 0; i < numResults; i++){
            topTenNames.add(pq.poll().getEmployeeName());
        }

        return topTenNames;
    }

    public Employee createEmployee(Map<String, Object> employeeInput) {
        try {
            var apiInputMap = mapFromControllerInputToApiInput(employeeInput);
            BaseRemoteDto<CreatedEmployee> createResponse = employeeClient.create(apiInputMap);
            CreatedEmployee emp = createResponse.getData();
            return new Employee(emp.getId(), emp.getName(), emp.getAge(), emp.getSalary());
        } catch (FeignException e) {
            //"If you are unable to successfully receive responses from the endpoints, mocking the response calls may prove to be helpful."
            if (e.status() == 429) {
                logger.debug("Received 429 status code. Using mocked response for createEmployee.");
                return new Employee("23", "Mocked DataUser", 30, 50000);
            } else {
                logger.error("Failed to create employee. FeignException occurred.", e);
                throw e;
            }
        }
    }    


    public String deleteEmployeeById(String id) {
        try {
            return employeeClient.delete(id).getData();
        } catch (FeignException e) {
            //"If you are unable to successfully receive responses from the endpoints, mocking the response calls may prove to be helpful."
            if (e.status() == 429) {
                logger.debug("Received 429 status code. Using mocked response for deleteEmployeeById.");
                return id;
            } else {
                logger.error("Failed to delete employee by id. FeignException occurred.", e);
                throw e;
            }
        }
    }

    // #region private methods
    private Map<String, Object> mapFromControllerInputToApiInput(Map<String, Object> employeeInput) {
        Map<String, Object> apiInputMap = new HashMap<>();
        
        if (employeeInput.containsKey("employee_name")) {
            apiInputMap.put("name", employeeInput.get("employee_name"));
        }
        if (employeeInput.containsKey("employee_age")) {
            apiInputMap.put("age", employeeInput.get("employee_age"));
        }
        if (employeeInput.containsKey("employee_salary")) {
            apiInputMap.put("salary", employeeInput.get("employee_salary"));
        }
    
        return apiInputMap;
    }        
    //#endregion
}
