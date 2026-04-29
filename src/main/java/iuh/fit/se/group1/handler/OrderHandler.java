package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.network.ClientHandler;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class OrderHandler implements RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderHandler.class);
    private final OrderService orderService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        try {
            Response response;
            switch (commandType) {
                case ORDER_GET_BY_ID -> response = handleGetById(request);
                case ORDER_GET_ALL -> response = handleGetAll();
                case ORDER_GET_ALL_WITH_RELATIONSHIP -> response = handleGetAllWithRelationship();
                case ORDER_GET_UNPAID -> response = handleGetUnpaid();
                case ORDER_GET_UNPAID_BY_KEYWORD -> response = handleGetUnpaidByKeyword(request);
                case ORDER_SEARCH_BY_KEYWORD -> response = handleSearchByKeyword(request);
                case ORDER_CREATE -> response = handleCreate(request);
                case ORDER_UPDATE_STATUS_PAID -> response = handleUpdateStatusPaid(request);
                case ORDER_UPDATE_DEPOSIT -> response = handleUpdateDeposit(request);
                case ORDER_DELETE -> response = handleDelete(request);
                case ORDER_GET_REVENUE_BETWEEN_DATES -> response = handleGetRevenueBetweenDates(request);
                case ORDER_GET_REVENUE_BETWEEN_DATES_BY_ROOM_TYPE ->
                        response = handleGetRevenueBetweenDatesByRoomType(request);
                case ORDER_GET_REVENUE_BETWEEN_DATES_BOOKING_COUNT ->
                        response = handleGetRevenueBetweenDatesBookingCount(request);
                case ORDER_CREATE_RECORD -> response = handleCreateRecord(request);
                case ORDER_UPDATE_TOTAL_PRICE -> response = handleUpdateTotalPrice(request);
                case ORDER_MOVE_BOOKING_TO_ORDER -> response = handleMoveBookingToOrder(request);
                case ORDER_RE_CALCULATE_TOTAL_PRICE -> response = handleReCalculateTotalPrice(request);
                case ORDER_UPDATE_ORDER_TYPE -> response = handleUpdateOrderType(request);
                case ORDER_GET_ALL_WITH_RELATIONSHIP_COMPLETE_YET ->
                        response = handleGetAllWithRelationshipAndCompleteYet();
                case ORDER_GET_UN_PENDING_BY_KEYWORD -> response = handleGetOrdersUnPendingByKeyWord(request);
                default -> response = Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            }

            if (isWriteCommand(commandType) && response.getCode() == 200) {
                String message = getMessage(commandType);
                ClientHandler.broadcast(message, CommandType.ORDER_REFRESH);
            }

            return response;
        } catch (Exception e) {
            log.error("Error handling order request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private String getMessage(CommandType commandType) {
        return switch (commandType) {
            case ORDER_CREATE -> "Order created";
            case ORDER_UPDATE_STATUS_PAID -> "Order status updated to paid";
            case ORDER_UPDATE_DEPOSIT -> "Order deposit updated";
            case ORDER_DELETE -> "Order deleted";
            case ORDER_UPDATE_TOTAL_PRICE -> "Order total price updated";
            case ORDER_MOVE_BOOKING_TO_ORDER -> "Booking moved to order";
            case ORDER_RE_CALCULATE_TOTAL_PRICE -> "Order total price recalculated";
            case ORDER_UPDATE_ORDER_TYPE -> "Order type updated";
            case ORDER_CREATE_RECORD -> "Order record created";
            default -> "Order has been changed";
        };
    }

    private boolean isWriteCommand(CommandType commandType) {
        return switch (commandType) {
            case ORDER_CREATE, ORDER_UPDATE_STATUS_PAID, ORDER_UPDATE_DEPOSIT, ORDER_DELETE, ORDER_UPDATE_TOTAL_PRICE,
                 ORDER_MOVE_BOOKING_TO_ORDER, ORDER_RE_CALCULATE_TOTAL_PRICE, ORDER_UPDATE_ORDER_TYPE,
                 ORDER_CREATE_RECORD -> true;
            default -> false;
        };
    }

    private Response handleGetOrdersUnPendingByKeyWord(Request request) {
        try {
            String keyword = request.getRequest().toString();
            return Response.builder()
                    .code(200)
                    .message("Get unpending order by keyword")
                    .data(orderService.getOrdersUnPendingByKeyWord(keyword))
                    .build();


        } catch (Exception e) {
            log.error("Error handling order request: {}", request, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }

    }

    private Response handleGetAllWithRelationshipAndCompleteYet() {
        try {
            List<OrderDTO> orders = orderService.getAllOrdersWithRelationshipAndCompleteYet();

            if (orders == null || orders.isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No orders found")
                        .data(List.of())
                        .build();
            }

            return Response.builder()
                    .code(200)
                    .message("Get all orders with relationship and complete yet successfully")
                    .data(orders)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdateOrderType(Request request) {
        OrderUpdateOrderTypeRequest updateTypeRequest = (OrderUpdateOrderTypeRequest) request.getRequest();
        orderService.updateOrderType(updateTypeRequest.getOrderId(), updateTypeRequest.getOrderTypeID());
        return Response.builder()
                .code(200)
                .message("Order type updated successfully")
                .build();
    }

    private Response handleReCalculateTotalPrice(Request request) {
        Long id = (Long) request.getRequest();

        orderService.recalculateOrderTotal(id);
        return Response.builder()
                .code(200)
                .message("Order has been recalculated")
                .build();
    }

    private Response handleMoveBookingToOrder(Request request) {

        MoveRequestBooking moveRequestBooking = (MoveRequestBooking) request.getRequest();
        orderService.moveBookingsToOrder(moveRequestBooking.getOrderId(), moveRequestBooking.getBookingIdsToMove());
        return Response.builder()
                .code(200)
                .message("Move booking to order successfully")
                .build();

    }

    private Response handleUpdateTotalPrice(Request request) {
        OrderUpdatePriceRequest updatePriceRequest = (OrderUpdatePriceRequest) request.getRequest();
        orderService.updateOrderTotalAmount(updatePriceRequest.getOrderId(), updatePriceRequest.getTotalPrice());
        return Response.builder()
                .code(200)
                .message("Total price updated")
                .build();
    }

    private Response handleCreateRecord(Request request) {


        OrderDTO orderDTO = orderService.createOrderRecord((OrderDTO) request.getRequest());
        return Response.builder()
                .code(200)
                .message("Order record created successfully")
                .data(orderDTO)
                .build();
    }

    private Response handleGetRevenueBetweenDatesBookingCount(Request request) {

        LocalDate date = (LocalDate) request.getRequest();

        return Response.builder()
                .code(200)
                .message("Get revenue between Dates booking count")
                .data(BookingCount.builder().bookingCount(orderService.getBookingCountByRoomTypeAndDate(date)).build())
                .build();


    }


    private Response handleGetRevenueBetweenDatesByRoomType(Request request) {

        DateRangeRequest dateRangeRequest = (DateRangeRequest) request.getRequest();

        Map<String, BigDecimal> map = orderService.getRevenueByRoomType(dateRangeRequest.getFrom(), dateRangeRequest.getTo());


        return Response.builder()
                .code(200)
                .message("Get revenue between Dates booking count")
                .data(RevenueDTO.builder()
                        .revenueByDate(map)
                        .build())
                .build();

    }

    private Response handleGetRevenueBetweenDates(Request request) {

        DateRangeRequest dateRangeRequest = (DateRangeRequest) request.getRequest();

        BigDecimal value = orderService.getTotalRevenueBetweenDates(dateRangeRequest.getFrom(), dateRangeRequest.getTo());


        return Response.builder()
                .code(200)
                .message("Get revenue between Dates booking count")
                .data(value)
                .build();


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
        CreateOrderRequest obj = (CreateOrderRequest) request.getRequest();
        OrderDTO orderDTO = obj.getOrder();
        List<OrderDetailDTO> orderDetails = obj.getOrderDetails();

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

            Response response = Response.builder()
                    .code(200)
                    .message("Order created successfully")
                    .data(created)
                    .build();

            return response;
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

            Response response = Response.builder()
                    .code(200)
                    .message("Order deposit updated successfully")
                    .build();
            return response;
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

