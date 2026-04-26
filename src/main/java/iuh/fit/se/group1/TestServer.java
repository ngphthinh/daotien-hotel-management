package iuh.fit.se.group1;

import iuh.fit.se.group1.dispatcher.Dispatcher;
import iuh.fit.se.group1.dispatcher.HandlerRegistry;
import iuh.fit.se.group1.handler.AmenityHandler;
import iuh.fit.se.group1.handler.AuthenticateHandler;
import iuh.fit.se.group1.handler.EmployeeHandler;
import iuh.fit.se.group1.handler.OrderHandler;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.SocketServer;
import iuh.fit.se.group1.service.AccountService;
import iuh.fit.se.group1.service.AmenityService;
import iuh.fit.se.group1.service.AuthenticateService;
import iuh.fit.se.group1.service.EmployeeService;
import iuh.fit.se.group1.service.OrderService;

public class TestServer {
    public static void main(String[] args) {

        AuthenticateService authenticateService = new AuthenticateService();
        AccountService accountService = new AccountService();
        EmployeeService employeeService = new EmployeeService();
        AmenityService amenityService = new AmenityService();
        OrderService orderService = new OrderService();
        
        HandlerRegistry registry = new HandlerRegistry();

        AuthenticateHandler authenticateHandler = new AuthenticateHandler(authenticateService, accountService);
        EmployeeHandler employeeHandler = new EmployeeHandler(employeeService);
        AmenityHandler amenityHandler = new AmenityHandler(amenityService);
        OrderHandler orderHandler = new OrderHandler(orderService);

        registerAuth(registry, authenticateHandler);
        registerEmployee(registry, employeeHandler);
        registerAmenity(registry, amenityHandler);
        registerOrder(registry, orderHandler);

        Dispatcher dispatcher = new Dispatcher(registry);
        new SocketServer(dispatcher).start();
    }

    private static void registerEmployee(HandlerRegistry registry, EmployeeHandler employeeHandler) {
        registry.register(CommandType.EMPLOYEE_GET_BY_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_ACCOUNT_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_ALL, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_KEYWORDS, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_CITIZEN_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_GET_BY_ROLE_ID, employeeHandler);
        registry.register(CommandType.EMPLOYEE_CREATE, employeeHandler);
        registry.register(CommandType.EMPLOYEE_UPDATE, employeeHandler);
        registry.register(CommandType.EMPLOYEE_DELETE, employeeHandler);
    }

    private static void registerAmenity(HandlerRegistry registry, AmenityHandler amenityHandler) {
        registry.register(CommandType.AMENITY_GET_BY_ID, amenityHandler);
        registry.register(CommandType.AMENITY_GET_BY_ALL, amenityHandler);
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
    }

    private static void registerAuth(HandlerRegistry registry, AuthenticateHandler authenticateHandler) {
        registry.register(CommandType.AUTH_LOGIN, authenticateHandler);
        registry.register(CommandType.AUTH_LOGOUT, authenticateHandler);
        registry.register(CommandType.AUTH_CHANGE_PASSWORD, authenticateHandler);
        registry.register(CommandType.AUTH_RESET_PASSWORD, authenticateHandler);
        registry.register(CommandType.AUTH_VALIDATE_MANAGER, authenticateHandler);

    }
}
