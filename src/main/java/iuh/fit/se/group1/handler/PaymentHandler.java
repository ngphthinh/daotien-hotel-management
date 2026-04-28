package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.PaymentRequest;
import iuh.fit.se.group1.dto.PaymentResponse;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@RequiredArgsConstructor
public class PaymentHandler implements RequestHandler {
    private final static Logger log = LoggerFactory.getLogger(PaymentHandler.class);
    private final OrderService orderService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case PAYMENT_CREATE -> handleCreatePayment(request);
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

    private Response handleCreatePayment(Request request) {
        PaymentRequest req = (PaymentRequest) request.getRequest();
        if (req == null) {
            return Response.builder()
                    .code(400)
                    .message("Payment request cannot be null")
                    .build();
        }

        if (req.getOrderId() == null) {
            return Response.builder()
                    .code(400)
                    .message("Order id required")
                    .build();
        }

        if (req.getPaymentType() == null) {
            return Response.builder()
                    .code(400)
                    .message("Payment type required")
                    .build();
        }

        try {
            OrderDTO order = orderService.getOrderById(req.getOrderId());
            if (order == null) {
                return Response.builder()
                        .code(404)
                        .message("Order not found")
                        .build();
            }
            order.setPaymentType(req.getPaymentType());
            order.setPaymentDate(req.getPaymentDate() != null ? req.getPaymentDate() : LocalDate.now());
            order.setTotalAmount(req.getTotalAmount() != null ? req.getTotalAmount() : order.getTotalAmount());
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .orderId(order.getOrderId())
                    .message("Payment successful")
                    .paymentStatus("PAID")
                    .totalAmount(order.getTotalAmount())
                    .paymentDate(order.getPaymentDate())
                    .employeePaymentId(order.getEmployeePayment() != null ? order.getEmployeePayment().getEmployeeId() : null)
                    .build();

            return Response.builder()
                    .code(200)
                    .message("Payment created successfully")
                    .data(paymentResponse)
                    .build();

        } catch (Exception e) {
            log.error("Error creating payment: ", e);
            return Response.builder()
                    .code(500)
                    .message("Server error: " + e.getMessage())
                    .build();
        }
    }
}
