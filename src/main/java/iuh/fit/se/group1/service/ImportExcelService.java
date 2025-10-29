package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Customer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImportExcelService {
    
    private final CustomerService customerService = new CustomerService();
    
    public List<Customer> importCustomersFromExcel(File file) {
        List<Customer> customers = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ dòng tiêu đề
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Customer c = new Customer();
                c.setFullName(getCellValue(row.getCell(1)));
                c.setGender("Nam".equalsIgnoreCase(getCellValue(row.getCell(2))));
                c.setEmail(getCellValue(row.getCell(3)));
                c.setCitizenId(getCellValue(row.getCell(4)));
                c.setPhone(getCellValue(row.getCell(5)));
                c.setAddress(""); // Excel này chưa có địa chỉ, có thể để trống
                c.setDateOfBirth(LocalDate.now());

                Customer saved = customerService.createCustomer(c);
                if (saved != null) customers.add(saved);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
