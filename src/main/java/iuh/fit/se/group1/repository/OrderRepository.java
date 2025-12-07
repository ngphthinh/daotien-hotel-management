package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.PaymentType;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository implements Repository<Order, Long> {

    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    private final CustomerRepository customerRepository;
    private final Connection connection;

    public OrderRepository() {
        this.customerRepository = new CustomerRepository();
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public Order save(Order entity) {
        String sql = "INSERT INTO Orders (orderDate, totalAmount ,employeeId, orderTypeId, customerId, promotionId, deposit) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getOrderDate()));
            preparedStatement.setBigDecimal(2, entity.getTotalAmount()); // totalAmount ban đầu là 0
            preparedStatement.setLong(3, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(4, entity.getOrderType().getOrderTypeId());

            if (entity.getCustomer() != null) {
                Customer existingCustomer = customerRepository.findByCitizenId(entity.getCustomer().getCitizenId());
                if (existingCustomer == null) {
                    Customer customerSave = customerRepository.save(entity.getCustomer());
                    preparedStatement.setLong(5, customerSave.getCustomerId());
                } else {
                    preparedStatement.setLong(5, existingCustomer.getCustomerId());
                }
            }

            if (entity.getPromotion() != null) {
                preparedStatement.setLong(6, entity.getPromotion().getPromotionId());
            } else {
                preparedStatement.setNull(6, java.sql.Types.BIGINT);
            }

            preparedStatement.setBigDecimal(7, entity.getDeposit());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setOrderId(generatedKeys.getLong(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving Order: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findById(Long aLong) {
        String sql = """
                SELECT
                    O.orderId, O.orderDate, O.totalAmount, O.employeeId,
                    O.orderTypeId, O.customerId, O.promotionId, O.deposit, O.createdAt AS orderCreatedAt,
                    B.bookingId, B.checkInDate, B.checkOutDate,B.bookingType,
                    B.orderId AS bookingOrderId, B.roomId, B.bookingType, B.createdAt AS bookingCreatedAt,
                    OT.orderTypeId AS otId, OT.name AS otName, OT.createdAt AS otCreatedAt,email, fullName, gender,phone,dateOfBirth,citizenId, rt.name as rtName, rt.roomTypeId,
                    R.roomNumber AS rRoomNumber
                FROM Orders O
                JOIN Booking B ON O.orderId = B.orderId
                JOIN OrderType OT ON O.orderTypeId = OT.orderTypeId
                JOIN Room R ON B.roomId = R.roomId
                JOIN Customer C on O.customerId = C.customerId
                JOIN RoomType rt on R.roomTypeId = rt.roomTypeId
                WHERE O.orderId = ?
                
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, aLong);

            try (ResultSet rs = ps.executeQuery()) {
                Order order = null; // ✅ tạo sau khi có dòng đầu tiên

                while (rs.next()) {
                    if (order == null) {
                        // 🔹 Map OrderType
                        OrderType orderType = new OrderType();
                        orderType.setOrderTypeId(rs.getLong("otId"));
                        orderType.setName(rs.getString("otName"));

                        // 🔹 Map Customer
                        Customer customer = new Customer();
                        customer.setCustomerId(rs.getLong("customerId"));
                        customer.setFullName(rs.getString("fullName"));
                        customer.setGender(rs.getBoolean("gender"));
                        customer.setPhone(rs.getString("phone"));
                        customer.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
                        customer.setCitizenId(rs.getString("citizenId"));
                        customer.setEmail(rs.getString("email"));

                        // 🔹 Map Order
                        order = new Order();
                        order.setOrderId(rs.getLong("orderId"));
                        order.setOrderDate(rs.getTimestamp("orderDate").toLocalDateTime());
                        order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                        order.setDeposit(rs.getBigDecimal("deposit"));
                        order.setOrderType(orderType);
                        order.setCustomer(customer);
                    }

                    // 🔹 Map Booking
                    RoomType roomType = new RoomType();
                    roomType.setRoomTypeId(rs.getString("roomTypeId"));
                    roomType.setName(rs.getString("rtName"));

                    Room room = new Room();
                    room.setRoomId(rs.getLong("roomId"));
                    room.setRoomNumber(rs.getString("rRoomNumber"));
                    room.setRoomType(roomType);

                    Booking booking = new Booking();
                    booking.setBookingId(rs.getLong("bookingId"));
                    booking.setCheckInDate(rs.getTimestamp("checkInDate").toLocalDateTime());
                    booking.setCheckOutDate(rs.getTimestamp("checkOutDate").toLocalDateTime());
                    booking.setBookingType(BookingType.fromString(rs.getString("bookingType")));
                    booking.setRoom(room);

                    order.addBooking(booking); // ✅ thêm từng booking vào order
                }

                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<Order> findAll() {
        String sql = """
                SELECT
                    O.orderId, O.orderDate, O.totalAmount, O.employeeId,
                    O.orderTypeId, O.customerId, O.promotionId, O.deposit, O.createdAt AS orderCreatedAt,
                    B.bookingId, B.checkInDate, B.checkOutDate,
                    B.orderId AS bookingOrderId, B.roomId, B.bookingType, B.createdAt AS bookingCreatedAt,
                    OT.orderTypeId AS otId, OT.name AS otName, OT.createdAt AS otCreatedAt, R.roomNumber AS rRoomNumber
                FROM Orders O
                JOIN Booking B ON O.orderId = B.orderId
                JOIN OrderType OT ON O.orderTypeId = OT.orderTypeId
                JOIN Room R ON B.roomId = R.roomId
                """;

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Map<Long, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                long orderId = rs.getLong("orderId");
                Order order = orderMap.get(orderId);
                if (order == null) {
                    order = new Order();
                    order.setOrderId(orderId);
                    order.setOrderDate(rs.getTimestamp("orderDate").toLocalDateTime());
                    order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                    order.setDeposit(rs.getBigDecimal("deposit"));

                    long orderTypeId = rs.getLong("otId");
                    OrderType orderType = new OrderType();
                    orderType.setOrderTypeId(orderTypeId);
                    orderType.setName(rs.getString("otName"));
                    order.setOrderType(orderType);

                    long customerId = rs.getLong("customerId");
                    if (!rs.wasNull()) {
                        Customer customer = customerRepository.findById(customerId);
                        order.setCustomer(customer);
                    }

                    order.setBookings(new ArrayList<>());
                    orderMap.put(orderId, order);
                }

                // Ánh xạ Booking
                Booking booking = new Booking();
                booking.setBookingId(rs.getLong("bookingId"));
                booking.setOrder(order);

                // Lấy Room
                Room room = new Room();
                room.setRoomId(rs.getLong("roomId"));
                room.setRoomNumber(rs.getString("rRoomNumber"));
                booking.setRoom(room);

                order.getBookings().add(booking);
            }

            orders.addAll(orderMap.values());
            return orders;

        } catch (SQLException e) {
            log.error("Error retrieving Orders with Bookings and OrderType: ", e);
            throw new RuntimeException(e);
        }
    }


    public List<BookingDisplayDTO> findAllBookingDisplay() {
        String sql = """
                SELECT
                    B.bookingId,
                    R.roomNumber,
                    C.fullName AS customerName,
                    C.phone
                FROM Booking B
                JOIN Room R ON B.roomId = R.roomId
                JOIN Orders O ON B.orderId = O.orderId
                JOIN Customer C ON O.customerId = C.customerId
                WHERE orderTypeId = 2
                """;

        List<BookingDisplayDTO> list = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long bookingId = rs.getLong("bookingId");
                String roomNumber = rs.getString("roomNumber");
                String customerName = rs.getString("customerName");
                String phoneNumber = rs.getString("phone");
                BookingDisplayDTO dto = new BookingDisplayDTO(bookingId, roomNumber, customerName, phoneNumber);
                list.add(dto);
            }

        } catch (SQLException e) {
            log.error("Error retrieving booking display list: ", e);
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public Order update(Order entity) {
        return null;
    }

    public void updateTotalAmount(Long orderId, BigDecimal totalAmount) {
        String sql = "UPDATE Orders SET totalAmount = ? WHERE orderId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, totalAmount);
            preparedStatement.setLong(2, orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating total amount for Order ID {}: ", orderId, e);
            throw new RuntimeException(e);
        }
    }

    public List<Order> findAllByOrderUnPaid() {
        String sql = """
               
                SELECT O.orderId, B.checkInDate, B.checkOutDate, O.totalAmount, C.phone, C.fullName, R.roomNumber
                FROM Orders O
                JOIN Booking B ON O.orderId = B.orderId
                JOIN OrderType OT ON O.orderTypeId = OT.orderTypeId
                JOIN Room R ON B.roomId = R.roomId
                Join Customer C ON O.customerId = C.customerId
                WHERE O.orderTypeId = 2
                """;

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Map<Long, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                long orderId = rs.getLong("orderId");
                Order order = orderMap.get(orderId);
                if (order == null) {
                    order = new Order();
                    order.setOrderId(orderId);
                    order.setTotalAmount(rs.getBigDecimal("totalAmount"));

                    if (!rs.wasNull()) {
                        Customer customer = new Customer();
                        customer.setFullName(rs.getString("fullName"));
                        customer.setPhone(rs.getString("phone"));
                        order.setCustomer(customer);
                    }

                    order.setBookings(new ArrayList<>());
                    orderMap.put(orderId, order);
                }

                // Ánh xạ Booking
                Booking booking = new Booking();
                booking.setCheckInDate(rs.getTimestamp("checkInDate").toLocalDateTime());
                booking.setCheckOutDate(rs.getTimestamp("checkOutDate").toLocalDateTime());

                // Lấy Room
                Room room = new Room();
                room.setRoomNumber(rs.getString("roomNumber"));
                booking.setRoom(room);
                order.getBookings().add(booking);
            }

            orders.addAll(orderMap.values());
            return orders;

        } catch (SQLException e) {
            log.error("Error retrieving Orders with Bookings and OrderType: ", e);
            throw new RuntimeException(e);
        }
    }

    public void updateOrderStatusToPaid(Order order) {
            String sql = "UPDATE Orders SET orderTypeId = 1, totalAmount = ?, paymentType = ?, promotionId = ? WHERE orderId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, order.getTotalAmount());
            preparedStatement.setString(2, order.getPaymentType().name());
            if (order.getPromotion() != null) {
                preparedStatement.setLong(3, order.getPromotion().getPromotionId());
            } else {
                preparedStatement.setNull(3, java.sql.Types.BIGINT);
            }
            preparedStatement.setLong(4, order.getOrderId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating order status to paid for Order ID {}: ", order.getOrderId(), e);
            throw new RuntimeException(e);
        }
    }
}
