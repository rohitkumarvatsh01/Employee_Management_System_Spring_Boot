package com.employeemanagement.service;

import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.exception.InvalidEmployeeDataException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Create a new record in the table.    
    public Employee createNewRecord(Employee employee) {
    	if(employee.getEmp_name()!=null && employee.getEmp_age()>18 && employee.getEmp_salary()>999 && employee.getEmp_city()!=null) {
    		return employeeRepository.save(employee);
    	}
    	else {
    		throw new InvalidEmployeeDataException("Employee Data is Not Correct Format");
    	}
    }

    // Retrieve all records from the database.       
    public List<Employee>getAllRecords(){
    	List<Employee>list=employeeRepository.findAll();
    	
    	if(list.isEmpty()) {
    		throw new EmployeeNotFoundException("No Any Employee Record Found");
    	}
    	else {
    		return list;
    	}
    }

    // Retrieve a record from the database by ID.
    public Employee getRecordById(long empid){
    	Optional<Employee>optionalEmployee=employeeRepository.findById(empid);
    	if(optionalEmployee.isPresent()) {
    		return optionalEmployee.get();
    	}
    	else {
    		throw new EmployeeNotFoundException("Employee Not Found with Empid "+empid);
    	}
    }

    // Update a record in the table by ID.    
    public Employee updateById(long empid, Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empid);

        if (optionalEmployee.isPresent()) {
            Employee existEmp = optionalEmployee.get();
            existEmp.setEmp_name(employee.getEmp_name());
            existEmp.setEmp_salary(employee.getEmp_salary());
            existEmp.setEmp_age(employee.getEmp_age());
            existEmp.setEmp_city(employee.getEmp_city());
            return employeeRepository.save(existEmp);
        } else {
        	throw new EmployeeNotFoundException("Employee Not Found with Empid "+empid);
        }
    }

    // Delete a record from the table by ID.
    public String deleteById(long empid) {
    	Optional<Employee>optionalEmployee=employeeRepository.findById(empid);
        if(optionalEmployee.isPresent()) {
        	employeeRepository.deleteById(empid);
        	return "Employee empid "+empid+" is Deleted";
        }
        else {
        	throw new EmployeeNotFoundException("Employee Not Found with Empid "+empid);
        }
    }

    // Delete all records from the table.
    public String deleteAllRecords() {
    	List<Employee>list=employeeRepository.findAll();
    	if(list.isEmpty()) {
    		throw new EmployeeNotFoundException("No Any Employee Record Found");
    	}
    	else {
    		employeeRepository.deleteAll();
    		return "All Records are Deleted";
    	}
    }
}
