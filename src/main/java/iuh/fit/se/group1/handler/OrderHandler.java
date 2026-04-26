package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.dto.OrderDetailDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Handler for Order-related requests
 * Processes commands: GET_BY_ID, GET_ALL, GET_ALL_WITH_RELATIONSHIP, GET_UNPAID,
 * GET_UNPAID_BY_KEYWORD, SEARCH_BY_KEYWORD, CREATE, UPDATE_STATUS_PAID, UPDATE_DEPOSIT, DELETE
 */
@RequiredArgsConstructor
public class OrderHandler implements RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderHandler.class);
    private final OrderService orderService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        try {
            return switch (commandType) {
                case ORDER_GET_BY_ID -> handleGetById(request);
                case ORDER_GET_ALL -> handleGetAll();
                case ORDER_GET_ALL_WITH_RELATIONSHIP -> handleGetAllWithRelationship();
                case ORDER_GET_UNPAID -> handleGetUnpaid();
                case ORDER_GET_UNPAID_BY_KEYWORD -> handleGetUnpaidByKeyword(request);
                case ORDER_SEARCH_BY_KEYWORD -> handleSearchByKeyword(request);
                case ORDER_CREATE -> handleCreate(request);
                case ORDER_UPDATE_STATUS_PAID -> handleUpdateStatusPaid(request);
                case ORDER_UPDATE_DEPOSIT -> handleUpdateDeposit(request);
                case ORDER_DELETE -> handleDelete(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling order request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetById(Request request) {
        Long orderId = (Long) request.getRequest();
        if (orderId == null) {
            return Response.builder()
                    .code(400)
                    .message("Order ID cannot be null")
                    .build();
        }

        OrderDTO order = orderService.getOrderById(orderId);
        if (order == null) {
            return Response.builder()
                    .code(404)
                    .message("Order not found with ID: " + orderId)
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get order successfully")
                .data(order)
                .build();
    }

    private Response handleGetAll() {
        List<OrderDTO> orders = orderService.getAllOrders();

        if (orders == null || orders.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No orders found")
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get all orders successfully")
                .data(orders)
                .build();
    }

    private Response handleGetAllWithRelationship() {
        List<OrderDTO> orders = orderService.getAllOrdersWithRelationship();

        if (orders == null || orders.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No orders found")
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get all orders with relationship successfully")
                .data(orders)
                .build();
    }

    private Response handleGetUnpaid() {
        List<OrderDTO> orders = orderService.getUnpaidOrders();

        if (orders == null || orders.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No unpaid orders found")
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get unpaid orders successfully")
                .data(orders)
                .build();
    }

    private Response handleGetUnpaidByKeyword(Request request) {
        String keyword = request.getRequest().toString();
        if (keyword == null || keyword.isEmpty()) {
            return Response.builder()
                    .code(400)
                    .message("Keyword cannot be null or empty")
                    .build();
        }

        List<OrderDTO> orders = orderService.getUnpaidOrdersByKeyword(keyword);

        if (orders == null || orders.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No unpaid orders found matching keyword: " + keyword)
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get unpaid orders by keyword successfully")
                .data(orders)
                .build();
    }

    private Response handleSearchByKeyword(Request request) {
        String keyword = request.getRequest().toString();
        if (keyword == null || keyword.isEmpty()) {
            return Response.builder()
                    .code(400)
                    .message("Keyword cannot be null or empty")
                    .build();
        }

        List<OrderDTO> orders = orderService.searchOrdersByKeyword(keyword);

        if (orders == null || orders.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No orders found matching keyword: " + keyword)
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Search orders by keyword successfully")
                .data(orders)
                .build();
    }

    private Response handleCreate(Request request) {
        Object obj = request.getRequest();
        if (!(obj instanceof Map)) {
            return Response.builder()
                    .code(400)
                    .message("Invalid request format for create order")
                    .build();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> requestData = (Map<String, Object>) obj;
        OrderDTO orderDTO = (OrderDTO) requestData.get("order");
        @SuppressWarnings("unchecked")
        List<OrderDetailDTO> orderDetails = (List<OrderDetailDTO>) requestData.get("orderDetails");

        if (orderDTO == null) {
            return Response.builder()
                    .code(400)
                    .message("Order data cannot be null")
                    .build();
        }

        try {
            OrderDTO created = orderService.createOrder(orderDTO, orderDetails != null ? orderDetails : List.of());
            
            if (created == null) {
                return Response.builder()
                        .code(400)
                        .message("Failed to create order - missing required fields")
                        .build();
            }
            
            return Response.builder()
                    .code(201)
                    .message("Order created successfully")
                    .data(created)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(400)
                    .message("Error creating order: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdateStatusPaid(Request request) {
        OrderDTO orderDTO = (OrderDTO) request.getRequest();

        if (orderDTO == null || orderDTO.getOrderId() == null) {
            return Response.builder()
                    .code(400)
                    .message("Order data and order ID cannot be null")
                    .build();
        }

        try {
            orderService.updateOrderStatusToPaid(orderDTO);
            return Response.builder()
                    .code(200)
                    .message("Order status updated to paid successfully")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(400)
                    .message("Error updating order status: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdateDeposit(Request request) {
        Object obj = request.getRequest();
        if (!(obj instanceof Map)) {
            return Response.builder()
                    .code(400)
                    .message("Invalid request format for update deposit")
                    .build();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> requestData = (Map<String, Object>) obj;
        Long orderId = (Long) requestData.get("orderId");
        BigDecimal deposit = (BigDecimal) requestData.get("deposit");

        if (orderId == null || deposit == null) {
            return Response.builder()
                    .code(400)
                    .message("Order ID and deposit cannot be null")
                    .build();
        }

        try {
            orderService.updateOrderDeposit(orderId, deposit);
            return Response.builder()
                    .code(200)
                    .message("Order deposit updated successfully")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(400)
                    .message("Error updating order deposit: " + e.getMessage())
                    .build();
        }
    }

    private Response handleDelete(Request request) {
        Long orderId = (Long) request.getRequest();

        if (orderId == null) {
            return Response.builder()
                    .code(400)
                    .message("Order ID cannot be null")
                    .build();
        }

        try {
            orderService.deleteOrderById(orderId);
            return Response.builder()
                    .code(200)
                    .message("Order deleted successfully")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error deleting order: " + e.getMessage())
                    .build();
        }
    }
}

