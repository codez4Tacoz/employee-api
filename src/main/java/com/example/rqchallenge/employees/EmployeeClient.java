package com.example.rqchallenge.employees;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.rqchallenge.employees.remoteDtos.BaseRemoteDto;
import com.example.rqchallenge.employees.remoteDtos.CreatedEmployee;

@FeignClient(name = "DummyRestApi", url = "https://dummy.restapiexample.com/api/v1")
public interface EmployeeClient {

    @GetMapping(value = "/employees/")
    BaseRemoteDto<List<Employee>> findAll();

    @GetMapping("/employee/{id}")
    BaseRemoteDto<Employee> getById(@PathVariable("id") String id);

    @PostMapping("/create")
    BaseRemoteDto<CreatedEmployee> create(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping("/delete/{id}")
    BaseRemoteDto<String> delete(@PathVariable("id") String id);
}
