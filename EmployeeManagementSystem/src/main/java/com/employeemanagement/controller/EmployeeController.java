package com.employeemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagement.model.Employee;
import com.employeemanagement.service.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Create a new record in the table.
    @PostMapping("/post")
    public String createNewRecord(@RequestBody Employee employee) {
        return employeeService.createNewRecord(employee);
    }

    // Retrieve all records from the database.
    @GetMapping("/get")
    public ResponseEntity<List<Employee>> getAllRecords() {
        return employeeService.getAllRecords();
    }

    // Retrieve a record from the database by ID.
    @GetMapping("/get/{empid}")
    public ResponseEntity<Employee> getRecordById(@PathVariable long empid) {
        return employeeService.getRecordById(empid);
    }

    // Update a record in the table by ID.
    @PutMapping("/put/{empid}")
    public String updateById(@PathVariable long empid, @RequestBody Employee employee) {
        return employeeService.updateById(empid, employee);
    }

    // Delete a record from the table by ID.
    @DeleteMapping("/delete/{empid}")
    public String deleteById(@PathVariable long empid) {
        return employeeService.deleteById(empid);
    }

    // Delete all records from the table.
    @DeleteMapping("/delete")
    public String deleteAllRecords() {
        return employeeService.deleteAllRecords();
    }
}
