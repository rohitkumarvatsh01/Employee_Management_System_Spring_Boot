package com.employeemanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.employeemanagement.model.Employee;
import com.employeemanagement.service.EmployeeExcelService;
import com.employeemanagement.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    
    @Autowired
    private EmployeeExcelService employeeExcelService;
    
    //Create a new record in the table from fetching excel sheet.
    @PostMapping("/upload")
    public ResponseEntity<String> saveDataFromExcel(@RequestParam("file") MultipartFile file) {
        String result = employeeExcelService.saveDataFromExcel(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Create a new record in the table.
    @PostMapping("/post")
    public ResponseEntity<Employee> createNewRecord(@RequestBody Employee employee) {
        Employee result = employeeService.createNewRecord(employee);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // Retrieve all records from the database.
    @GetMapping("/get")
    public ResponseEntity<List<Employee>> getAllRecords() {
        return new ResponseEntity<>(employeeService.getAllRecords(), HttpStatus.OK);
    }

    // Retrieve a record from the database by ID.
    @GetMapping("/get/{empid}")
    public ResponseEntity<Employee>getRecordById(@PathVariable Long empid){
    	Employee result = employeeService.getRecordById(empid);
    	return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    // Update a record in the table by ID.
    @PutMapping("/put/{empid}")
    public ResponseEntity<Employee> updateById(@PathVariable long empid, @RequestBody Employee employee) {
        Employee result = employeeService.updateById(empid, employee);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Delete a record from the table by ID.
    @DeleteMapping("/delete/{empid}")
    public ResponseEntity<String> deleteById(@PathVariable long empid) {
        String result = employeeService.deleteById(empid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Delete all records from the table.
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllRecords() {
        String result = employeeService.deleteAllRecords();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
