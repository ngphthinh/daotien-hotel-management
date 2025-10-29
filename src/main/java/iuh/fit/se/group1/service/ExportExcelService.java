package iuh.fit.se.group1.service;

import iuh.fit.se.group1.ui.component.custom.message.Message;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Service class để xuất dữ liệu từ JTable ra file Excel
 * Có thể tái sử dụng cho nhiều module khác nhau
 */
public class ExportExcelService {

    /**
     * Xuất dữ liệu từ JTable ra file Excel
     *
     * @param parent Component cha (để hiển thị dialog)
     * @param table JTable chứa dữ liệu cần xuất
     * @param sheetName Tên sheet trong Excel
     * @param defaultFileName Tên file mặc định (không bao gồm extension)
     */
    public static void exportTableToExcel(Component parent, JTable table, String sheetName, String defaultFileName) {
        try {
            // Tạo file chooser với thư mục mặc định là Desktop hoặc Documents
            JFileChooser fileChooser = new JFileChooser();
            
            // Set thư mục mặc định là Desktop
            String userHome = System.getProperty("user.home");
            java.io.File desktopDir = new java.io.File(userHome, "Desktop");
            if (!desktopDir.exists()) {
                desktopDir = new java.io.File(userHome, "Documents");
            }
            fileChooser.setCurrentDirectory(desktopDir);
            
            fileChooser.setDialogTitle("Lưu file Excel");

            // Tên file mặc định với ngày hiện tại
            String fileName = defaultFileName + "_" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx";
            fileChooser.setSelectedFile(new java.io.File(desktopDir, fileName));

            // Chỉ cho phép file .xlsx
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(parent);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Đảm bảo file có đuôi .xlsx
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                // Xuất dữ liệu
                exportData(table, filePath, sheetName);

                Message.showMessage("Thành công",
                        "Xuất file Excel thành công!\nĐường dẫn: " + filePath);
            }
        } catch (Exception e) {
            Message.showMessage("Lỗi", "Không thể xuất file Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xuất dữ liệu từ JTable với cấu hình tùy chỉnh
     *
     * @param parent Component cha
     * @param table JTable chứa dữ liệu
     * @param sheetName Tên sheet
     * @param defaultFileName Tên file mặc định
     * @param excludeLastColumn true nếu muốn bỏ cột cuối (cột chức năng)
     */
    public static void exportTableToExcel(Component parent, JTable table, String sheetName,
                                          String defaultFileName, boolean excludeLastColumn) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");

            String fileName = defaultFileName + "_" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx";
            fileChooser.setSelectedFile(new java.io.File(fileName));

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(parent);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                exportData(table, filePath, sheetName, excludeLastColumn);

                Message.showMessage("Thành công",
                        "Xuất file Excel thành công!\nĐường dẫn: " + filePath);
            }
        } catch (Exception e) {
            Message.showMessage("Lỗi", "Không thể xuất file Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Thực hiện xuất dữ liệu ra file Excel
     */
    private static void exportData(JTable table, String filePath, String sheetName) throws IOException {
        exportData(table, filePath, sheetName, true); // Mặc định bỏ cột cuối
    }

    /**
     * Thực hiện xuất dữ liệu ra file Excel với tùy chọn
     */
    private static void exportData(JTable table, String filePath, String sheetName,
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
        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.createRow(i + 1);

            // STT
            Cell sttDataCell = row.createCell(0);
            sttDataCell.setCellValue(i + 1);
            sttDataCell.setCellStyle(dataStyle);

            // Các cột dữ liệu
            for (int j = 0; j < columnCount; j++) {
                Cell cell = row.createCell(j + 1);
                Object value = model.getValueAt(i, j);
                cell.setCellValue(value != null ? value.toString() : "");
                cell.setCellStyle(dataStyle);
            }
        }

        // Auto-size columns
        for (int i = 0; i <= columnCount; i++) {
            sheet.autoSizeColumn(i);
            // Thêm padding
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }

        // Ghi file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    /**
     * Tạo style cho header
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();

        // Font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);

        // Background color
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
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
    private static CellStyle createDataStyle(Workbook workbook) {
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