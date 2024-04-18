package com.example.rqchallenge;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.rqchallenge.employees.Employee;
import com.example.rqchallenge.employees.EmployeeController;
import com.example.rqchallenge.employees.EmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService mockEmployeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private static Employee employee1 = new Employee("1", "John Doe", 30, 50000);

    // #region getAllEmployees
    @Test
    void getAllEmployees_returnsList_whenSuccess() throws Exception {
        List<Employee> mockEmployeesList = new ArrayList<>();
        mockEmployeesList.add(employee1);

        when(mockEmployeeService.getAllEmployees()).thenReturn(mockEmployeesList);

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(employee1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employee_name").value(employee1.getEmployeeName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employee_age").value(employee1.getEmployeeAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employee_salary").value(employee1.getEmployeeSalary()))
                .andReturn();          
    }

    @Test
    void getAllEmployees_returns500_whenExceptionOccurs() throws Exception {
        when(mockEmployeeService.getAllEmployees()).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
    // #endregion

    // #region getEmployeesByNameSearch
    @Test
    void getAllEmployeesByNameSearch_returnsList_whenSuccess() throws Exception {
        String searchString = "Janel";
        List<Employee> mockEmployees = new ArrayList<>();
        mockEmployees.add(employee1);

        when(mockEmployeeService.getEmployeesByNameSearch(searchString)).thenReturn(mockEmployees);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/search/{searchString}", searchString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).isNotNull();

        DocumentContext documentContext = JsonPath.parse(content);
        assertThat(documentContext.read("$.length()", Integer.class)).isEqualTo(1);
        assertThat(documentContext.read("[0].id", String.class)).isEqualTo(employee1.getId());
        assertThat(documentContext.read("[0].employee_name", String.class)).isEqualTo(employee1.getEmployeeName());
        assertThat(documentContext.read("[0].employee_age", Integer.class)).isEqualTo(employee1.getEmployeeAge());
        assertThat(documentContext.read("[0].employee_salary", Integer.class)).isEqualTo(employee1.getEmployeeSalary());
    }


    @Test
    void getAllEmployeesByNameSearch_returnsEmptyList_whenNoneFound() throws Exception {
        String searchString = "Janel";
        List<Employee> mockEmployees = new ArrayList<>();

        when(mockEmployeeService.getEmployeesByNameSearch(searchString)).thenReturn(mockEmployees);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/search/{searchString}", searchString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull();

        DocumentContext documentContext = JsonPath.parse(content);
        assertThat(documentContext.read("$.length()", Integer.class)).isZero();
    }

    @Test
    void getAllEmployeesByNameSearch_returns500_whenExceptionOccurs() throws Exception {
        String searchString = "Janel";
        when(mockEmployeeService.getEmployeesByNameSearch(searchString)).thenThrow(new RuntimeException("Error occurred"));

        mockMvc.perform(MockMvcRequestBuilders.get("/search/{searchString}", searchString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
    }
    // #endregion

    // #region getEmployeeById
    @Test
    void getEmployeeById_returnsEmployee_whenSuccess() throws Exception {
        when(mockEmployeeService.getEmployeeById(employee1.getId())).thenReturn(employee1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/{id}", employee1.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).isNotNull();

        DocumentContext documentContext = JsonPath.parse(content);
        assertThat(documentContext.read("$.id", String.class)).isEqualTo(employee1.getId());
        assertThat(documentContext.read("$.employee_name", String.class)).isEqualTo(employee1.getEmployeeName());
        assertThat(documentContext.read("$.employee_age", Integer.class)).isEqualTo(employee1.getEmployeeAge());
        assertThat(documentContext.read("$.employee_salary", Integer.class)).isEqualTo(employee1.getEmployeeSalary());
    }


    @Test
    void getEmployeeById_returns404_whenEmployeeNotFound() throws Exception {
        when(mockEmployeeService.getEmployeeById("123")).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/{id}", employee1.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.isEmpty() || "[]".equals(content));
    }

    @Test
    void getEmployeeById_returns500_whenExceptionOccurs() throws Exception {
        when(mockEmployeeService.getEmployeeById("123")).thenThrow(new RuntimeException("Error occurred"));

        mockMvc.perform(MockMvcRequestBuilders.get("/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
    }
    // #endregion

    // #region getHighestSalaryOfEmployees
    @Test
    void getHighestSalaryOfEmployees_returnsIntegerHighestSalary_whenSuccess() throws Exception {
        Integer oneMillionDollars = 1000000;
        when(mockEmployeeService.getHighestSalaryOfEmployees()).thenReturn(oneMillionDollars);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/highestSalary")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        int actualHighestSalary = Integer.parseInt(result.getResponse().getContentAsString());
        assertEquals(oneMillionDollars, actualHighestSalary, "Highest salary should match expecations");
    }

    @Test
    void getHighestSalaryOfEmployees_returns500_whenExceptionOccurs() throws Exception {
        when(mockEmployeeService.getHighestSalaryOfEmployees()).thenThrow(new RuntimeException("Error occurred"));

        mockMvc.perform(MockMvcRequestBuilders.get("/highestSalary")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andReturn();
    }
    // #endregion

    // #region getTopTenHighestEarningEmployeeNames
    @Test
    void getTopTenHighestEarningEmployeeNames_returnsTop10HighestEarningEmployeeNames_whenSuccess() throws Exception {
        List<String> expectedHighestEarnersList = Arrays.asList(
                "Peter Parker", "Tony Stark", "Steve Rogers", "Bruce Banner",
                "Natasha Romanoff", "Clint Barton", "Wanda Maximoff", 
                "Thor Odinson", "Carol Danvers", "T'Challa"
        );

        when(mockEmployeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(expectedHighestEarnersList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/topTenHighestEarningEmployeeNames")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> actualTop10List = objectMapper.readValue(responseContent, new TypeReference<List<String>>() {});
        
        assertEquals(expectedHighestEarnersList.size(), actualTop10List.size(), "Number of elements should match");

        for (String expectedName : expectedHighestEarnersList) {
            assertTrue(actualTop10List.contains(expectedName), "Expected name '" + expectedName + "' not found");
        }
    }


    @Test
    void getTopTenHighestEarningEmployeeNames_returns500_whenExceptionOccurs() throws Exception {
        when(mockEmployeeService.getTopTenHighestEarningEmployeeNames()).thenThrow(new RuntimeException("Error occurred"));

        mockMvc.perform(MockMvcRequestBuilders.get("/topTenHighestEarningEmployeeNames")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andReturn();
    }
    // #endregion


    // #region createEmployee
    @Test
    void createEmployee_returnsCreatedEmployee_whenSuccess() throws Exception {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("employee_name", employee1.getEmployeeName());
        employeeInput.put("employee_age", employee1.getEmployeeAge());
        employeeInput.put("employee_salary", employee1.getEmployeeSalary());

        when(mockEmployeeService.createEmployee(employeeInput)).thenReturn(employee1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee actualEmployee = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Employee.class);

        assertEquals(employee1, actualEmployee, "Created employee should match the expected employee");
    }

    @Test
    void createEmployee_returns422_whenIllegalArgumentExceptionOccurs() throws Exception {
        when(mockEmployeeService.createEmployee(Mockito.anyMap())).thenThrow(new IllegalArgumentException("Bad Data input"));

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Mockito.anyMap())))
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
            .andReturn();
    }

    @Test
    void createEmployee_returns500_whenNonIllegalArgumentExceptionOccurs() throws Exception {
        when(mockEmployeeService.createEmployee(Mockito.anyMap())).thenThrow(new RuntimeException("Mock Error occurred"));

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Mockito.anyMap())))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andReturn();
    }
    // #endregion

    // #region deleteEmployeeById
     @Test
     void deleteEmployeeById_returnsEmployee_whenSuccess() throws Exception {
         when(mockEmployeeService.deleteEmployeeById(employee1.getId())).thenReturn(employee1.getId());
 
         MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/{id}", employee1.getId())
             .contentType(MediaType.APPLICATION_JSON))
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andReturn();
 
         String content = result.getResponse().getContentAsString();
 
         assertThat(content).isNotNull().isEqualTo(employee1.getId());
     }
 
     @Test
     void deleteEmployeeById_returns500_whenExceptionOccurs() throws Exception {
         when(mockEmployeeService.deleteEmployeeById("123")).thenThrow(new RuntimeException("Error occurred"));
 
         mockMvc.perform(MockMvcRequestBuilders.delete("/{id}", "123")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                 .andReturn();
     }
    // #endregion
}

