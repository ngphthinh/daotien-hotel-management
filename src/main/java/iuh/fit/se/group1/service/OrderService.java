package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.entity.OrderType;
import iuh.fit.se.group1.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private OrderDetailService orderDetailsService;

    public OrderService() {
        this.orderDetailsService = new OrderDetailService();
        this.orderRepository = new OrderRepository();
    }

    public Order createOrder(Order order, List<OrderDetail> orderDetails) {
        if (order == null) {
            AppLogger.info("Order is null");
            return null;
        }

        if (order.getEmployee() == null || order.getCustomer() == null || order.getBookings() == null || order.getBookings().isEmpty()) {
            AppLogger.info("Order is missing required fields");
            return null;
        }
        // Lưu là đang sử li
        order.setOrderType(new OrderType(2L));
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            AppLogger.info("Failed to save order");
            return null;
        }


        if (orderDetailsService.saveOrderDetailsForOrder(savedOrder, orderDetails)) {



//            BigDecimal totalAmountOrderDetail = orderDetails.stream()
//                    .map(detail -> detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);



//            savedOrder.setTotalAmount(totalAmountOrderDetail);
//            updateTotalAmount(savedOrder, totalAmountOrderDetail);

            AppLogger.info("Order created successfully with order details");
            return savedOrder;
        }
        return null;
    }

    private void updateTotalAmount(Order order, BigDecimal totalAmountOrderDetail) {
        BigDecimal totalAmount = order.getTotalAmountBooking().add(totalAmountOrderDetail);
        order.setTotalAmount(totalAmount);
        orderRepository.updateTotalAmount(order.getOrderId(), totalAmount);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<BookingDisplayDTO> findAllBookingDisplay(){
        return orderRepository.findAllBookingDisplay();
    }
}
