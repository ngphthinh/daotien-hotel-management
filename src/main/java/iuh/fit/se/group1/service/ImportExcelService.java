package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.enums.RoomStatus;
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
    RoomService roomService = new RoomService();

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

            Row headerRow = sheet.getRow(0);
            int startColumn = 0;
            if (headerRow != null && headerRow.getCell(0) != null) {
                String firstHeaderValue = headerRow.getCell(0).getStringCellValue().trim();
                if (firstHeaderValue.equalsIgnoreCase("STT")) {
                    startColumn = 1;
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String amenityIdStr = getCellValue(row.getCell(startColumn));
                String amenityName = getCellValue(row.getCell(startColumn + 1));
                String priceStr = getCellValue(row.getCell(startColumn + 2));

                if (amenityName.isEmpty()) {
                    continue;
                }

                BigDecimal amenityPrice = BigDecimal.ZERO;
                try {
                    amenityPrice = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format at row " + (rowIndex + 1) + ": " + priceStr);
                }

                Amenity amenity = new Amenity();
                try {
                    if (!amenityIdStr.isEmpty()) {
                        amenity.setAmenityId(Long.parseLong(amenityIdStr));
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid amenity ID at row " + (rowIndex + 1));
                }

                amenity.setNameAmenity(amenityName);
                amenity.setPrice(amenityPrice);

                Amenity savedAmenity = amenityService.createAmenity(amenity);
                if (savedAmenity != null) {
                    amenities.add(savedAmenity);
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

            Row headerRow = sheet.getRow(0);
            boolean hasIndexColumn = false;

            if (headerRow != null && headerRow.getCell(0) != null) {
                String firstHeaderValue = getCellValuePromotion(headerRow.getCell(0));
                hasIndexColumn = firstHeaderValue.equalsIgnoreCase("STT");
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                if (rowIndex == 1 && row.getCell(0) != null) {
                    String firstValue = getCellValuePromotion(row.getCell(0));
                    if (firstValue.toLowerCase().contains("mã") || firstValue.toLowerCase().contains("tên")) {
                        continue;
                    }
                }

                int baseColumn = hasIndexColumn ? 1 : 0;

                String promotionIdStr = getCellValuePromotion(row.getCell(baseColumn));
                String promotionName = getCellValuePromotion(row.getCell(baseColumn + 1));
                String discountPriceStr = cleanNumberString(getCellValuePromotion(row.getCell(baseColumn + 2)));
                String discountPercentStr = cleanNumberString(getCellValuePromotion(row.getCell(baseColumn + 3)));
                String startDateStr = getCellValuePromotion(row.getCell(baseColumn + 4));
                String endDateStr = getCellValuePromotion(row.getCell(baseColumn + 5));
                String createdDateStr = getCellValuePromotion(row.getCell(baseColumn + 6));

                System.out.printf("Row %d: %s | %s | %s | %s | %s | %s | %s%n",
                        rowIndex, promotionIdStr, promotionName, discountPriceStr,
                        discountPercentStr, startDateStr, endDateStr, createdDateStr);

                if (promotionName.isEmpty()) {
                    continue;
                }

                Promotion promotion = new Promotion();
                promotion.setDescription("Không có mô tả");

                try {
                    if (!promotionIdStr.isEmpty()) {
                        promotion.setPromotionId(Long.parseLong(promotionIdStr));
                    }
                } catch (NumberFormatException ignored) {
                }

                promotion.setPromotionName(promotionName);

                try {
                    promotion.setMinOrderAmount(discountPriceStr.isEmpty()
                            ? BigDecimal.ZERO
                            : new BigDecimal(discountPriceStr));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price at row " + (rowIndex + 1) + ": " + discountPriceStr);
                    promotion.setMinOrderAmount(BigDecimal.ZERO);
                }

                try {
                    promotion.setDiscountPercent(discountPercentStr.isEmpty()
                            ? 0f
                            : Float.parseFloat(discountPercentStr));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid percent at row " + (rowIndex + 1) + ": " + discountPercentStr);
                    promotion.setDiscountPercent(0f);
                }

                try {
                    if (!startDateStr.isEmpty()) {
                        promotion.setStartDate(LocalDate.parse(startDateStr, dateFormatter));
                    }
                } catch (Exception ignored) {
                }

                try {
                    if (!endDateStr.isEmpty()) {
                        promotion.setEndDate(LocalDate.parse(endDateStr, dateFormatter));
                    }
                } catch (Exception ignored) {
                }

                try {
                    if (!createdDateStr.isEmpty()) {
                        promotion.setCreatedAt(LocalDate.parse(createdDateStr, dateFormatter));
                    } else {
                        promotion.setCreatedAt(LocalDate.now());
                    }
                } catch (Exception e) {
                    promotion.setCreatedAt(LocalDate.now());
                }

                Promotion savedPromotion = promotionService.createPromotion(promotion);
                if (savedPromotion != null) {
                    promotions.add(savedPromotion);
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
                if (row == null) {
                    continue;
                }

                String employeeCodeStr = getCellValue(row.getCell(startCol));
                String fullName = getCellValue(row.getCell(startCol + 1));
                String genderStr = getCellValue(row.getCell(startCol + 2));
                String roleName = getCellValue(row.getCell(startCol + 3));
                String phone = getCellValue(row.getCell(startCol + 4));

                if (fullName.isEmpty() || phone.isEmpty()) {
                    continue;
                }

                Long employeeId = null;
                try {
                    if (!employeeCodeStr.isEmpty()) {
                        employeeId = Long.parseLong(employeeCodeStr.replaceAll("\\D", ""));
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("️Mã nhân viên không hợp lệ ở dòng " + (i + 1) + ": " + employeeCodeStr);
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

    private RoomTypeService roomTypeService = new RoomTypeService();

    public List<Room> importRoomsFromExcel(File file) {
        List<Room> rooms = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return rooms;
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String roomNumber = getCellValueRoom(row.getCell(2));
                String roomTypeName = getCellValueRoom(row.getCell(3));
                String roomStatusStr = getCellValueRoom(row.getCell(4));

                roomTypeName = java.text.Normalizer.normalize(roomTypeName, java.text.Normalizer.Form.NFC).trim();

                String roomTypeId = switch (roomTypeName) {
                    case "Phòng đơn" ->
                        "SINGLE";
                    case "Phòng đôi" ->
                        "DOUBLE";
                    default ->
                        "SINGLE";
                };

                RoomType roomType = roomTypeService.getRoomTypeById(roomTypeId);
                if (roomType == null) {
                    roomType = new RoomType();
                    roomType.setRoomTypeId(roomTypeId);
                    roomType.setName(roomTypeName);
                    roomType = roomTypeService.createRoomType(roomType);
                    System.out.println("ℹ️ Tạo RoomType mới: " + roomTypeName);
                }

                Room room = new Room();
                room.setRoomNumber(roomNumber);
                room.setRoomType(roomType);

                RoomStatus status;
                String st = roomStatusStr.trim().toUpperCase();

                switch (st) {
                    case "CÓ SẴN", "AVAILABLE" ->
                        status = RoomStatus.AVAILABLE;
                    case "ĐANG SỬ DỤNG", "OCCUPIED" ->
                        status = RoomStatus.OCCUPIED;
                    case "BẢO TRÌ", "OUT_OF_ORDER" ->
                        status = RoomStatus.OUT_OF_ORDER;
                    default -> {
                        System.err.println("Trạng thái không hợp lệ dòng " + (i + 1) + ": " + roomStatusStr);
                        status = RoomStatus.AVAILABLE;
                    }
                }
                room.setRoomStatus(status);

                room.setCreatedAt(LocalDate.now());

                Room savedRoom = roomService.createRoom(room);
                if (savedRoom != null) {
                    rooms.add(savedRoom);
                    System.out.println("Thêm phòng: " + roomNumber + " - " + roomTypeName + " - " + status);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }

    private String getCellValueRoom(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING ->
                cell.getStringCellValue().trim();
            case NUMERIC ->
                String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN ->
                String.valueOf(cell.getBooleanCellValue());
            default ->
                "";
        };
    }
    SurchargeService surchargeService = new SurchargeService();

    public List<Surcharge> importSurchargesFromExcel(File file) {
        List<Surcharge> surcharges = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return surcharges;
            }

            Row headerRow = sheet.getRow(0);
            int startColumn = 0;

            if (headerRow != null && headerRow.getCell(0) != null) {
                String firstHeaderValue = headerRow.getCell(0).getStringCellValue().trim();
                if (firstHeaderValue.equalsIgnoreCase("STT")) {
                    startColumn = 1;
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String surchargeIdStr = getCellValue(row.getCell(startColumn));
                String surchargeName = getCellValue(row.getCell(startColumn + 1));
                String priceStr = getCellValue(row.getCell(startColumn + 2));

                if (surchargeName.isEmpty()) {
                    continue;
                }

                BigDecimal price = BigDecimal.ZERO;
                try {
                    price = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format at row " + (rowIndex + 1) + ": " + priceStr);
                }

                Surcharge surcharge = new Surcharge();

                try {
                    if (!surchargeIdStr.isEmpty()) {
                        surcharge.setSurchargeId(Long.parseLong(surchargeIdStr));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid surcharge ID at row " + (rowIndex + 1));
                }

                surcharge.setName(surchargeName);
                surcharge.setPrice(price);

                Surcharge savedSurcharge = surchargeService.createSurcharge(surcharge);
                if (savedSurcharge != null) {
                    surcharges.add(savedSurcharge);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return surcharges;
    }

}
