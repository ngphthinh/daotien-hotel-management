package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.dto.ExportRequest;
import iuh.fit.se.group1.dto.ImportRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class ImportExportExcelServiceClient implements ServiceClient {
    private final ClientSocketManager socket;

    public Response exportTableToExcel(JTable tbl, String title, boolean b) throws Exception {
        ExportRequest request = ExportRequest.builder()
                .title(title)
                .excludeLastColumn(b)
                .table(tbl)
                .build();
        return socket.send(Request.builder()
                .request(request)
                .commandType(CommandType.EXPORT_EXCEL)
                .build()).get();
    }


    public Response importAmenitiesFromExcel(File file) throws Exception {
        return socket.send(Request.builder()
                .request(ImportRequest.builder().file(file).build())
                .commandType(CommandType.IMPORT_AMENITIES)
                .build()).get();
    }

    public Response importPromotionsFromExcel(File file) throws Exception {
        return socket.send(Request.builder()
                .request(ImportRequest.builder().file(file).build())
                .commandType(CommandType.IMPORT_PROMOTIONS)
                .build()).get();
    }

    public Response importCustomersFromExcel(File file) throws Exception {
        return socket.send(Request.builder()
                .request(ImportRequest.builder().file(file).build())
                .commandType(CommandType.IMPORT_CUSTOMERS)
                .build()).get();
    }

    public Response importEmployeesFromExcel(File file) throws Exception {
        return socket.send(Request.builder()
                .request(ImportRequest.builder().file(file).build())
                .commandType(CommandType.IMPORT_EMPLOYEES)
                .build()).get();
    }


    public Response importRoomsFromExcel(File file) throws Exception {
        return socket.send(Request.builder()
                .request(ImportRequest.builder().file(file).build())
                .commandType(CommandType.IMPORT_ROOMS)
                .build()).get();
    }

    public Response importSurchargesFromExcel(File file) throws Exception {
        return socket.send(Request.builder()
                .request(ImportRequest.builder().file(file).build())
                .commandType(CommandType.IMPORT_SURCHARGES)
                .build()).get();
    }
}
