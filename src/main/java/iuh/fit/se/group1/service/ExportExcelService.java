package iuh.fit.se.group1.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ExportExcelService {

    public byte[] exportTableToExcel(List<String> columns,
                                     List<List<Object>> data,
                                     String sheetName,
                                     boolean excludeLastColumn) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            int columnCount = columns.size();
            if (excludeLastColumn) columnCount--;

            // ===== Header =====
            Row headerRow = sheet.createRow(0);

            Cell sttCell = headerRow.createCell(0);
            sttCell.setCellValue("STT");
            sttCell.setCellStyle(headerStyle);

            for (int i = 0; i < columnCount; i++) {
                Cell cell = headerRow.createCell(i + 1);
                cell.setCellValue(columns.get(i));
                cell.setCellStyle(headerStyle);
            }

            // ===== Data =====
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);

                // STT
                Cell sttValue = row.createCell(0);
                sttValue.setCellValue(i + 1);
                sttValue.setCellStyle(dataStyle);

                for (int j = 0; j < columnCount; j++) {
                    Object value = data.get(i).get(j);

                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(value != null ? value.toString() : "");
                    cell.setCellStyle(dataStyle);
                }
            }

            // Auto size
            for (int i = 0; i <= columnCount; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Times New Roman");

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }
}