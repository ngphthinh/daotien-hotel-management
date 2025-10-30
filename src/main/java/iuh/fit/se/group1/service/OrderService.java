package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.repository.OrderRepository;

public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    public Order createOrder(Order order) {
        if (order == null) {
            AppLogger.info("Order is null");
            return null;
        }

        if (order.getEmployee() == null || order.getOrderType() == null || order.getCustomer() == null || order.getBookings() == null || order.getBookings().isEmpty()) {
            AppLogger.info("Order is missing required fields");
            return null;
        }

        Order savedOrder = orderRepository.save(order);
        if (savedOrder != null) {
            AppLogger.info("Order created successfully with ID: %d by employeeId: %d ".formatted(savedOrder.getOrderId(), savedOrder.getEmployee().getEmployeeId()));
            return savedOrder;
        }
        return null;
    }
}
