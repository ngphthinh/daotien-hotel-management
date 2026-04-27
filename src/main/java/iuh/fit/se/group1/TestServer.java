package iuh.fit.se.group1;

import iuh.fit.se.group1.dispatcher.Dispatcher;
import iuh.fit.se.group1.dispatcher.HandlerRegistry;
import iuh.fit.se.group1.handler.*;
import iuh.fit.se.group1.infrastructure.JPAUtil;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.SocketServer;
import iuh.fit.se.group1.service.*;

public class TestServer {
    public static void main(String[] args) {

        JPAUtil.getEntityManager();

        AuthenticateService authenticateService = new AuthenticateService();
        AccountService accountService = new AccountService();
        EmployeeService employeeService = new EmployeeService();
        AmenityService amenityService = new AmenityService();
        OrderService orderService = new OrderService();

        EmailSenderService emailSenderService = new EmailSenderService();
        BookingService bookingService = new BookingService();
        CustomerService customerService = new CustomerService();

        HandlerRegistry registry = new HandlerRegistry();


        CustomerHandler customerHandler = new CustomerHandler(customerService);
        BookingHandler bookingHandler = new BookingHandler(bookingService);
        AuthenticateHandler authenticateHandler = new AuthenticateHandler(authenticateService, accountService);
        EmployeeHandler employeeHandler = new EmployeeHandler(employeeService);
        AmenityHandler amenityHandler = new AmenityHandler(amenityService);
        OrderHandler orderHandler = new OrderHandler(orderService);

        EmailHandler emailHandler = new EmailHandler(emailSenderService);

        registerAuth(registry, authenticateHandler);
        registerEmployee(registry, employeeHandler);
        registerAmenity(registry, amenityHandler);
        registerOrder(registry, orderHandler);
        registerEmail(registry, emailHandler);
        registerBooking(registry, bookingHandler);
        registerCustomer(registry, customerHandler);

        Dispatcher dispatcher = new Dispatcher(registry);
        new SocketServer(dispatcher).start();
    }

    private static void registerCustomer(HandlerRegistry registry, CustomerHandler customerHandler) {
        registry.register(CommandType.CUSTOMER_GET_BY_ID, customerHandler);
        registry.register(CommandType.CUSTOMER_CREATE, customerHandler);
        registry.register(CommandType.CUSTOMER_UPDATE, customerHandler);
        registry.register(CommandType.CUSTOMER_DELETE, customerHandler);
        registry.register(CommandType.CUSTOMER_GET_ALL, customerHandler);
        registry.register(CommandType.CUSTOMER_GET_BY_KEYWORDS, customerHandler);
        registry.register(CommandType.CUSTOMER_GET_BY_CITIZEN, customerHandler);


    }

    private static void registerBooking(HandlerRegistry registry, BookingHandler bookingHandler) {
        registry.register(CommandType.BOOKING_GET_PRICE_FROM_BOOKING, bookingHandler);
    }

    private static void registerEmployee(HandlerRegistry registry, EmployeeHandler employeeHandler) {
        registry.register(CommandType.EMPLOYEE_GET_BY_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_ACCOUNT_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_ALL, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_KEYWORDS, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_CITIZEN_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_ROLE_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_CREATE, employeeHandler);
        registry.register(CommandType.EMPLOYEE_UPDATE, employeeHandler);
        registry.register(CommandType.EMPLOYEE_DELETE, employeeHandler);
    }

    private static void registerAmenity(HandlerRegistry registry, AmenityHandler amenityHandler) {
        registry.register(CommandType.AMENITY_GET_BY_ID, amenityHandler);
        registry.register(CommandType.AMENITY_GET_ALL, amenityHandler);
        registry.register(CommandType.AMENITY_GET_BY_KEYWORDS, amenityHandler);
        registry.register(CommandType.AMENITY_CREATE, amenityHandler);
        registry.register(CommandType.AMENITY_UPDATE, amenityHandler);
        registry.register(CommandType.AMENITY_DELETE, amenityHandler);
    }

    private static void registerOrder(HandlerRegistry registry, OrderHandler orderHandler) {
        registry.register(CommandType.ORDER_GET_BY_ID, orderHandler);
        registry.register(CommandType.ORDER_GET_ALL, orderHandler);
        registry.register(CommandType.ORDER_GET_ALL_WITH_RELATIONSHIP, orderHandler);
        registry.register(CommandType.ORDER_GET_UNPAID, orderHandler);
        registry.register(CommandType.ORDER_GET_UNPAID_BY_KEYWORD, orderHandler);
        registry.register(CommandType.ORDER_SEARCH_BY_KEYWORD, orderHandler);
        registry.register(CommandType.ORDER_CREATE, orderHandler);
        registry.register(CommandType.ORDER_UPDATE_STATUS_PAID, orderHandler);
        registry.register(CommandType.ORDER_UPDATE_DEPOSIT, orderHandler);
        registry.register(CommandType.ORDER_DELETE, orderHandler);

        registry.register(CommandType.ORDER_GET_REVENUE_BETWEEN_DATES_BY_ROOM_TYPE, orderHandler);
        registry.register(CommandType.ORDER_GET_REVENUE_BETWEEN_DATES_BOOKING_COUNT, orderHandler);
        registry.register(CommandType.ORDER_GET_REVENUE_BETWEEN_DATES, orderHandler);

        registry.register(CommandType.ORDER_CREATE_RECORD, orderHandler);
        registry.register(CommandType.ORDER_UPDATE_TOTAL_PRICE, orderHandler);
        registry.register(CommandType.ORDER_UPDATE_DEPOSIT, orderHandler);
        registry.register(CommandType.ORDER_MOVE_BOOKING_TO_ORDER, orderHandler);
        registry.register(CommandType.ORDER_RE_CALCULATE_TOTAL_PRICE, orderHandler);
        registry.register(CommandType.ORDER_UPDATE_ORDER_TYPE, orderHandler);


    }

    private static void registerAuth(HandlerRegistry registry, AuthenticateHandler authenticateHandler) {
        registry.register(CommandType.AUTH_LOGIN, authenticateHandler);
        registry.register(CommandType.AUTH_LOGOUT, authenticateHandler);
        registry.register(CommandType.AUTH_CHANGE_PASSWORD, authenticateHandler);
        registry.register(CommandType.AUTH_RESET_PASSWORD, authenticateHandler);
        registry.register(CommandType.AUTH_VALIDATE_MANAGER, authenticateHandler);
    }

    private static void registerEmail(HandlerRegistry registry, EmailHandler emailHandler) {
        registry.register(CommandType.EMAIL_SEND, emailHandler);
    }
}
