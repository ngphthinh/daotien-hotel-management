package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.ExportOrderToPdfRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JaspersoftExportServiceClient implements ServiceClient {
    private final ClientSocketManager socketManager;


    public Response exportOrderToPdf(Long order, String promotionStr, String paymentType, String totalPricePayment, String employeeCurrentFullName) throws Exception {
        ExportOrderToPdfRequest request = ExportOrderToPdfRequest.builder()
                .order(order)
                .promotionStr(promotionStr)
                .paymentType(paymentType)
                .totalPricePayment(totalPricePayment)
                .employeeCurrentFullName(employeeCurrentFullName)
                .build();

        return socketManager.send(Request.builder()
                .commandType(CommandType.JASPERSOFT_EXPORT_ORDER_TO_PDF)
                .request(request)
                .build()).get();
    }
}
