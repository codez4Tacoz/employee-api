package com.example.rqchallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;

import com.example.rqchallenge.employees.Employee;
import com.example.rqchallenge.employees.EmployeeClient;
import com.example.rqchallenge.employees.EmployeeService;
import com.example.rqchallenge.employees.remoteDtos.BaseRemoteDto;
import com.example.rqchallenge.employees.remoteDtos.CreatedEmployee;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private EmployeeService employeeService;

    private static Employee employee1 = new Employee("1", "John Doe", 30, 50000);
    private static Employee employee2 = new Employee("2", "Janel Doe", 28, 150000);
    private static Employee employee3 = new Employee("3", "Janel Doe", 38, 100000);

    // #region getAllEmployees
    @Test
    void getAllEmployees_returnsList_whenSuccess() throws Exception {
        var expectedEmployees = Arrays.asList(employee1, employee2);
        var employeesDto = new BaseRemoteDto<List<Employee>>();
        employeesDto.setData(expectedEmployees);
        when(employeeClient.findAll()).thenReturn(employeesDto);

        List<Employee> actualEmployees = employeeService.getAllEmployees();

        assertEquals(expectedEmployees, actualEmployees);       
    }

    @Test
    void getAllEmployees_throwsException_whenServiceThrows() throws Exception {
        when(employeeClient.findAll()).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> employeeService.getAllEmployees());
    }
    // #endregion

    // #region getEmployeesByNameSearch
    @Test
    void getEmployeesByNameSearch_findsEmployees_whenCaseDoesNotMatch() throws Exception {
        var allEmployeesList = Arrays.asList(employee1, employee2, employee3);
        var employeesDto = new BaseRemoteDto<List<Employee>>();
        employeesDto.setData(allEmployeesList);
        when(employeeClient.findAll()).thenReturn(employeesDto);
        var expectedEmployeesFound = Arrays.asList(employee1);

        List<Employee> employeesFound = employeeService.getEmployeesByNameSearch("JOHN doe");

        assertEquals(expectedEmployeesFound, employeesFound);       
    }

    @Test
    void getEmployeesByNameSearch_findsMultipleEmployees_whenMultipleExist() throws Exception {
        var allEmployeesList = Arrays.asList(employee1, employee2, employee3);
        var employeesDto = new BaseRemoteDto<List<Employee>>();
        employeesDto.setData(allEmployeesList);
        when(employeeClient.findAll()).thenReturn(employeesDto);
        var expectedEmployeesFound = Arrays.asList(employee2, employee3);

        List<Employee> employeesFound = employeeService.getEmployeesByNameSearch("Janel Doe");

        assertEquals(expectedEmployeesFound, employeesFound);      
    }


    @Test
    void getEmployeesByNameSearch_findsEmptyList_whenNoEmployeesExist() throws Exception {
        var allEmployeesList = Arrays.<Employee>asList();
        var employeesDto = new BaseRemoteDto<List<Employee>>();
        employeesDto.setData(allEmployeesList);
        when(employeeClient.findAll()).thenReturn(employeesDto);
        var expectedEmployeesFound = Arrays.<Employee>asList();

        List<Employee> employeesFound = employeeService.getEmployeesByNameSearch("Janel Doe");

        assertEquals(expectedEmployeesFound, employeesFound);      
    }

    @Test
    void getEmployeesByNameSearch_findsNoEmployees_whenNoneMatch() throws Exception {
        var allEmployeesList = Arrays.asList(employee1, employee2, employee3);
        var employeesDto = new BaseRemoteDto<List<Employee>>();
        employeesDto.setData(allEmployeesList);
        when(employeeClient.findAll()).thenReturn(employeesDto);
        var expectedEmployeesFound = Arrays.<Employee>asList();

        List<Employee> employeesFound = employeeService.getEmployeesByNameSearch("Fran Doe");

        assertEquals(expectedEmployeesFound, employeesFound);        
    }

    @Test
    void getEmployeesByNameSearch_throwsException_whenServiceThrows() throws Exception {
        when(employeeClient.findAll()).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeesByNameSearch("foo"));
    }
    // #endregion

    // #region getEmployeeById
    @Test
    void getEmployeeById_returnsEmployee_whenPassedAValidId() throws Exception {
        var searchId = employee1.getId();
        var employeeDto = new BaseRemoteDto<Employee>();
        employeeDto.setData(employee1);
        when(employeeClient.getById(searchId)).thenReturn(employeeDto);

        Employee actualEmployee = employeeService.getEmployeeById(searchId);

        assertEquals(employee1, actualEmployee);       
    }

    @Test
    void getEmployeeById_findsNoEmployees_whenTheIdBeingSearchedForIsNotFound() throws Exception {
        var employeesDtoWithNullData = new BaseRemoteDto<Employee>();
        employeesDtoWithNullData.setData(null);
        when(employeeClient.getById(ArgumentMatchers.anyString())).thenReturn(employeesDtoWithNullData);

        Employee actualEmployee = employeeService.getEmployeeById("99999");

        assertNull(actualEmployee);
    }

    @Test
    void getEmployeeById_throwsException_whenServiceThrows() throws Exception {
        var searchId = employee1.getId();
        when(employeeClient.getById(searchId)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(searchId));
    }
    // #endregion

    // #region getTopTenHighestEarningEmployeeNames
    @Test
    void getTopTenHighestEarningEmployeeNames_returnsTopTenEmployees_whenEmployeesExist() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
    
        BaseRemoteDto<List<Employee>> employeesDto = new BaseRemoteDto<>();
        employeesDto.setData(employees);
    
        when(employeeClient.findAll()).thenReturn(employeesDto);
    
        List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();
    
        List<String> expectedNames = List.of(employee2.getEmployeeName(), employee3.getEmployeeName(), employee1.getEmployeeName());
        assertEquals(expectedNames, topTenNames);
    }
    
    @Test
    void getTopTenHighestEarningEmployeeNames_returnsEmptyList_whenNoEmployeesExist() throws Exception {
        List<Employee> emptyEmployeesList = Collections.emptyList();
        BaseRemoteDto<List<Employee>> emptyEmployeesDto = new BaseRemoteDto<>();
        emptyEmployeesDto.setData(emptyEmployeesList);
    
        when(employeeClient.findAll()).thenReturn(emptyEmployeesDto);
    
        List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();
    
        assertTrue(topTenNames.isEmpty());
    }
    
    @Test
    void getTopTenHighestEarningEmployeeNames_returnsAllEmployees_whenLessThanTenEmployeesExist() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee3);
    
        BaseRemoteDto<List<Employee>> employeesDto = new BaseRemoteDto<>();
        employeesDto.setData(employees);
    
        when(employeeClient.findAll()).thenReturn(employeesDto);
    
        List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();
    
        List<String> expectedNames = List.of(employee3.getEmployeeName());
        assertEquals(expectedNames, topTenNames);
    }
    
    @Test
    void getTopTenHighestEarningEmployeeNames_throwsException_whenServiceThrows() throws Exception {
        when(employeeClient.findAll()).thenThrow(new RuntimeException());
    
        assertThrows(RuntimeException.class, () -> employeeService.getTopTenHighestEarningEmployeeNames());
    }
    
    // #endregion


    // #region createEmployee
    @Test
    void createEmployee_returnsEmployeeWithValuesMatchingInput_whenPassedEmployeeValues() throws Exception {
        Map<String, Object> controllerInputMap = new HashMap<String, Object>();
        controllerInputMap.put("employee_name", employee1.getEmployeeName());
        controllerInputMap.put("employee_age", employee1.getEmployeeAge());
        controllerInputMap.put("employee_salary", employee1.getEmployeeSalary());

        Map<String, Object> remoteInputMap = new HashMap<String, Object>();
        remoteInputMap.put("name", employee1.getEmployeeName());
        remoteInputMap.put("age", employee1.getEmployeeAge());
        remoteInputMap.put("salary", employee1.getEmployeeSalary());

        var employeeDto = new BaseRemoteDto<CreatedEmployee>();
        var dataResponse = new CreatedEmployee(employee1.getEmployeeName(), employee1.getEmployeeAge(), employee1.getEmployeeSalary());
        employeeDto.setData(dataResponse);

        when(employeeClient.create(remoteInputMap)).thenReturn(employeeDto);
        Employee expectedEmployee = new Employee(dataResponse);

        Employee employeeReturned = employeeService.createEmployee(controllerInputMap);

        assertEquals(expectedEmployee, employeeReturned);       
    }

    @Test
    void createEmployee_returnsEmployeeWithValuesMatchingInputAndIgnoresExtraValues_whenPassedEmployeeValuesAndExtraValues() throws Exception {
        Map<String, Object> employeeInputMap = new HashMap<String, Object>();
        employeeInputMap.put("employee_name", employee1.getEmployeeName());
        employeeInputMap.put("employee_age", employee1.getEmployeeAge());
        employeeInputMap.put("employee_salary", employee1.getEmployeeSalary());
        employeeInputMap.put("foo", employee1.getEmployeeSalary());
        employeeInputMap.put("bar", employee1.getEmployeeSalary());

        Map<String, Object> remoteInputMap = new HashMap<String, Object>();
        remoteInputMap.put("name", employee1.getEmployeeName());
        remoteInputMap.put("age", employee1.getEmployeeAge());
        remoteInputMap.put("salary", employee1.getEmployeeSalary());


        var employeeDto = new BaseRemoteDto<CreatedEmployee>();
        var dataResponse = new CreatedEmployee(employee1.getEmployeeName(), employee1.getEmployeeAge(), employee1.getEmployeeSalary());
        employeeDto.setData(dataResponse);

        when(employeeClient.create(remoteInputMap)).thenReturn(employeeDto);
        Employee expectedEmployee = new Employee(dataResponse);

        Employee employeeReturned = employeeService.createEmployee(employeeInputMap);

        assertEquals(expectedEmployee, employeeReturned);       
    }

    @Test
    void createEmployee_returnsEmployeeWithNullValues_whenPassedEmptyArray() throws Exception {
        Map<String, Object> employeeInputMap = new HashMap<String, Object>();
        var employeeDto = new BaseRemoteDto<CreatedEmployee>();
        employeeDto.setData(new CreatedEmployee());

        when(employeeClient.create(employeeInputMap)).thenReturn(employeeDto);

        Employee employeeReturned = employeeService.createEmployee(employeeInputMap);

        assertEquals(new Employee(), employeeReturned);       
    }

    @Test
    void createEmployee_throwsException_whenServiceThrows() throws Exception {
        Map<String, Object> employeeInput = new HashMap<>();
        when(employeeClient.create(employeeInput)).thenThrow(new RuntimeException());
    
        assertThrows(RuntimeException.class, () -> employeeService.createEmployee(employeeInput));
    }
    // #endregion


    // #region deleteEmployeeById
    @Test
    void deleteEmployeeById_returnsDeletedEmployeeId_whenPassedValidId() throws Exception {
        var employeeIdToDelete = "19";
        var employeeIdDto = new BaseRemoteDto<String>();
        employeeIdDto.setData(employeeIdToDelete);
        when(employeeClient.delete(employeeIdToDelete)).thenReturn(employeeIdDto);

        String employeeIdReturned = employeeService.deleteEmployeeById(employeeIdToDelete);

        assertEquals(employeeIdReturned, employeeIdToDelete);       
    }

    @Test
    void deleteEmployeeById_returnsNull_whenPassedInvalidId() throws Exception {
        var employeeIdToDelete = "19";
        var employeeIdDto = new BaseRemoteDto<String>();
        when(employeeClient.delete(employeeIdToDelete)).thenReturn(employeeIdDto);

        String employeeIdReturned = employeeService.deleteEmployeeById(employeeIdToDelete);

        assertEquals(employeeIdReturned, null);       
    }

    @Test
    void deleteEmployeeById_throwsException_whenServiceThrows() throws Exception {
        when(employeeClient.delete(Mockito.anyString())).thenThrow(new RuntimeException());
    
        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployeeById("1123124"));
    }
    // #endregion
}
