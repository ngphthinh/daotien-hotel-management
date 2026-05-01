package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {
    private final CustomerMapper customerMapper;
    private final EmployeeMapper employeeMapper;
    private final PromotionMapper promotionMapper;
    private final OrderTypeMapper orderTypeMapper;
    private final BookingMapper bookingMapper;

    public OrderMapper() {
        this.orderTypeMapper = new OrderTypeMapper();
        this.customerMapper = new CustomerMapper();
        this.bookingMapper = new BookingMapper();
        this.employeeMapper = new EmployeeMapper();
        this.promotionMapper = new PromotionMapper();
    }

    public Order toOrder(OrderDTO order) {

        if (order == null) return null;

        return Order.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .employee(employeeMapper.toEmployee(order.getEmployee()))
                .orderType(orderTypeMapper.toOrderType(order.getOrderType()))
                .customer(customerMapper.toCustomer(order.getCustomer()))
                .promotion(promotionMapper.toPromotion(order.getPromotion()))
                .bookings(order.getBookings().stream().map(bookingMapper::toBooking).collect(Collectors.toList()))
                .deposit(order.getDeposit())
                .employeePayment(employeeMapper.toEmployee(order.getEmployeePayment()))
                .createdAt(order.getCreatedAt())
                .paymentType(order.getPaymentType())
                .paymentDate(order.getPaymentDate())
                .build();
    }

    public OrderDTO toOrderDTO(Order order) {


        if (order == null) return null;
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .employee(employeeMapper.toDTO(order.getEmployee()))
                .orderType(orderTypeMapper.toOrderTypeDTO(order.getOrderType()))
                .customer(customerMapper.toDTO(order.getCustomer()))
                .promotion(promotionMapper.toDTO(order.getPromotion()))
                .bookings(order.getBookings().stream().map(bookingMapper::toDTO).collect(Collectors.toList()))
                .deposit(order.getDeposit())
                .createdAt(order.getCreatedAt())
                .employeePayment(employeeMapper.toDTO(order.getEmployeePayment()))
                .paymentDate(order.getPaymentDate())
                .paymentType(order.getPaymentType())
                .build();

    }

}
