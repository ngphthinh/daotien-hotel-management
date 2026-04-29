package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.enums.RoomStatus;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ImportExcelService {

    private static final Logger log = LoggerFactory.getLogger(ImportExcelService.class);
    private final CustomerService customerService = new CustomerService();
    private final AmenityService amenityService = new AmenityService();
    private final PromotionService promotionService = new PromotionService();
    private final RoomService roomService = new RoomService();
    private final RoomTypeService roomTypeService = new RoomTypeService();
    private final SurchargeService surchargeService = new SurchargeService();
    private final EmployeeService employeeService = new EmployeeService();

    public List<CustomerDTO> importCustomersFromExcel(File file) {
        List<CustomerDTO> customers = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return customers;

            Row header = sheet.getRow(0);
            int startCol = 0;

            if (header != null && header.getCell(0) != null) {
                String firstHeader = getCellValue(header.getCell(0));
                if ("STT".equalsIgnoreCase(firstHeader)) {
                    startCol = 1;
                }
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String fullName = getCellValue(row.getCell(startCol + 0));
                String genderStr = getCellValue(row.getCell(startCol + 1));
                String email = getCellValue(row.getCell(startCol + 2));
                String citizenId = getCellValue(row.getCell(startCol + 3));
                String phone = getCellValue(row.getCell(startCol + 4));
                String dobStr = getCellValue(row.getCell(startCol + 5));

                if (fullName.isEmpty()) continue;

                CustomerDTO c = new CustomerDTO();
                c.setCustomerId(null);
                c.setFullName(fullName);
                c.setGender(!"Nam".equalsIgnoreCase(genderStr));
                c.setEmail(email);
                c.setCitizenId(citizenId.replace("'", ""));
                c.setPhone(phone.replace("'", ""));

                try {
                    if (!dobStr.isEmpty()) {
                        c.setDateOfBirth(LocalDate.parse(dobStr, formatter));
                    } else {
                        c.setDateOfBirth(LocalDate.now());
                    }
                } catch (Exception e) {
                    c.setDateOfBirth(LocalDate.now());
                }

                customers.add(c);
            }

            return customerService.createCustomers(customers);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    public List<AmenityDTO> importAmenitiesFromExcel(File file) {
        List<AmenityDTO> amenities = new ArrayList<>();
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
                String priceStr = cleanNumberString(getCellValue(row.getCell(startColumn + 2)));

                if (amenityName.isEmpty()) {
                    continue;
                }

                BigDecimal amenityPrice = BigDecimal.ZERO;
                try {
                    amenityPrice = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format at row " + (rowIndex + 1) + ": " + priceStr);
                }

                AmenityDTO amenity = new AmenityDTO();
                try {
                    if (!amenityIdStr.isEmpty()) {
                        amenity.setAmenityId(Long.parseLong(amenityIdStr));
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid amenity ID at row " + (rowIndex + 1));
                }

                amenity.setNameAmenity(amenityName);
                amenity.setPrice(amenityPrice);

                amenity.setAmenityId(null);
                amenities.add(amenity);
            }

            return amenityService.createAmenities(amenities);
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
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double val = cell.getNumericCellValue();
                if (val == Math.floor(val)) {
                    yield String.valueOf((long) val);
                } else {
                    yield String.valueOf(val);
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public List<PromotionDTO> importPromotionsFromExcel(File file) {
        List<PromotionDTO> promotions = new ArrayList<>();
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

                PromotionDTO promotion = new PromotionDTO();
                promotion.setDescription("Không có mô tả");

                promotion.setPromotionId(null);

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

                promotions.add(promotion);
            }

            return promotionService.createPromotions(promotions);

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
            case STRING -> cell.getStringCellValue().trim();
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
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private String cleanNumberString(String str) {
        if (str == null) {
            return "";

        }

        str = str.split(" ")[0];

        str = str.replaceAll("[₫%]", "")
                .replaceAll("[\\s\u00A0]", "")
                .replace(",", ".");

        if (str.matches(".*\\.\\d{3}($|\\D).*")) {
            str = str.replace(".", "");
        }
        return str.trim();
    }

    public List<EmployeeDTO> importEmployeesFromExcel(File file) {
        Map<EmployeeDTO, String> employeeRoleMap = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return new ArrayList<>();

            Row header = sheet.getRow(0);
            boolean hasSttColumn = false;

            if (header != null && header.getCell(0) != null) {
                String firstHeader = getCellValue(header.getCell(0));
                if ("STT".equalsIgnoreCase(firstHeader)) {
                    hasSttColumn = true;
                }
            }

            int startCol = hasSttColumn ? 1 : 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String fullName = getCellValue(row.getCell(startCol + 0));
                String genderStr = getCellValue(row.getCell(startCol + 1));
                String email = getCellValue(row.getCell(startCol + 2));
                String citizenId = getCellValue(row.getCell(startCol + 3));
                String phone = getCellValue(row.getCell(startCol + 4));
                String hireDateStr = getCellValue(row.getCell(startCol + 5));
                String roleName = getCellValue(row.getCell(startCol + 6));

                if (fullName.isEmpty() || phone.isEmpty()) continue;

                EmployeeDTO e = new EmployeeDTO();
                e.setEmployeeId(null); // luôn insert mới
                e.setFullName(fullName);
                e.setGender(!"Nam".equalsIgnoreCase(genderStr));
                e.setEmail(email);
                e.setCitizenId(citizenId.replace("'", ""));
                e.setPhone(phone.replace("'", ""));

                // parse ngày
                try {
                    if (!hireDateStr.isEmpty()) {
                        e.setHireDate(LocalDate.parse(hireDateStr, formatter));
                    } else {
                        e.setHireDate(LocalDate.now());
                    }
                } catch (Exception ex) {
                    e.setHireDate(LocalDate.now());
                }

                // role mapping
                String roleId = Role.RECEPTIONIST.name();
                if (roleName.toLowerCase().contains("quản lý")  || roleName.toLowerCase().contains("quản lí") || roleName.toLowerCase().contains("manager")) {
                    roleId = Role.MANAGER.name();
                }

                employeeRoleMap.put(e, roleId);

            }
            return employeeService.createEmployees(employeeRoleMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import employees from Excel", e);
        }
    }


    public List<RoomViewDTO> importRoomsFromExcel(File file) {
        List<RoomViewDTO> rooms = new ArrayList<>();

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
                    case "Phòng đơn" -> "SINGLE";
                    case "Phòng đôi" -> "DOUBLE";
                    default -> "SINGLE";
                };

                RoomTypeDTO roomType = roomTypeService.getRoomTypeById(roomTypeId);
                if (roomType == null) {
                    roomType = new RoomTypeDTO();
                    roomType.setRoomTypeId(roomTypeId);
                    roomType.setName(roomTypeName);
                    roomType = roomTypeService.createRoomType(roomType);
                    System.out.println("ℹ️ Tạo RoomType mới: " + roomTypeName);
                }

                RoomViewDTO room = new RoomViewDTO();
                room.setRoomNumber(roomNumber);
                room.setRoomType(roomType);

                RoomStatus status;
                String st = roomStatusStr.trim().toUpperCase();

                switch (st) {
                    case "CÓ SẴN", "AVAILABLE" -> status = RoomStatus.AVAILABLE;
                    case "ĐANG SỬ DỤNG", "OCCUPIED" -> status = RoomStatus.OCCUPIED;
                    case "BẢO TRÌ", "OUT_OF_ORDER" -> status = RoomStatus.OUT_OF_ORDER;
                    default -> {
                        System.err.println("Trạng thái không hợp lệ dòng " + (i + 1) + ": " + roomStatusStr);
                        status = RoomStatus.AVAILABLE;
                    }
                }
                room.setRoomStatus(status);


                room.setRoomId(null);
                rooms.add(room);
            }

            return roomService.createRooms(rooms);
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
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }


    public List<SurchargeDTO> importSurchargesFromExcel(File file) {
        List<SurchargeDTO> surcharges = new ArrayList<>();

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
                String priceStr = cleanNumberString(getCellValue(row.getCell(startColumn + 2)));

                if (surchargeName.isEmpty()) {
                    continue;
                }

                BigDecimal price = BigDecimal.ZERO;
                try {
                    price = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format at row " + (rowIndex + 1) + ": " + priceStr);
                }

                SurchargeDTO surcharge = new SurchargeDTO();

                try {
                    if (!surchargeIdStr.isEmpty()) {
                        surcharge.setSurchargeId(Long.parseLong(surchargeIdStr));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid surcharge ID at row " + (rowIndex + 1));
                }

                surcharge.setName(surchargeName);
                surcharge.setPrice(price);

                surcharge.setSurchargeId(null);
                surcharges.add(surcharge);
            }
            return surchargeService.createSurcharges(surcharges);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return surcharges;
    }

}