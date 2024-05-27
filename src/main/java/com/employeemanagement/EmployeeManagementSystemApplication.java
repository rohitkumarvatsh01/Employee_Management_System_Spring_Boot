package com.employeemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementSystemApplication.class, args);
	}
}

//Table->
//USE Project;
//CREATE TABLE Employee(
//empid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
//emp_name VARCHAR(30) DEFAULT NULL,
//emp_salary FLOAT DEFAULT NULL,
//emp_age INT DEFAULT NULL,
//emp_city VARCHAR(30) DEFAULT NULL
//);
//SELECT * FROM Employee;
