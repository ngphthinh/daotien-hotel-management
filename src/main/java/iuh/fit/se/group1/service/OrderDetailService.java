package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.repository.OrderDetailRepository;

import java.util.List;

public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService() {
        this.orderDetailRepository = new OrderDetailRepository();
    }

    public boolean saveOrderDetailsForOrder(Order savedOrder, List<OrderDetail> orderDetails) {
        return orderDetailRepository.save(savedOrder, orderDetails);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId){
        return orderDetailRepository.findByOrderId(orderId);
    }
}
