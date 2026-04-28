package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.ExportOrderToPdfRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.JaspersoftExportService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JaspersoftExportHandler implements RequestHandler {
    private final JaspersoftExportService jaspersoftExportService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        if (commandType.equals(CommandType.JASPERSOFT_EXPORT_ORDER_TO_PDF)) {
            try {
                ExportOrderToPdfRequest exportOrderToPdfRequest = (ExportOrderToPdfRequest) request.getRequest();

                return Response.builder()
                        .code(200)
                        .message("Exported order to pdf")
                        .data(jaspersoftExportService.exportOrderToPdf(exportOrderToPdfRequest))
                        .build();
            } catch (Exception e) {
                return Response.builder()
                        .code(400)
                        .message(e.getMessage())
                        .build();
            }
        } else {
            return Response.builder()
                    .code(400)
                    .message("Unsupported command type: " + commandType)
                    .build();
        }
    }
}
