package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.ExportRequest;
import iuh.fit.se.group1.dto.ExportResponse;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.EmailSenderService;
import iuh.fit.se.group1.service.ExportExcelService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImportExportHandler implements RequestHandler {

    private final ExportExcelService exportExcelService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case EXPORT_EXCEL -> handleExportExcel(request);

                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleExportExcel(Request request) {
        try {
            ExportRequest exportRequest = (ExportRequest) request.getRequest();


            ExportResponse exportResponse = ExportResponse.builder()
                    .fileData(exportExcelService.exportTableToExcel(exportRequest.getTable(), exportRequest.getTitle(), exportRequest.isExcludeLastColumn()))
                    .build();


            return Response.builder()
                    .code(200)
                    .message("Export successful")
                    .data(exportResponse)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }
}
