package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.PaymentType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.repository.BookingRepository;
import iuh.fit.se.group1.repository.OrderRepository;
import iuh.fit.se.group1.repository.RoomRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private OrderDetailService orderDetailsService;
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository = new BookingRepository();

    public OrderService() {
        this.orderDetailsService = new OrderDetailService();
        this.orderRepository = new OrderRepository();
        this.roomRepository = new RoomRepository();
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


        // nếu 2 là đặt phòng thì cập nhật trạng thái phòng thành đang sử dụng
        if (order.getOrderType().getOrderTypeId() == 2) {
            List<Long> roomsIdx = order.getBookings().stream()
                    .map(booking -> booking.getRoom().getRoomId())
                    .toList();
            roomRepository.updateRoomStatusBatch(roomsIdx, RoomStatus.OCCUPIED);
        }

        // Lưu là đang sử li
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            AppLogger.info("Failed to save order");
            return null;
        }

        // save booking
        bookingRepository.saveAllBookingsForOrder(savedOrder, order.getBookings());


        if (orderDetailsService.saveOrderDetailsForOrder(savedOrder, orderDetails)) {
            AppLogger.info("Order created successfully with order details");
            return savedOrder;
        }
        return null;
    }


    public Order createOrderFromOrder(Order order) {
        if (order == null) {
            AppLogger.info("Order is null");
            return null;
        }

        if (order.getEmployee() == null || order.getCustomer() == null || order.getBookings() == null || order.getBookings().isEmpty()) {
            System.out.println("Employee :" + order.getEmployee());
            System.out.println("Customer :" + order.getCustomer());
            System.out.println("Bookings :" + order.getBookings());
            AppLogger.info(getClass() + " Order is missing required fields ");
            return null;
        }


        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            AppLogger.info("Failed to save order");
            return null;
        }

        bookingRepository.saveAllBookingsForOrder(savedOrder, order.getBookings());
        AppLogger.info("Order created successfully");
        return savedOrder;
    }

    /**
     * Create an empty order record (no bookings saved) based on the provided order object.
     * Returns the saved Order with generated orderId.
     */
    public Order createOrderRecord(Order order) {
        if (order == null) return null;
        if (order.getEmployee() == null || order.getCustomer() == null) return null;
        return orderRepository.save(order);
    }

    /**
     * Move existing booking rows to another order by bookingId list.
     */
    public void moveBookingsToOrder(Long targetOrderId, List<Long> bookingIds) {
        bookingRepository.moveBookingsToOrder(targetOrderId, bookingIds);
    }

    /**
     * Update total amount for a given order id.
     */
    public void updateOrderTotalAmount(Long orderId, java.math.BigDecimal amount) {
        orderRepository.updateTotalAmount(orderId, amount);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }



    public Order getOrderById(Long id) {
        return orderRepository.findById(id);
    }


    public void updateOrderStatusToPaid(Order order) {
        orderRepository.updateOrderStatusToPaid(order);
        List<Long> roomIds = order.getBookings().stream().map(e -> e.getRoom().getRoomId()).toList();
        roomRepository.updateRoomStatusBatch(roomIds, RoomStatus.AVAILABLE);
    }

    public void updateOrderDeposit(Long orderId, java.math.BigDecimal deposit) {
        orderRepository.updateDeposit(orderId, deposit);
    }

    public void updateOrderType(Long orderId, Long newOrderTypeId) {
        orderRepository.updateOrderType(orderId, newOrderTypeId);
    }

    public List<Order> getUnpaidOrders() {
        return orderRepository.findAllByOrderUnPaid();
    }

    public List<Order> getUnpaidOrdersByKeyword(String keyword) {
        return orderRepository.findUnpaidOrdersByKeyword(keyword);
    }

    public BigDecimal getTotalRevenueBetweenDates(LocalDate from, LocalDate to) {
        return orderRepository.calculateTotalRevenueBetweenDates(from, to);
    }

    public void removeBookingsFromOrder(Order currentOrder, List<Booking> result) {
        bookingRepository.removeBookingsFromOrder(currentOrder, result);
    }


    public void recalculateOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            AppLogger.info("Order not found with id: " + orderId);
            return;
        }
//        lấy tổng tièn phòng
        List<Booking> bookings = order.getBookings();


        BigDecimal totalRoom = BigDecimal.valueOf(bookings.stream().mapToDouble(e->bookingService.getPriceFromBooking(e)).sum());

//        lấy tổng tiền dịch vu
        BigDecimal totalAmenity = orderDetailsService.getOrderDetailsByOrderId(orderId).stream().map(OrderDetail::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
//        lấy tổng tiền phụ phí
        BigDecimal totalSurcharge = surchargeDetailService.getSurchargeDetailsByOrderId(orderId).stream().map(e -> e.getSurcharge().getPrice().multiply(BigDecimal.valueOf(e.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = totalAmenity.add(totalSurcharge).add(totalRoom).subtract(order.getDeposit());
        orderRepository.updateTotalAmount(orderId, totalAmount);
    }
    private BookingService bookingService = new  BookingService();
    private SurchargeDetailService surchargeDetailService = new SurchargeDetailService();

    public void deleteOrderById(Long id) {
        surchargeDetailService.deleteById(id);
        orderDetailsService.deleteById(id);
        bookingRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
    }

    public List<Order> searchOrdersByKeyword(String searchText) {
        return orderRepository.searchOrdersByKeyword(searchText);
    }

    public List<Order> getOrdersByRoomIdAndOrderType(Long roomId, Long orderTypeId) {
        return orderRepository.findOrdersByRoomIdAndOrderType(roomId, orderTypeId);

    }
}
