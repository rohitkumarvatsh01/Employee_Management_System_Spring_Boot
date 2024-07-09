package com.employeemanagement.service;

import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.exception.InvalidEmployeeDataException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.repository.EmployeeRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    // Create a new record in the table.
    public Employee createNewRecord(Employee employee) {
        validateEmployeeData(employee);
        logger.info("Adding new employee: {}", employee);
        return employeeRepository.save(employee);
    }

    // Validate employee data
    private void validateEmployeeData(Employee employee) {
        if (employee.getEmp_name() == null || employee.getEmp_name().isEmpty()) {
            logger.warn("Invalid employee name: {}", employee.getEmp_name());
            throw new InvalidEmployeeDataException("Employee name is invalid");
        }
        if (employee.getEmp_age() <= 18) {
            logger.warn("Invalid employee age: {}", employee.getEmp_age());
            throw new InvalidEmployeeDataException("Employee age must be greater than 18");
        }
        if (employee.getEmp_salary() <= 999) {
            logger.warn("Invalid employee salary: {}", employee.getEmp_salary());
            throw new InvalidEmployeeDataException("Employee salary must be greater than 999");
        }
        if (employee.getEmp_city() == null || employee.getEmp_city().isEmpty()) {
            logger.warn("Invalid employee city: {}", employee.getEmp_city());
            throw new InvalidEmployeeDataException("Employee city is invalid");
        }
    }


    // Retrieve all records from the database.       
    public List<Employee> getAllRecords() {
        List<Employee> list = employeeRepository.findAll();
        
        if (list.isEmpty()) {
            logger.info("No employee records found");
            throw new EmployeeNotFoundException("No employee records found");
        } else {
            logger.info("Retrieved all employee records");
            return list;
        }
    }

    // Retrieve a record from the database by ID.
    public Employee getRecordById(long empid) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empid);
        
        if (optionalEmployee.isPresent()) {
            logger.info("Employee found with ID: {}", empid);
            return optionalEmployee.get();
        } else {
            logger.warn("Employee not found with ID: {}", empid);
            throw new EmployeeNotFoundException("Employee not found with ID " + empid);
        }
    }

    // Update a record in the table by ID.    
    public Employee updateById(long empid, Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empid);

        if (optionalEmployee.isPresent()) {
            Employee existEmp = optionalEmployee.get();
            existEmp.setEmp_name(employee.getEmp_name());
            existEmp.setEmp_age(employee.getEmp_age());
            existEmp.setEmp_salary(employee.getEmp_salary());
            existEmp.setEmp_city(employee.getEmp_city());
            logger.info("Updated employee with ID: {}", empid);
            return employeeRepository.save(existEmp);
        } else {
            logger.warn("Employee not found with ID: {}", empid);
            throw new EmployeeNotFoundException("Employee not found with ID " + empid);
        }
    }

    // Delete a record from the table by ID.
    public String deleteById(long empid) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empid);
        if (optionalEmployee.isPresent()) {
            employeeRepository.deleteById(empid);
            logger.info("Deleted employee with ID: {}", empid);
            return "Employee with ID " + empid + " is deleted";
        } else {
            logger.warn("Employee not found with ID: {}", empid);
            throw new EmployeeNotFoundException("Employee not found with ID " + empid);
        }
    }

    // Delete all records from the table.
    public String deleteAllRecords() {
        List<Employee> list = employeeRepository.findAll();
        if (list.isEmpty()) {
            logger.info("No employee records found to delete");
            throw new EmployeeNotFoundException("No employee records found to delete");
        } else {
            employeeRepository.deleteAll();
            logger.info("Deleted all employee records");
            return "All employee records are deleted";
        }
    }
}
