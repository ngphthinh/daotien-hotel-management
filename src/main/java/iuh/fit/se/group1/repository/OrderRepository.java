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
    private final BookingRepository bookingRepository;
    private final Connection connection;

    public OrderRepository() {
        this.customerRepository = new CustomerRepository();
        this.bookingRepository = new BookingRepository();
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public Order save(Order entity) {
        String sql = "INSERT INTO Orders (orderDate, employeeId, orderTypeId, customerId, promotionId, deposit) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getOrderDate()));
            preparedStatement.setLong(2, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(3, entity.getOrderType().getOrderTypeId());

            if (entity.getCustomer() != null) {
                Customer existingCustomer = customerRepository.findByCitizenId(entity.getCustomer().getCitizenId());
                if (existingCustomer == null) {
                    Customer customerSave = customerRepository.save(entity.getCustomer());
                    preparedStatement.setLong(4, customerSave.getCustomerId());
                } else {
                    preparedStatement.setLong(4, existingCustomer.getCustomerId());
                }
            }

            if (entity.getPromotion() != null) {
                preparedStatement.setLong(5, entity.getPromotion().getPromotionId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.BIGINT);
            }

            preparedStatement.setBigDecimal(6, entity.getDeposit());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setOrderId(generatedKeys.getLong(1));
                }
            }


            if (entity.getBookings() != null && !entity.getBookings().isEmpty()) {
                List<Booking> bookingsSave = new ArrayList<>();
                for (var booking : entity.getBookings()) {
                    bookingsSave.add(bookingRepository.save(booking));
                }
                entity.setBookings(bookingsSave);
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
                    B.bookingId, B.checkInDate, B.checkOutDate, B.employeeId AS bookingEmployeeId,
                    B.orderId AS bookingOrderId, B.roomId, B.bookingType, B.totalPrice, B.createdAt AS bookingCreatedAt,
                    OT.orderTypeId AS otId, OT.name AS otName, OT.createdAt AS otCreatedAt, fullName, gender,phone, rt.name as rtName, rt.roomTypeId,
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
                    booking.setBookingType(BookingType.valueOf(rs.getString("bookingType")));
                    booking.setTotalPrice(rs.getBigDecimal("totalPrice"));
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
                    B.bookingId, B.checkInDate, B.checkOutDate, B.employeeId AS bookingEmployeeId,
                    B.orderId AS bookingOrderId, B.roomId, B.bookingType, B.totalPrice, B.createdAt AS bookingCreatedAt,
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
                booking.setCheckInDate(rs.getTimestamp("checkInDate").toLocalDateTime());
                booking.setCheckOutDate(rs.getTimestamp("checkOutDate").toLocalDateTime());
                booking.setBookingType(BookingType.valueOf(rs.getString("bookingType")));
                booking.setTotalPrice(rs.getBigDecimal("totalPrice"));
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
                SELECT
                    O.orderId, O.orderDate, O.totalAmount, O.employeeId,
                    O.orderTypeId, O.customerId, O.promotionId, O.deposit, O.createdAt AS orderCreatedAt,
                    B.bookingId, B.checkInDate, B.checkOutDate, B.employeeId AS bookingEmployeeId,
                    B.orderId AS bookingOrderId, B.roomId, B.bookingType, B.totalPrice, B.createdAt AS bookingCreatedAt,
                    OT.orderTypeId AS otId, OT.name AS otName, OT.createdAt AS otCreatedAt, R.roomNumber AS rRoomNumber
                FROM Orders O
                JOIN Booking B ON O.orderId = B.orderId
                JOIN OrderType OT ON O.orderTypeId = OT.orderTypeId
                JOIN Room R ON B.roomId = R.roomId
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
                booking.setCheckInDate(rs.getTimestamp("checkInDate").toLocalDateTime());
                booking.setCheckOutDate(rs.getTimestamp("checkOutDate").toLocalDateTime());
                booking.setBookingType(BookingType.valueOf(rs.getString("bookingType")));
                booking.setTotalPrice(rs.getBigDecimal("totalPrice"));
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

    public void updateOrderStatusToPaid(Long aLong, PaymentType eWallet, BigDecimal totalAmount) {
        String sql = "UPDATE Orders SET orderTypeId = 1, totalAmount = ?, paymentType = ? WHERE orderId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, totalAmount);
            preparedStatement.setString(2, eWallet.name());
            preparedStatement.setLong(3, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating order status to paid for Order ID {}: ", aLong, e);
            throw new RuntimeException(e);
        }
    }
}
