package com.example.rqchallenge.employees.remoteDtos;

/**
 * For some fun reason the remote api returns a different dto from the create response than the other api methods.
 * So, an additional type was added to avoid having to write a custom object mapper
 */
public class CreatedEmployee {
    private String id;      
    private String name;
    private Integer age;
    private Integer salary;

    public CreatedEmployee() {
    }

    public CreatedEmployee(String id, String name, Integer age, Integer salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }
    
    public CreatedEmployee(String name, Integer age, Integer salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSalary() {
        return salary;
    }
    
    public void setSalary(Integer salary) {
        this.salary = salary;
    }     
    

}
