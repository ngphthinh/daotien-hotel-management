package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.ExportRequest;
import iuh.fit.se.group1.dto.ExportResponse;
import iuh.fit.se.group1.dto.ImportRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.EmailSenderService;
import iuh.fit.se.group1.service.ExportExcelService;
import iuh.fit.se.group1.service.ImportExcelService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImportExportHandler implements RequestHandler {

    private final ExportExcelService exportExcelService;
    private final ImportExcelService importExcelService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case EXPORT_EXCEL -> handleExportExcel(request);
                case IMPORT_SURCHARGES -> handleImportSurcharges(request);
                case IMPORT_AMENITIES -> handleImportAmenities(request);
                case IMPORT_PROMOTIONS -> handleImportPromotions(request);
                case IMPORT_CUSTOMERS -> handleImportCustomers(request);
                case IMPORT_EMPLOYEES -> handleImportEmployees(request);
                case IMPORT_ROOMS -> handleImportRoomTypes(request);
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

    private Response handleImportRoomTypes(Request request) {

        try {
            ImportRequest importRequest = (ImportRequest) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Import successful")
                    .data(importExcelService.importRoomsFromExcel(importRequest.getFile()))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }

    }

    private Response handleImportEmployees(Request request) {
        try {
            ImportRequest importRequest = (ImportRequest) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Import successful")
                    .data(importExcelService.importEmployeesFromExcel(importRequest.getFile()))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleImportCustomers(Request request) {
        try {
            ImportRequest importRequest = (ImportRequest) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Import successful")
                    .data(importExcelService.importCustomersFromExcel(importRequest.getFile()))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleImportPromotions(Request request) {
        try {
            ImportRequest importRequest = (ImportRequest) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Import successful")
                    .data(importExcelService.importPromotionsFromExcel(importRequest.getFile()))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }

    }

    private Response handleImportAmenities(Request request) {
        try {
            ImportRequest importRequest = (ImportRequest) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Import successful")
                    .data(importExcelService.importAmenitiesFromExcel(importRequest.getFile()))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }

    }

    private Response handleImportSurcharges(Request request) {
        try {
            ImportRequest importRequest = (ImportRequest) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Import successful")
                    .data(importExcelService.importSurchargesFromExcel(importRequest.getFile()))
                    .build();

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
                    .fileData(exportExcelService.exportTableToExcel(exportRequest.getColumnHeaders(), exportRequest.getData(), exportRequest.getTitle(), exportRequest.isExcludeLastColumn()))
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
