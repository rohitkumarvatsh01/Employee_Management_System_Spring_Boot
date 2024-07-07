package com.employeemanagement.service;

import com.employeemanagement.exception.InvalidEmployeeDataException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
public class EmployeeExcelService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void createNewRecordFromExcel(String filePath) {
        try (FileInputStream excelFile = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip the header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Employee employee = new Employee();
                
                employee.setEmp_name(currentRow.getCell(0).getStringCellValue());
                employee.setEmp_salary((float) currentRow.getCell(1).getNumericCellValue());
                employee.setEmp_age((int) currentRow.getCell(2).getNumericCellValue());
                employee.setEmp_city(currentRow.getCell(3).getStringCellValue());

                employeeRepository.save(employee);
            }
        } catch (IOException e) {
            throw new InvalidEmployeeDataException("Employee Data is Not Correct Format");
        }
    }
}
