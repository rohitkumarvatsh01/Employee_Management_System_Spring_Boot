package com.employeemanagement.service;

import com.employeemanagement.model.Employee;
import com.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Create a new record in the table.
    public String createNewRecord(Employee employee) {
        employeeRepository.save(employee);
        return "New Record is Created";
    }

    // Retrieve all records from the database.
    public ResponseEntity<List<Employee>> getAllRecords() {
        List<Employee> empList = new ArrayList<>();
        employeeRepository.findAll().forEach(empList::add);
        return new ResponseEntity<>(empList, HttpStatus.OK);
    }

    // Retrieve a record from the database by ID.
    public ResponseEntity<Employee> getRecordById(long empid) {
        Optional<Employee> emp = employeeRepository.findById(empid);
        if (emp.isPresent()) {
            return new ResponseEntity<>(emp.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a record in the table by ID.
    public String updateById(long empid, Employee employee) {
        Optional<Employee> emp = employeeRepository.findById(empid);

        if (emp.isPresent()) {
            Employee existEmp = emp.get();
            existEmp.setEmp_name(employee.getEmp_name());
            existEmp.setEmp_salary(employee.getEmp_salary());
            existEmp.setEmp_age(employee.getEmp_age());
            existEmp.setEmp_city(employee.getEmp_city());
            employeeRepository.save(existEmp);
            return "Employee EmpId " + empid + " is Updated Successfully";
        } else {
            return "Employee EmpId " + empid + " is Not Found";
        }
    }

    // Delete a record from the table by ID.
    public String deleteById(long empid) {
        if (employeeRepository.existsById(empid)) {
            employeeRepository.deleteById(empid);
            return "EmpId " + empid + " is Deleted Successfully";
        } else {
            return "Employee EmpId " + empid + " is Not Found";
        }
    }

    // Delete all records from the table.
    public String deleteAllRecords() {
        employeeRepository.deleteAll();
        return "All Records are Deleted";
    }
}
