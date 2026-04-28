package iuh.fit.se.group1.service;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Service class để xuất dữ liệu từ JTable ra file Excel
 * Có thể tái sử dụng cho nhiều module khác nhau
 */
public class ExportExcelService {

    public byte[] exportTableToExcel(JTable table, String sheetName, boolean excludeLastColumn) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            if (excludeLastColumn) columnCount--;

            // Header
            Row headerRow = sheet.createRow(0);
            Cell sttCell = headerRow.createCell(0);
            sttCell.setCellValue("STT");
            sttCell.setCellStyle(headerStyle);

            for (int i = 0; i < columnCount; i++) {
                Cell cell = headerRow.createCell(i + 1);
                cell.setCellValue(table.getColumnName(i));
                cell.setCellStyle(headerStyle);
            }

            // Data
            for (int i = 0; i < table.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(i + 1);

                for (int j = 0; j < columnCount; j++) {
                    Object value = table.getValueAt(i, j);
                    row.createCell(j + 1)
                            .setCellValue(value != null ? value.toString() : "");
                }
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * Thực hiện xuất dữ liệu ra file Excel
     */
    private void exportData(JTable table, String filePath, String sheetName) throws IOException {
        exportData(table, filePath, sheetName, true); // Mặc định bỏ cột cuối
    }

    /**
     * Thực hiện xuất dữ liệu ra file Excel với tùy chọn
     */
    private void exportData(JTable table, String filePath, String sheetName,
                            boolean excludeLastColumn) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Tạo style cho header
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Tạo style cho data cells
        CellStyle dataStyle = createDataStyle(workbook);

        // Lấy model từ table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int columnCount = model.getColumnCount();
        int rowCount = model.getRowCount();

        // Điều chỉnh số cột nếu cần bỏ cột cuối
        if (excludeLastColumn) {
            columnCount--;
        }

        // Tạo header row
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(30);

        Font bodyFont = workbook.createFont();
        bodyFont.setFontHeightInPoints((short) 16);
        bodyFont.setFontName("Times New Roman");

        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setFont(bodyFont);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // Thêm cột STT
        Cell sttCell = headerRow.createCell(0);
        sttCell.setCellValue("STT");
        sttCell.setCellStyle(headerStyle);

        // Thêm các cột từ table
        for (int i = 0; i < columnCount; i++) {
            Cell cell = headerRow.createCell(i + 1);
            cell.setCellValue(table.getColumnName(i));
            cell.setCellStyle(headerStyle);
        }

        // Ghi data
        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.cloneStyleFrom(bodyStyle);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < table.getRowCount(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            dataRow.setHeightInPoints(22);

            // STT
            Cell sttValueCell = dataRow.createCell(0);
            sttValueCell.setCellValue(i + 1);
            sttValueCell.setCellStyle(bodyStyle);
            sttValueCell.setCellStyle(centerStyle);


            for (int j = 0; j < columnCount; j++) {
                Cell cell = dataRow.createCell(j + 1);
                Object value = table.getValueAt(i, j);
                cell.setCellValue(value != null ? value.toString() : "");
                if (j == 0) {
                    cell.setCellStyle(centerStyle);
                } else {
                    cell.setCellStyle(bodyStyle);
                }
            }

        }

        for (int i = 0; i <= columnCount; i++) {
            sheet.autoSizeColumn(i);

            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        sheet.setColumnWidth(0, 256 * 15);
        sheet.setColumnWidth(1, 256 * 15);

        workbook.close();
    }

    /**
     * Tạo style cho header
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();

        // Font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 16);
        headerFont.setFontName("Times New Roman");
        headerStyle.setFont(headerFont);

        // Background color
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Alignment
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Borders
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);

        return headerStyle;
    }

    /**
     * Tạo style cho data cells
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();

        // Borders
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);

        // Alignment
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return dataStyle;
    }


}