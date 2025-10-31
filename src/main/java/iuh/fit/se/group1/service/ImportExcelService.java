package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.enums.Role;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ImportExcelService {

    private final CustomerService customerService = new CustomerService();
    private final AmenityService amenityService = new AmenityService();
    PromotionService promotionService = new PromotionService();

    public List<Customer> importCustomersFromExcel(File file) {
        List<Customer> customers = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return customers;
            }

            Row header = sheet.getRow(0);
            int startCol = 0;
            if (header != null && header.getCell(0) != null) {
                String firstHeader = header.getCell(0).getStringCellValue().trim();
                if (firstHeader.equalsIgnoreCase("STT")) {
                    startCol = 1;
                }
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String fullName = getCellValue(row.getCell(startCol + 1));
                if (fullName.isEmpty()) {
                    continue;
                }

                Customer c = new Customer();
                c.setFullName(fullName);
                c.setGender("Nam".equalsIgnoreCase(getCellValue(row.getCell(startCol + 2))));
                c.setEmail(getCellValue(row.getCell(startCol + 3)));
                c.setCitizenId(getCellValue(row.getCell(startCol + 4)));
                c.setPhone(getCellValue(row.getCell(startCol + 5)));
                c.setDateOfBirth(LocalDate.now());

                Customer saved = customerService.createCustomer(c);
                if (saved != null) {
                    customers.add(saved);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public List<Amenity> importAmenitiesFromExcel(File file) {
        List<Amenity> amenities = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return amenities;
            }

            Row header = sheet.getRow(0);
            int startCol = 0;
            if (header != null && header.getCell(0) != null) {
                String firstHeader = header.getCell(0).getStringCellValue().trim();
                if (firstHeader.equalsIgnoreCase("STT")) {
                    startCol = 1;
                }
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String maDVStr = getCellValue(row.getCell(startCol));
                String tenDV = getCellValue(row.getCell(startCol + 1));
                String giaStr = getCellValue(row.getCell(startCol + 2));

                if (tenDV.isEmpty()) {
                    continue;
                }
                BigDecimal giaDV = BigDecimal.ZERO;
                try {
                    giaDV = new BigDecimal(giaStr);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng giá tại dòng " + (i + 1) + ": " + giaStr);
                }

                Amenity a = new Amenity();
                try {
                    if (!maDVStr.isEmpty()) {
                        a.setAmenityId(Long.parseLong(maDVStr));
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Mã dịch vụ không hợp lệ tại dòng " + (i + 1));
                }

                a.setNameAmenity(tenDV);
                a.setPrice(giaDV);

                Amenity saved = amenityService.createAmenity(a);
                if (saved != null) {
                    amenities.add(saved);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return amenities;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING ->
                cell.getStringCellValue().trim();
            case NUMERIC -> {
                double val = cell.getNumericCellValue();
                if (val == Math.floor(val)) {
                    yield String.valueOf((long) val);
                } else {
                    yield String.valueOf(val);
                }
            }
            case BOOLEAN ->
                String.valueOf(cell.getBooleanCellValue());
            default ->
                "";
        };
    }

    public List<Promotion> importPromotionsFromExcel(File file) {
        List<Promotion> promotions = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return promotions;
            }

            Row header = sheet.getRow(0);
            boolean hasSttColumn = false;

            if (header != null && header.getCell(0) != null) {
                String firstHeader = getCellValuePromotion(header.getCell(0));
                hasSttColumn = firstHeader.equalsIgnoreCase("STT");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                if (i == 1 && row.getCell(0) != null) {
                    String first = getCellValuePromotion(row.getCell(0));
                    if (first.toLowerCase().contains("mã") || first.toLowerCase().contains("tên")) {
                        continue; 
                    }
                }

                int base = hasSttColumn ? 1 : 0;

                
                String maKMStr = getCellValuePromotion(row.getCell(base));
                String tenKM = getCellValuePromotion(row.getCell(base + 1));
                String giaKMStr = cleanNumberString(getCellValuePromotion(row.getCell(base + 2)));
                String giamPhanTramStr = cleanNumberString(getCellValuePromotion(row.getCell(base + 3)));
                String ngayBDStr = getCellValuePromotion(row.getCell(base + 4));
                String ngayKTStr = getCellValuePromotion(row.getCell(base + 5));
                String ngayTaoStr = getCellValuePromotion(row.getCell(base + 6));
                System.out.printf("Row %d: %s | %s | %s | %s | %s | %s | %s%n",
                        i, maKMStr, tenKM, giaKMStr, giamPhanTramStr, ngayBDStr, ngayKTStr, ngayTaoStr);

                if (tenKM.isEmpty()) {
                    continue;
                }

                Promotion p = new Promotion();
                p.setDescription("Không có mô tả");
                try {
                    if (!maKMStr.isEmpty()) {
                        p.setPromotionId(Long.parseLong(maKMStr));
                    }
                } catch (NumberFormatException ignored) {
                }

                p.setPromotionName(tenKM);

                try {
                    p.setDiscountPrice(giaKMStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(giaKMStr));
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng giá tại dòng " + (i + 1) + ": " + giaKMStr);
                    p.setDiscountPrice(BigDecimal.ZERO);
                }

                try {
                    p.setDiscountPercent(giamPhanTramStr.isEmpty() ? 0f : Float.parseFloat(giamPhanTramStr));
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi phần trăm tại dòng " + (i + 1) + ": " + giamPhanTramStr);
                    p.setDiscountPercent(0f);
                }

                try {
                    if (!ngayBDStr.isEmpty()) {
                        p.setStartDate(LocalDate.parse(ngayBDStr, dateFormatter));
                    }
                } catch (Exception ignored) {
                }
                try {
                    if (!ngayKTStr.isEmpty()) {
                        p.setEndDate(LocalDate.parse(ngayKTStr, dateFormatter));
                    }
                } catch (Exception ignored) {
                }
                try {
                    if (!ngayTaoStr.isEmpty()) {
                        p.setCreatedAt(LocalDate.parse(ngayTaoStr, dateFormatter));
                    } else {
                        p.setCreatedAt(LocalDate.now());
                    }
                } catch (Exception e) {
                    p.setCreatedAt(LocalDate.now());
                }

                Promotion saved = promotionService.createPromotion(p);
                if (saved != null) {
                    promotions.add(saved);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return promotions;
    }

    private String getCellValuePromotion(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING ->
                cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
                    yield date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                } else {
                    double val = cell.getNumericCellValue();
                    if (val == Math.floor(val)) {
                        yield String.valueOf((long) val);
                    } else {
                        yield String.valueOf(val);
                    }
                }
            }
            case BOOLEAN ->
                String.valueOf(cell.getBooleanCellValue());
            default ->
                "";
        };
    }

    private String cleanNumberString(String str) {
        if (str == null) {
            return "";
        }
        str = str.replaceAll("[₫%]", "")
                .replaceAll("[\\s\u00A0]", "")
                .replace(",", "."); 

        if (str.matches(".*\\.\\d{3}($|\\D).*")) {
            str = str.replace(".", "");
        }
        return str.trim();
    }
    
public List<Employee> importEmployeesFromExcel(File file) {
    List<Employee> employees = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            return employees;
        }

        Row header = sheet.getRow(0);
        boolean hasSttColumn = false;
        if (header != null && header.getCell(0) != null) {
            String firstHeader = header.getCell(0).getStringCellValue().trim();
            if (firstHeader.equalsIgnoreCase("STT")) {
                hasSttColumn = true;
            }
        }

        int startCol = hasSttColumn ? 1 : 0; 

        EmployeeService employeeService = new EmployeeService();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String employeeCodeStr = getCellValue(row.getCell(startCol));         
            String fullName        = getCellValue(row.getCell(startCol + 1));      
            String genderStr       = getCellValue(row.getCell(startCol + 2));     
            String roleName        = getCellValue(row.getCell(startCol + 3));    
            String phone           = getCellValue(row.getCell(startCol + 4));      

            if (fullName.isEmpty() || phone.isEmpty()) continue;

            Long employeeId = null;
            try {
                if (!employeeCodeStr.isEmpty()) {
                    employeeId = Long.parseLong(employeeCodeStr.replaceAll("\\D", ""));
                }
            } catch (NumberFormatException ex) {
                System.err.println("⚠️ Mã nhân viên không hợp lệ ở dòng " + (i + 1) + ": " + employeeCodeStr);
            }

            Employee e = new Employee();
            if (employeeId != null) {
                e.setEmployeeId(employeeId);
            }
            e.setFullName(fullName);
            e.setGender("Nữ".equalsIgnoreCase(genderStr));
            e.setPhone(phone);
            e.setHireDate(LocalDate.now());
            e.setEmail("");
            e.setCitizenId("");

            String roleId = Role.RECEPTIONIST.toString();
            if (roleName.equalsIgnoreCase("Quản lý") || roleName.equalsIgnoreCase("Manager")) {
                roleId = Role.MANAGER.toString();
            }

            Employee saved = employeeService.createEmployee(e, roleId);
            if (saved != null) {
                employees.add(saved);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return employees;
}



}
