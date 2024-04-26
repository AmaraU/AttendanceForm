package com.example.attendance;

import java.time.LocalTime;
import java.util.List;

public interface EmployeeService {

    Employee findById(int theId);
    List<Employee> findAll();
    void save(Employee theEmployee);

    Employee saveEmployee(Employee theEmployee);

    void deleteEmployeeById(int theId);


}
