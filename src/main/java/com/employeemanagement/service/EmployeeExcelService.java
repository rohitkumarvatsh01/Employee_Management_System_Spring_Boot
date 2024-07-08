package com.employeemanagement.service;

import com.employeemanagement.exception.InvalidEmployeeDataException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.repository.EmployeeRepository;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class EmployeeExcelService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmployeeExcelService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final int THREAD_POOL_SIZE = 10;

    public String saveDataFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            logger.warn("Uploaded file is empty");
            throw new InvalidEmployeeDataException("Uploaded file is empty");
        }

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = getWorkbook(file, is);

            Sheet sheet = workbook.getSheetAt(0);

            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            List<Future<Void>> futures = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip header row
                    continue;
                }

                Callable<Void> task = () -> {
                    Employee employee = new Employee();
                    employee.setEmp_name(getCellValueAsString(row.getCell(0)));
                    employee.setEmp_age(getCellValueAsInt(row.getCell(1)));
                    employee.setEmp_salary(getCellValueAsFloat(row.getCell(2)));
                    employee.setEmp_city(getCellValueAsString(row.getCell(3)));

                    validateEmployeeData(employee);

                    employeeRepository.save(employee);
                    logger.info("Employee data added: {}", employee);
                    return null;
                };

                futures.add(executorService.submit(task));
            }

            for (Future<Void> future : futures) {
                try {
                    future.get(); // Wait for task completion
                } catch (Exception e) {
                    logger.warn("Failed to process row: {}", e.getMessage());
                }
            }

            executorService.shutdown();
            return "All employee data has been added";
        } catch (InvalidEmployeeDataException e) {
            logger.warn("Invalid employee data: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.warn("Failed to parse Excel file: {}", e.getMessage());
            throw new InvalidEmployeeDataException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    private void validateEmployeeData(Employee employee) {
        if (employee.getEmp_name() != null || employee.getEmp_age() > 18 || employee.getEmp_salary() > 999 || employee.getEmp_city() != null) {
            throw new InvalidEmployeeDataException("Employee data validation failed: " + employee);
        }
    }

    private Workbook getWorkbook(MultipartFile file, InputStream is) throws Exception {
        if (file.getOriginalFilename().endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        } else if (file.getOriginalFilename().endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else {
            throw new InvalidEmployeeDataException("The specified file is not an Excel file");
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private float getCellValueAsFloat(Cell cell) {
        if (cell == null) {
            return 0.0f;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return (float) cell.getNumericCellValue();
            case STRING:
                try {
                    return Float.parseFloat(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0.0f;
                }
            case BOOLEAN:
                return cell.getBooleanCellValue() ? 1.0f : 0.0f;
            case FORMULA:
                return (float) cell.getNumericCellValue();
            case BLANK:
                return 0.0f;
            default:
                return 0.0f;
        }
    }

    private int getCellValueAsInt(Cell cell) {
        if (cell == null) {
            return 0;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            case BOOLEAN:
                return cell.getBooleanCellValue() ? 1 : 0;
            case FORMULA:
                return (int) cell.getNumericCellValue();
            case BLANK:
                return 0;
            default:
                return 0;
        }
    }
}