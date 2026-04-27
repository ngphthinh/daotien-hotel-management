package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.OrderDetailCreateRequest;
import iuh.fit.se.group1.dto.OrderDetailDTO;
import iuh.fit.se.group1.dto.OrderDetailDeleteRequest;
import iuh.fit.se.group1.dto.OrderDetailUpdateRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OrderDetailServiceClient implements ServiceClient {
    private final ClientSocketManager socket;


    public Response getOrderDetailsByOrderId(Long orderId) throws Exception {
        return socket.send(Request.builder()
                .request(orderId)
                .commandType(CommandType.ORDER_DETAIL_FROM_ORDER)
                .build()
        ).get();
    }

    public Response updateOrderDetailFormOrderId(Long amenityId, BigDecimal unitPrice, int quantity, Long orderId) throws Exception {
        OrderDetailUpdateRequest orderDetailUpdateRequest = OrderDetailUpdateRequest.builder()
                .amenityId(amenityId)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .orderId(orderId)
                .build();

        return socket.send(Request.builder()
                .request(orderDetailUpdateRequest)
                .commandType(CommandType.ORDER_DETAIL_UPDATE_FROM_ORDER)
                .build()
        ).get();
    }

    public Response save(OrderDetailDTO newDetail, Long orderId) throws Exception {
        OrderDetailCreateRequest request = OrderDetailCreateRequest.builder()
                .orderDetail(newDetail)
                .orderId(orderId)
                .build();

        return socket.send(Request.builder()
                .request(request)
                .commandType(CommandType.ORDER_DETAIL_CREATE)
                .build()
        ).get();

    }

    public Response deleteById(Long amenityId, Long orderId) throws Exception {
        OrderDetailDeleteRequest detailDeleteRequest = OrderDetailDeleteRequest.builder()
                .amenityId(amenityId)
                .orderId(orderId)
                .build();

        return socket.send(Request.builder()
                .request(detailDeleteRequest)
                .commandType(CommandType.ORDER_DETAIL_DELETE_BY_ID)
                .build()
        ).get();

    }
}
