package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.PaymentRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;
    public Response createPayment(PaymentRequest paymentRequest) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PAYMENT_CREATE)
                .request(paymentRequest)
                .build()).get();
    }
}
