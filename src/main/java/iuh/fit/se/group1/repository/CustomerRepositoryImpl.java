package iuh.fit.se.group1.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iuh.fit.se.group1.repository.interfaces.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

public class CustomerRepositoryImpl implements Repository<Customer, Long>, iuh.fit.se.group1.repository.interfaces.CustomerRepository {
    private static final Logger log = LoggerFactory.getLogger(CustomerRepositoryImpl.class);

    private final Connection connection;

    public CustomerRepositoryImpl() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Customer save(Customer entity) {
        String sql = "INSERT INTO Customer(fullName, phone,email,citizenId,gender,dateOfBirth, createdAt) VALUES (?, ?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPhone());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getCitizenId());
            preparedStatement.setBoolean(5, entity.isGender());
            preparedStatement.setDate(6, Date.valueOf(entity.getDateOfBirth()));
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(7, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setCustomerId(generatedKeys.getLong(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving Amenity: ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Customer findById(Long id) {
        String sql = "SELECT * FROM Customer WHERE customerId = ? and isDeleted = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(resultSet.getLong("customerId"));
                    customer.setFullName(resultSet.getString("fullName"));
                    customer.setPhone(resultSet.getString("phone"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setCitizenId(resultSet.getString("citizenId"));
                    customer.setGender(resultSet.getBoolean("gender"));
                    customer.setDateOfBirth(resultSet.getDate("dateOfBirth").toLocalDate());
                    customer.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return customer;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "Update Customer set isDeleted = 1 where customerId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE isDeleted = 0 ORDER BY customerId ASC, fullName ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(resultSet.getLong("customerId"));
                    customer.setFullName(resultSet.getString("fullName"));
                    customer.setPhone(resultSet.getString("phone"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setCitizenId(resultSet.getString("citizenId"));
                    customer.setGender(resultSet.getBoolean("gender"));
                    customer.setDateOfBirth(resultSet.getDate("dateOfBirth").toLocalDate());
                    customer.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public Customer update(Customer entity) {
        String sql = "UPDATE Customer SET fullName = ?, phone = ?, email = ?, citizenId = ?, gender = ?, dateOfBirth = ? WHERE customerId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPhone());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getCitizenId());
            preparedStatement.setBoolean(5, entity.isGender());
            preparedStatement.setDate(6, Date.valueOf(entity.getDateOfBirth()));
            preparedStatement.setLong(7, entity.getCustomerId());


            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findByCustomerNameOrId(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE (fullName COLLATE SQL_Latin1_General_CP1_CI_AS LIKE ? OR customerId LIKE ?) and isDeleted = 0 Order BY customerId ASC, fullName ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String likeKeyword = "%" + keyword + "%";
            preparedStatement.setString(1, likeKeyword);
            preparedStatement.setString(2, likeKeyword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(resultSet.getLong("customerId"));
                    customer.setFullName(resultSet.getString("fullName"));
                    customer.setPhone(resultSet.getString("phone"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setCitizenId(resultSet.getString("citizenId"));
                    customer.setGender(resultSet.getBoolean("gender"));
                    customer.setDateOfBirth(resultSet.getDate("dateOfBirth").toLocalDate());
                    customer.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public boolean isCitizenIdExists(String citizenId) {
        String sql = "SELECT 1 FROM Customer WHERE citizenId = ? AND isDeleted = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, citizenId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Customer findByCitizenId(String citizenId) {
        String sql = "SELECT * FROM Customer WHERE citizenId = ? AND isDeleted = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, citizenId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(resultSet.getLong("customerId"));
                    customer.setFullName(resultSet.getString("fullName"));
                    customer.setPhone(resultSet.getString("phone"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setCitizenId(resultSet.getString("citizenId"));
                    customer.setGender(resultSet.getBoolean("gender"));
                    customer.setDateOfBirth(resultSet.getDate("dateOfBirth").toLocalDate());
                    customer.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return customer;
                }
            }
        } catch (SQLException e) {
            log.info("Error finding Customer by citizenId: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
