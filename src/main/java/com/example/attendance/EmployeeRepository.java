package com.example.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
//    public void updateTimeOut(Employee theEmployee);

}
