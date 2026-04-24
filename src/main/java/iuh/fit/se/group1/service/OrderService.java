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
import java.util.*;

public class OrderService extends Service {
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
//        EntityManager em = JPAUtil.getEntityManager();
//        EntityTransaction tx = em.getTransaction();
//
//        try {
//            tx.begin();
//
//            if (order == null) return null;
//
//            if (order.getEmployee() == null || order.getCustomer() == null
//                    || order.getBookings() == null || order.getBookings().isEmpty()) {
//                return null;
//            }
//
//            order.setCreatedAt(LocalDate.now());
//
//            if (order.getOrderType().getOrderTypeId() == 2) {
//                List<Long> roomsIdx = order.getBookings().stream()
//                        .map(b -> b.getRoom().getRoomId())
//                        .toList();
//
//                roomRepository.updateRoomStatusBatch(em, roomsIdx, RoomStatus.OCCUPIED);
//            }
//
//            Order savedOrder = orderRepositoryImpl.save(em, order);
//            if (savedOrder == null) return null;
//
//            bookingRepository.saveAllBookingsForOrder(em, savedOrder, order.getBookings());
//
//            if (orderDetailsService.saveOrderDetailsForOrder(em, savedOrder, orderDetails)) {
//                tx.commit();
//                return savedOrder;
//            }
//
//            tx.rollback();
//            return null;
//
//        } catch (Exception e) {
//            if (tx.isActive()) tx.rollback();
//            throw e;
//        } finally {
//            em.close();
//        }
        return doInTransaction(entityManager -> {
            if (order == null) return null;

            if (order.getEmployee() == null || order.getCustomer() == null
                    || order.getBookings() == null || order.getBookings().isEmpty()) {
                return null;
            }

            order.setCreatedAt(LocalDate.now());

            if (order.getOrderType().getOrderTypeId() == 2) {
                List<Long> roomsIdx = order.getBookings().stream()
                        .map(b -> b.getRoom().getRoomId())
                        .toList();

                roomRepository.updateRoomStatusBatch(entityManager, roomsIdx, RoomStatus.OCCUPIED);
            }

            Order savedOrder = orderRepositoryImpl.save(entityManager, order);
            if (savedOrder == null) return null;

            bookingRepository.saveAllBookingsForOrder(entityManager, savedOrder, order.getBookings());

            if (orderDetailsService.saveOrderDetailsForOrder(entityManager, savedOrder, orderDetails)) {
                return savedOrder;
            }

            throw new RuntimeException("Failed to save order details");
        });
    }

    private static final String SINGLE_ROOM_TYPE = "SINGLE";
    private static final String DOUBLE_ROOM_TYPE = "DOUBLE";

    public Order createOrderFromOrder(Order order) {
//        if (order == null) {
//            AppLogger.info("Order is null");
//            return null;
//        }
//
//        if (order.getEmployee() == null || order.getCustomer() == null || order.getBookings() == null || order.getBookings().isEmpty()) {
//            System.out.println("Employee :" + order.getEmployee());
//            System.out.println("Customer :" + order.getCustomer());
//            System.out.println("Bookings :" + order.getBookings());
//            AppLogger.info(getClass() + " Order is missing required fields ");
//            return null;
//        }
//
//
//        Order savedOrder = orderRepositoryImpl.save(order);
//        if (savedOrder == null) {
//            AppLogger.info("Failed to save order");
//            return null;
//        }
//
//        bookingRepository.saveAllBookingsForOrder(savedOrder, order.getBookings());
//        AppLogger.info("Order created successfully");
//        return savedOrder;
        return doInTransaction(entityManager -> {
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

            Order savedOrder = orderRepositoryImpl.save(entityManager, order);
            if (savedOrder == null) {
                AppLogger.info("Failed to save order");
                return null;
            }

            bookingRepository.saveAllBookingsForOrder(entityManager, savedOrder, order.getBookings());
            AppLogger.info("Order created successfully");
            return savedOrder;
        });
    }

    /**
     * Create an empty order record (no bookings saved) based on the provided order object.
     * Returns the saved Order with generated orderId.
     */
    public Order createOrderRecord(Order order) {
        if (order == null) return null;
        if (order.getEmployee() == null || order.getCustomer() == null) return null;
//        return orderRepositoryImpl.save(order);
        return doInTransaction(entityManager -> orderRepositoryImpl.save(entityManager, order));
    }

    /**
     * Move existing booking rows to another order by bookingId list.
     */
    public void moveBookingsToOrder(Long targetOrderId, List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) return;

        doInTransactionVoid(entityManager -> bookingRepository.moveBookingsToOrder(entityManager, targetOrderId, bookingIds));
    }

    /**
     * Update total amount for a given order id.
     */
    public void updateOrderTotalAmount(Long orderId, java.math.BigDecimal amount) {
//        orderRepositoryImpl.updateTotalAmount(orderId, amount);
        doInTransactionVoid(entityManager -> orderRepositoryImpl.updateTotalAmount(entityManager, orderId, amount));
    }

    public List<Order> getAllOrders() {
//        return orderRepositoryImpl.findAll();
        return doInTransaction(orderRepositoryImpl::findAll);
    }

    public List<Order> getAllOrdersWithRelationshipAndCompleteYet() {
//        return orderRepositoryImpl.findAllOrdersCompleteYet();
        return doInTransaction(orderRepositoryImpl::findAllOrdersCompleteYet);
    }

    public List<Order> getAllOrdersWithRelationship() {
//        return orderRepositoryImpl.findAllOrdersWithRelationship();
        return doInTransaction(orderRepositoryImpl::findAllOrdersWithRelationship);
    }


    public Order getOrderById(Long id) {
//        return orderRepositoryImpl.findById(id);
        return doInTransaction(entityManager -> orderRepositoryImpl.findById(entityManager, id));
    }


    public void updateOrderStatusToPaid(Order order) {
//        orderRepositoryImpl.updateOrderStatusToPaid(order);
//        List<Long> roomIds = order.getBookings().stream().map(e -> e.getRoom().getRoomId()).toList();
//        roomRepository.updateRoomStatusBatch(roomIds, RoomStatus.AVAILABLE);
        doInTransactionVoid(entityManager -> {
            orderRepositoryImpl.updateOrderStatusToPaid(entityManager, order);
            List<Long> roomIds = order.getBookings().stream().map(e -> e.getRoom().getRoomId()).toList();
            roomRepository.updateRoomStatusBatch(entityManager, roomIds, RoomStatus.AVAILABLE);
        });
    }

    public void updateOrderDeposit(Long orderId, java.math.BigDecimal deposit) {
//        orderRepositoryImpl.updateDeposit(orderId, deposit);
        doInTransactionVoid(entityManager -> orderRepositoryImpl.updateDeposit(entityManager, orderId, deposit));
    }

    public void updateOrderType(Long orderId, Long newOrderTypeId) {
//        orderRepositoryImpl.updateOrderType(orderId, newOrderTypeId);
        doInTransactionVoid(entityManager -> orderRepositoryImpl.updateOrderType(entityManager, orderId, newOrderTypeId));
    }

    public List<Order> getUnpaidOrders() {
//        return orderRepositoryImpl.findAllByOrderUnPaid();
        return doInTransaction(orderRepositoryImpl::findAllByOrderUnPaid);
    }

    public List<Order> getUnpaidOrdersByKeyword(String keyword) {
//        return orderRepositoryImpl.findUnpaidOrdersByKeyword(keyword);
        return doInTransaction(entityManager -> orderRepositoryImpl.findUnpaidOrdersByKeyword(entityManager, keyword));
    }

    public BigDecimal getTotalRevenueBetweenDates(LocalDate from, LocalDate to) {
//        return orderRepositoryImpl.calculateTotalRevenueBetweenDates(from, to);
        return doInTransaction(entityManager -> orderRepositoryImpl.calculateTotalRevenueBetweenDates(entityManager, from, to));
    }

    /**
     * Get revenue by room type for a date range
     *
     * @param from Start date
     * @param to   End date
     * @return Map with room type name as key and revenue as value
     */
    public Map<String, BigDecimal> getRevenueByRoomType(LocalDate from, LocalDate to) {
//        return orderRepositoryImpl.getRevenueByRoomType(from, to);
        return doInTransaction(entityManager -> orderRepositoryImpl.getRevenueByRoomType(entityManager, from, to));
    }

    /**
     * Lấy số lượng booking theo loại phòng cho một ngày cụ thể
     *
     * @param date Ngày cần lấy dữ liệu
     * @return Map với key là tên loại phòng, value là số lượng booking
     */
    public Map<String, Integer> getBookingCountByRoomTypeAndDate(LocalDate date) {
//        return orderRepositoryImpl.getBookingCountByRoomTypeAndDate(date);
        return doInTransaction(entityManager -> orderRepositoryImpl.getBookingCountByRoomTypeAndDate(entityManager, date));
    }

    public void removeBookingsFromOrder(Order currentOrder, List<Booking> result) {
//        bookingRepository.removeBookingsFromOrder(currentOrder, result);
        doInTransactionVoid(entityManager -> bookingRepository.removeBookingsFromOrder(entityManager, currentOrder, result));
    }


    public void recalculateOrderTotal(Long orderId) {
//        Order order = orderRepositoryImpl.findById(orderId);
//        if (order == null) {
//            AppLogger.info("Order not found with id: " + orderId);
//            return;
//        }
////        lấy tổng tièn phòng
//        List<Booking> bookings = order.getBookings();
//
//
//        BigDecimal totalRoom = BigDecimal.valueOf(bookings.stream().mapToDouble(e -> bookingService.getPriceFromBooking(e)).sum());
//
////        lấy tổng tiền dịch vu
//        BigDecimal totalAmenity = orderDetailsService.getOrderDetailsByOrderId(orderId).stream().map(OrderDetail::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
////        lấy tổng tiền phụ phí
//        BigDecimal totalSurcharge = surchargeDetailService.getSurchargeDetailsByOrderId(orderId).stream().map(e -> e.getSurcharge().getPrice().multiply(BigDecimal.valueOf(e.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        BigDecimal totalAmount = totalAmenity.add(totalSurcharge).add(totalRoom).subtract(order.getDeposit());
//        orderRepositoryImpl.updateTotalAmount(orderId, totalAmount);
        doInTransactionVoid(entityManager -> {
            Order order = orderRepositoryImpl.findById(entityManager, orderId);
            if (order == null) {
                AppLogger.info("Order not found with id: " + orderId);
                return;
            }
            // lấy tổng tièn phòng
            List<Booking> bookings = order.getBookings();

            BigDecimal totalRoom = BigDecimal.valueOf(bookings.stream().mapToDouble(e -> bookingService.getPriceFromBooking(e)).sum());

            // lấy tổng tiền dịch vu
            BigDecimal totalAmenity = orderDetailsService.getOrderDetailsByOrderId(orderId).stream().map(OrderDetail::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            // lấy tổng tiền phụ phí
            BigDecimal totalSurcharge = surchargeDetailService.getSurchargeDetailsByOrderId(orderId).stream().map(e -> e.getSurcharge().getPrice().multiply(BigDecimal.valueOf(e.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalAmount = totalAmenity.add(totalSurcharge).add(totalRoom).subtract(order.getDeposit());
            orderRepositoryImpl.updateTotalAmount(entityManager, orderId, totalAmount);
        });
    }

    private BookingService bookingService = new BookingService();
    private SurchargeDetailService surchargeDetailService = new SurchargeDetailService();

    public void deleteOrderById(Long id) {
        surchargeDetailService.deleteById(id);
        orderDetailsService.deleteById(id);
        doInTransactionVoid(em -> {
            bookingRepository.deleteByOrderId(em, id);
            orderRepositoryImpl.deleteById(em, id);
        });
    }

    public List<Order> searchOrdersByKeyword(String searchText) {
//        return orderRepositoryImpl.searchOrdersByKeyword(searchText);
        return doInTransaction(entityManager -> orderRepositoryImpl.searchOrdersByKeyword(entityManager, searchText));
    }

    public List<Order> getOrdersByRoomIdAndOrderType(Long roomId, Long orderTypeId) {
//        return orderRepositoryImpl.findOrdersByRoomIdAndOrderType(roomId, orderTypeId);

        return doInTransaction(entityManager -> orderRepositoryImpl.findOrdersByRoomIdAndOrderType(entityManager, roomId, orderTypeId));
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

//        return orderRepositoryImpl.findOrdersUnPendingByKeyWord(keyword);
        return doInTransaction(entityManager -> orderRepositoryImpl.findOrdersUnPendingByKeyWord(entityManager, keyword));
    }

    public boolean addSurchargeToOrder(long orderId, long surchargeAmount) {
        if (surchargeAmount == 0) return true;

//        return orderRepositoryImpl.addSurchargeToOrder(orderId, surchargeAmount);
        return doInTransaction(entityManager -> orderRepositoryImpl.addSurchargeToOrder(entityManager, orderId, surchargeAmount));
    }

    public boolean existsTransformType(Long orderId) {
//        return orderRepositoryImpl.existsTransformType(orderId);
        return doInTransaction(entityManager -> orderRepositoryImpl.existsTransformType(entityManager, orderId));
    }

    public boolean addRoomAmountToOrder(long orderId, long amount) {
//        return orderRepositoryImpl.addRoomAmountToOrder(orderId, amount);
        return doInTransaction(entityManager -> orderRepositoryImpl.addRoomAmountToOrder(entityManager, orderId, amount));
    }

    public boolean subtractAmountFromOrder(long orderId, double amount) {
//        return orderRepositoryImpl.subtractAmountFromOrder(orderId, amount);
        return doInTransaction(entityManager -> orderRepositoryImpl.subtractAmountFromOrder(entityManager, orderId, amount));
    }

}
