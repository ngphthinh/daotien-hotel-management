package iuh.fit.se.group1.util;

import iuh.fit.se.group1.dto.ExportResponse;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.SocketFacade;
import iuh.fit.se.group1.network.client.service.ImportExportExcelServiceClient;

import javax.swing.*;

public class ExportUtil {
    public static byte[] exportTableToExcel(JTable table, String sheetName, boolean excludeLastColumn) throws Exception {
        ImportExportExcelServiceClient importExportExcelServiceClient = SocketFacade.getInstance().getImportExportExcel();

        Response response = importExportExcelServiceClient.exportTableToExcel(
                table,
                sheetName,
                excludeLastColumn
        );

        if (response.getCode() != 200) {
            JOptionPane.showMessageDialog(null, "Server returned HTTP Status " + response.getCode() + "nMessage: " + response.getMessage(), "Lỗi xuất Excel", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        ExportResponse exportResponse = (ExportResponse) response.getData();

        return exportResponse.getFileData();
    }
}
