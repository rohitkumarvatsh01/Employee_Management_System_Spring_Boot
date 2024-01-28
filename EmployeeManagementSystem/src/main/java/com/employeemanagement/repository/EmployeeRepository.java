package com.employeemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employeemanagement.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
