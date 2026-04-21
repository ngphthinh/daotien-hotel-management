package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.repository.jpa.BookingRepositoryImpl;
import iuh.fit.se.group1.repository.jpa.OrderRepositoryImpl;
import iuh.fit.se.group1.repository.jpa.RoomRepositoryImpl;
import iuh.fit.se.group1.repository.interfaces.BookingRepository;
import iuh.fit.se.group1.repository.interfaces.RoomRepository;
import iuh.fit.se.group1.util.Constants;
import iuh.fit.se.group1.util.InvoiceItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class OrderService {
    private final OrderRepositoryImpl orderRepositoryImpl;
    private OrderDetailService orderDetailsService;
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository = new BookingRepositoryImpl();

    public OrderService() {
        this.orderDetailsService = new OrderDetailService();
        this.orderRepositoryImpl = new OrderRepositoryImpl();
        this.roomRepository = new RoomRepositoryImpl();
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


        order.setCreatedAt(LocalDate.now());

        // nếu 2 là đặt phòng thì cập nhật trạng thái phòng thành đang sử dụng
        if (order.getOrderType().getOrderTypeId() == 2) {
            List<Long> roomsIdx = order.getBookings().stream()
                    .map(booking -> booking.getRoom().getRoomId())
                    .toList();
            roomRepository.updateRoomStatusBatch(roomsIdx, RoomStatus.OCCUPIED);
        }

        // Lưu là đang sử li
        Order savedOrder = orderRepositoryImpl.save(order);
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


    private static final String SINGLE_ROOM_TYPE = "SINGLE";
    private static final String DOUBLE_ROOM_TYPE = "DOUBLE";

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


        Order savedOrder = orderRepositoryImpl.save(order);
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
        return orderRepositoryImpl.save(order);
    }

    /**
     * Move existing booking rows to another order by bookingId list.
     */
    public void moveBookingsToOrder(Long targetOrderId, List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) return;

        bookingRepository.moveBookingsToOrder(targetOrderId, bookingIds);
    }

    /**
     * Update total amount for a given order id.
     */
    public void updateOrderTotalAmount(Long orderId, java.math.BigDecimal amount) {
        orderRepositoryImpl.updateTotalAmount(orderId, amount);
    }

    public List<Order> getAllOrders() {
        return orderRepositoryImpl.findAll();
    }

    public List<Order> getAllOrdersWithRelationship() {
        return orderRepositoryImpl.findAllOrders();
    }


    public Order getOrderById(Long id) {
        return orderRepositoryImpl.findById(id);
    }


    public void updateOrderStatusToPaid(Order order) {
        orderRepositoryImpl.updateOrderStatusToPaid(order);
        List<Long> roomIds = order.getBookings().stream().map(e -> e.getRoom().getRoomId()).toList();
        roomRepository.updateRoomStatusBatch(roomIds, RoomStatus.AVAILABLE);
    }

    public void updateOrderDeposit(Long orderId, java.math.BigDecimal deposit) {
        orderRepositoryImpl.updateDeposit(orderId, deposit);
    }

    public void updateOrderType(Long orderId, Long newOrderTypeId) {
        orderRepositoryImpl.updateOrderType(orderId, newOrderTypeId);
    }

    public List<Order> getUnpaidOrders() {
        return orderRepositoryImpl.findAllByOrderUnPaid();
    }

    public List<Order> getUnpaidOrdersByKeyword(String keyword) {
        return orderRepositoryImpl.findUnpaidOrdersByKeyword(keyword);
    }

    public BigDecimal getTotalRevenueBetweenDates(LocalDate from, LocalDate to) {
        return orderRepositoryImpl.calculateTotalRevenueBetweenDates(from, to);
    }

    /**
     * Get revenue by room type for a date range
     *
     * @param from Start date
     * @param to   End date
     * @return Map with room type name as key and revenue as value
     */
    public Map<String, BigDecimal> getRevenueByRoomType(LocalDate from, LocalDate to) {
        return orderRepositoryImpl.getRevenueByRoomType(from, to);
    }

    /**
     * Lấy số lượng booking theo loại phòng cho một ngày cụ thể
     *
     * @param date Ngày cần lấy dữ liệu
     * @return Map với key là tên loại phòng, value là số lượng booking
     */
    public Map<String, Integer> getBookingCountByRoomTypeAndDate(LocalDate date) {
        return orderRepositoryImpl.getBookingCountByRoomTypeAndDate(date);
    }

    public void removeBookingsFromOrder(Order currentOrder, List<Booking> result) {
        bookingRepository.removeBookingsFromOrder(currentOrder, result);
    }


    public void recalculateOrderTotal(Long orderId) {
        Order order = orderRepositoryImpl.findById(orderId);
        if (order == null) {
            AppLogger.info("Order not found with id: " + orderId);
            return;
        }
//        lấy tổng tièn phòng
        List<Booking> bookings = order.getBookings();


        BigDecimal totalRoom = BigDecimal.valueOf(bookings.stream().mapToDouble(e -> bookingService.getPriceFromBooking(e)).sum());

//        lấy tổng tiền dịch vu
        BigDecimal totalAmenity = orderDetailsService.getOrderDetailsByOrderId(orderId).stream().map(OrderDetail::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
//        lấy tổng tiền phụ phí
        BigDecimal totalSurcharge = surchargeDetailService.getSurchargeDetailsByOrderId(orderId).stream().map(e -> e.getSurcharge().getPrice().multiply(BigDecimal.valueOf(e.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = totalAmenity.add(totalSurcharge).add(totalRoom).subtract(order.getDeposit());
        orderRepositoryImpl.updateTotalAmount(orderId, totalAmount);
    }

    private BookingService bookingService = new BookingService();
    private SurchargeDetailService surchargeDetailService = new SurchargeDetailService();

    public void deleteOrderById(Long id) {
        surchargeDetailService.deleteById(id);
        orderDetailsService.deleteById(id);
        bookingRepository.deleteByOrderId(id);
        orderRepositoryImpl.deleteById(id);
    }

    public List<Order> searchOrdersByKeyword(String searchText) {
        return orderRepositoryImpl.searchOrdersByKeyword(searchText);
    }

    public List<Order> getOrdersByRoomIdAndOrderType(Long roomId, Long orderTypeId) {
        return orderRepositoryImpl.findOrdersByRoomIdAndOrderType(roomId, orderTypeId);

    }

    public List<InvoiceItem> getInvoiceItems(Order order) {

        List<Booking> bookings = order.getBookings();
        String unitBookingType = getUnitBookingType(bookings.get(0).getBookingType());
        List<InvoiceItem> items = new ArrayList<>();
        int index = 1;
        int singleRooms = (int) bookings.stream()
                .filter(e -> e.getRoom().getRoomType().getRoomTypeId().equals(SINGLE_ROOM_TYPE))
                .count();

        int doubleRooms = (int) bookings.stream()
                .filter(e -> e.getRoom().getRoomTypeId().equals(DOUBLE_ROOM_TYPE))
                .count();

        if (singleRooms > 0) {
            double unitPrice = bookings.stream().filter(e -> e.getRoom().getRoomType().getRoomTypeId().equals(SINGLE_ROOM_TYPE))
                    .findFirst()
                    .map(e -> bookingService.getPriceFromBooking(e)).orElse(0.0);
            InvoiceItem singleRoomItem = new InvoiceItem(
                    index++,
                    "Phòng đơn",
                    unitBookingType,
                    Constants.VND_FORMAT.format(unitPrice),
                    singleRooms,
                    Constants.VND_FORMAT.format((unitPrice * singleRooms)));
            items.add(singleRoomItem);
        }

        if (doubleRooms > 0) {
            double unitPrice = bookings.stream().filter(e -> e.getRoom().getRoomType().getRoomTypeId().equals(DOUBLE_ROOM_TYPE))
                    .findFirst()
                    .map(e -> bookingService.getPriceFromBooking(e)).orElse(0.0);
            InvoiceItem doubleRoomItem = new InvoiceItem(
                    index++,
                    "Phòng đôi",
                    unitBookingType,
                    Constants.VND_FORMAT.format(unitPrice),
                    doubleRooms,
                    Constants.VND_FORMAT.format((unitPrice * doubleRooms)));
            items.add(doubleRoomItem);
        }

        List<OrderDetail> orderDetails = this.orderDetailsService.getOrderDetailsByOrderId(order.getOrderId());

        for (OrderDetail orderDetail : orderDetails) {
            InvoiceItem amenityItem = new InvoiceItem(
                    index++,
                    orderDetail.getAmenity().getNameAmenity(),
                    "Lần",
                    Constants.VND_FORMAT.format(orderDetail.getUnitPrice()),
                    orderDetail.getQuantity(),
                    Constants.VND_FORMAT.format(orderDetail.getUnitPrice().doubleValue() * orderDetail.getQuantity())
            );
            items.add(amenityItem);
        }

        List<SurchargeDetail> surchargeDetails = this.surchargeDetailService.getSurchargeDetailsByOrderId(order.getOrderId());
        for (SurchargeDetail surchargeDetail : surchargeDetails) {
            InvoiceItem surchargeItem = new InvoiceItem(
                    index++,
                    surchargeDetail.getSurcharge().getName(),
                    "Lần",
                    Constants.VND_FORMAT.format(surchargeDetail.getSurcharge().getPrice()),
                    surchargeDetail.getQuantity(),
                    Constants.VND_FORMAT.format(surchargeDetail.getSurcharge().getPrice().doubleValue() * surchargeDetail.getQuantity())
            );
            items.add(surchargeItem);
        }

        return items;
    }

    private String getUnitBookingType(BookingType bookingType) {
        return switch (bookingType) {
            case HOURLY -> "Giờ";
            case DAILY -> "Ngày";
            case OVERNIGHT -> "Đêm";
            default -> "";
        };
    }

    public List<Order> getOrdersUnPendingByKeyWord(String keyword) {

        return orderRepositoryImpl.findOrdersUnPendingByKeyWord(keyword);
    }

    public boolean addSurchargeToOrder(long orderId, long surchargeAmount) {
        if (surchargeAmount == 0) return true;

        return orderRepositoryImpl.addSurchargeToOrder(orderId, surchargeAmount);
    }

    public boolean existsTransformType(Long orderId) {
        return orderRepositoryImpl.existsTransformType(orderId);
    }

    public boolean addRoomAmountToOrder(long orderId, long amount) {
        return orderRepositoryImpl.addRoomAmountToOrder(orderId, amount);
    }

    public boolean subtractAmountFromOrder(long orderId, double amount) {
        return orderRepositoryImpl.subtractAmountFromOrder(orderId, amount);
    }

}
