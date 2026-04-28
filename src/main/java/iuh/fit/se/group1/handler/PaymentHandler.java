package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.PaymentResponse;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RequiredArgsConstructor
public class PaymentHandler implements RequestHandler {
    private final static Logger log = LoggerFactory.getLogger(PaymentHandler.class);
    private final PaymentService paymentService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case PAYMENT_CREATE -> handleCreatePayment(request);
                case PAYMENT_QUERY -> handleQueryPayment(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling payment request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleQueryPayment(Request request) {
        try {

            String orderId = request.getRequest().toString();
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .raw(paymentService.queryPayment(orderId))
                    .build();
            return Response.builder()
                    .code(200)
                    .message("Query payment successful")
                    .data(paymentResponse)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreatePayment(Request request) {
        try {
            OrderDTO orderDTO = (OrderDTO) request.getRequest();
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .raw(paymentService.createPayment(orderDTO))
                    .build();

            return Response.builder()
                    .code(200)
                    .message("Payment created successfully")
                    .data(paymentResponse)
                    .build();

        } catch (Exception e) {
            log.error("Error handling payment request: {}", request.getCommandType(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }
}
