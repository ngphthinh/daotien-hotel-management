package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements Repository<Order, Long> {

    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    private final CustomerRepository customerRepository ;
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getOrderDate()));
            preparedStatement.setLong(2, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(3, entity.getOrderType().getOrderTypeId());

            if (entity.getCustomer() != null) {
                Customer existingCustomer = customerRepository.findByCitizenId(entity.getCustomer().getCitizenId());
                if (existingCustomer == null) {
                    Customer customerSave =  customerRepository.save(entity.getCustomer());
                    preparedStatement.setLong(4, customerSave.getCustomerId());
                }else {
                    preparedStatement.setLong(4, existingCustomer.getCustomerId());
                }
            }

            if (entity.getPromotion() != null) {
                preparedStatement.setLong(5, entity.getPromotion().getPromotionId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.BIGINT);
            }

            preparedStatement.setBigDecimal(6,  entity.getDeposit());
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
        } catch ( SQLException e) {
            log.error("Error saving Order: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public Order update(Order entity) {
        return null;
    }

}
