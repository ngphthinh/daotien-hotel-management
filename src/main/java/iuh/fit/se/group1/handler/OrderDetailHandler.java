package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.OrderDetailCreateRequest;
import iuh.fit.se.group1.dto.OrderDetailDeleteRequest;
import iuh.fit.se.group1.dto.OrderDetailUpdateRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.OrderDetailService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.swing.*;

@RequiredArgsConstructor
public class OrderDetailHandler implements RequestHandler {

    private final OrderDetailService orderDetailService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        try {
            return switch (commandType) {
                case ORDER_DETAIL_CREATE -> handleCreate(request);
                case ORDER_DETAIL_GET_BY_ID -> handleGetById(request);
                case ORDER_DETAIL_DELETE_BY_ID -> handleDeleteById(request);
                case ORDER_DETAIL_FROM_ORDER -> handleGetByOrderId(request);
                case ORDER_DETAIL_UPDATE_FROM_ORDER -> handleUpdate(request);
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

    private Response handleUpdate(Request request) {
        try {
            OrderDetailUpdateRequest req = (OrderDetailUpdateRequest) request.getRequest();

            orderDetailService.updateOrderDetailFormOrderId(
                    req.getAmenityId(),
                    req.getUnitPrice(),
                    req.getQuantity(),
                    req.getOrderId()
            );

            return Response.builder()
                    .code(200)
                    .message("Order Detail updated successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Update failed: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetByOrderId(Request request) {
        try {
            Long id = (Long) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Get Order Detail by Order ID")
                    .data(orderDetailService.getOrderDetailsByOrderId(id))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Get by order id failed: " + e.getMessage())
                    .build();
        }
    }

    private Response handleDeleteById(Request request) {
        try {
            OrderDetailDeleteRequest req = (OrderDetailDeleteRequest) request.getRequest();

            orderDetailService.deleteById(req.getAmenityId(), req.getOrderId());

            return Response.builder()
                    .code(200)
                    .message("Order has been deleted")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Delete failed: " + e.getMessage())
                    .build();
        }
    }


    private Response handleGetById(Request request) {
        try {
            Long id = (Long) request.getRequest();

            return Response.builder()
                    .code(200)
                    .message("Success")
                    .data(orderDetailService.getOrderDetailsByOrderId(id))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Get by id failed: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        try {
            OrderDetailCreateRequest data = (OrderDetailCreateRequest) request.getRequest();

            return Response.builder()
                    .data(orderDetailService.save(data.getOrderDetail(), data.getOrderId()))
                    .message("Order detail created successfully")
                    .code(200)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Create failed: " + e.getMessage())
                    .build();
        }
    }
}
