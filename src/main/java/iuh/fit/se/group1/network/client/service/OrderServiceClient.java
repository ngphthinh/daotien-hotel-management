package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class OrderServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;

    public Response getAllOrders() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_ALL)
                .build()).get();
    }

    public Response getAllOrdersWithRelationship() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_ALL_WITH_RELATIONSHIP)
                .build()).get();
    }

    public Response searchOrdersByKeyword(String filter) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_SEARCH_BY_KEYWORD)
                .request(filter)
                .build()).get();
    }

    public Response getOrderById(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_BY_ID)
                .request(id)
                .build()).get();
    }

    public Response deleteOrderById(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_DELETE)
                .request(id)
                .build()).get();
    }

    public Response getTotalRevenueBetweenDates(LocalDate from, LocalDate to) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_REVENUE_BETWEEN_DATES)
                .request(DateRangeRequest.builder()
                        .from(from)
                        .to(to).build())
                .build()).get();
    }

    public Response getRevenueByRoomType(LocalDate from, LocalDate to) throws Exception {

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_REVENUE_BETWEEN_DATES_BY_ROOM_TYPE)
                .request(DateRangeRequest.builder()
                        .from(from)
                        .to(to).build())
                .build()).get();
    }

    public Response getBookingCountByRoomTypeAndDate(LocalDate currentDate) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_REVENUE_BETWEEN_DATES_BOOKING_COUNT)
                .request(currentDate)
                .build()).get();
    }

    public Response createOrder(OrderDTO order, List<OrderDetailDTO> orderDetails) throws Exception {

        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                .order(order)
                .orderDetails(orderDetails)
                .build();

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_CREATE)
                .request(createOrderRequest)
                .build()).get();
    }

    public Response getUnpaidOrdersByKeyword(String keyword) throws Exception {

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_UNPAID_BY_KEYWORD)
                .request(keyword)
                .build()).get();

    }

    public Response getUnpaidOrders() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_GET_UNPAID)
                .build()).get();
    }

    public Response createOrderRecord(OrderDTO newOrderRecord) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_CREATE_RECORD)
                .request(newOrderRecord)
                .build()).get();

    }

    public Response updateOrderStatusToPaid(OrderDTO currentOrder) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_UPDATE_STATUS_PAID)
                .request(currentOrder)
                .build()).get();

    }

    public Response updateOrderTotalAmount(Long orderId, BigDecimal totalRemainingRooms) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_UPDATE_TOTAL_PRICE)
                .request(OrderUpdatePriceRequest.builder()
                        .orderId(orderId)
                        .totalPrice(totalRemainingRooms)
                        .build())
                .build()).get();

    }

    public Response moveBookingsToOrder(Long orderId, List<Long> bookingIdsToMove) throws Exception {

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_MOVE_BOOKING_TO_ORDER)
                .request(MoveRequestBooking.builder()
                        .orderId(orderId)
                        .bookingIdsToMove(bookingIdsToMove)
                        .build())
                .build()).get();

    }

    public Response recalculateOrderTotal(Long orderId) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_RE_CALCULATE_TOTAL_PRICE)
                .request(orderId)
                .build()).get();
    }

    public Response updateOrderType(Long orderId, long orderTypeID) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ORDER_UPDATE_ORDER_TYPE)
                .request(OrderUpdateOrderTypeRequest.builder()
                        .orderId(orderId)
                        .orderTypeID(orderTypeID)
                        .build())
                .build()).get();
    }
}
