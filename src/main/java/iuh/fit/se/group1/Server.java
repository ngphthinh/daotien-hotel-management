package iuh.fit.se.group1;

import iuh.fit.se.group1.config.InitData;
import iuh.fit.se.group1.dispatcher.Dispatcher;
import iuh.fit.se.group1.dispatcher.HandlerRegistry;
import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.handler.*;
import iuh.fit.se.group1.infrastructure.JPAUtil;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.SocketServer;
import iuh.fit.se.group1.service.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class Server {
    public static void main(String[] args) {

        JPAUtil.getEntityManager();
        System.out.println("Đang tải dữ liệu ứng dụng...");
        EmployeeService employeeService = new EmployeeService();
        if (employeeService.count() == 0) {
            InitData.initAllData();
            EmployeeDTO admin = new EmployeeDTO();
            admin.setFullName("Quản Trị Viên Admin");
            admin.setPhone("0123456789");
            admin.setHireDate(LocalDate.now());
            admin.setEmail("nguyenphuocthinh0710@gmail.com");
            admin.setGender(false);
            admin.setCitizenId("082205000819");
            EmployeeDTO employee = employeeService.createEmployee(admin, Role.MANAGER.toString());
            if (employee == null) {
                System.out.println("Không tạo được tài khoản");
            } else {
                System.out.println(employee);
            }
        }
        AuthenticateService authenticateService = new AuthenticateService();
        AccountService accountService = new AccountService();
        AmenityService amenityService = new AmenityService();
        OrderService orderService = new OrderService();

        EmailSenderService emailSenderService = new EmailSenderService();
        BookingService bookingService = new BookingService();
        CustomerService customerService = new CustomerService();

        JaspersoftExportService jaspersoftExportService = new JaspersoftExportService();
        OrderDetailService orderDetailService = new OrderDetailService();
        RoleService roleService = new RoleService();
        DenominationDetailService denominationDetailService = new DenominationDetailService();
        DashboardService dashboardService = new DashboardService();

        ImportExcelService importExcelService = new ImportExcelService();
        ExportExcelService exportExcelService = new ExportExcelService();
        SurchargeService surchargeService = new SurchargeService();
        PromotionService promotionService = new PromotionService();
        ShiftService shiftService = new ShiftService();
        ShiftCloseService shiftCloseService = new ShiftCloseService();
        RoomTypeService roomTypeService = new RoomTypeService();
        RoomToolsService roomToolsService = new RoomToolsService();
        EmployeeShiftService employeeShiftService = new EmployeeShiftService();
        PaymentService paymentService = new PaymentService();
        SurchargeDetailService surchargeDetailService = new SurchargeDetailService();


        RoomService roomService = new RoomService();

        HandlerRegistry registry = new HandlerRegistry();


        CustomerHandler customerHandler = new CustomerHandler(customerService);
        BookingHandler bookingHandler = new BookingHandler(bookingService);
        AuthenticateHandler authenticateHandler = new AuthenticateHandler(authenticateService, accountService);
        EmployeeHandler employeeHandler = new EmployeeHandler(employeeService);
        AmenityHandler amenityHandler = new AmenityHandler(amenityService);
        OrderHandler orderHandler = new OrderHandler(orderService);
        DashboardHandler dashboardHandler = new DashboardHandler(dashboardService);
        DenominationDetailHandler denominationDetailHandler = new DenominationDetailHandler(denominationDetailService);
        EmailHandler emailHandler = new EmailHandler(emailSenderService);
        OrderDetailHandler orderDetailHandler = new OrderDetailHandler(orderDetailService);
        RoleHandler roleHandler = new RoleHandler(roleService);
        ImportExportHandler exportHandler = new ImportExportHandler(exportExcelService, importExcelService);
        SurchargeHandler surchargeHandler = new SurchargeHandler(surchargeService);
        PromotionHandler promotionHandler = new PromotionHandler(promotionService);
        ShiftCloseHandler shiftCloseHandler = new ShiftCloseHandler(shiftCloseService);
        RoomTypeHandler roomTypeHandler = new RoomTypeHandler(roomTypeService);
        JaspersoftExportHandler jaspersoftExportHandler = new JaspersoftExportHandler(jaspersoftExportService);
        RoomToolsHandler roomToolsHandler = new RoomToolsHandler(roomToolsService);
        EmployeeShiftHandler employeeShiftHandler = new EmployeeShiftHandler(employeeShiftService);
        SurchargeDetailHandler surchargeDetailHandler = new SurchargeDetailHandler(surchargeDetailService);

        PaymentHandler paymentHandler = new PaymentHandler(paymentService);
        RoomHandler roomHandler = new RoomHandler(roomService);

        ShiftHandler shiftHandler = new ShiftHandler(shiftService);

        registerAuth(registry, authenticateHandler);
        registerEmployee(registry, employeeHandler);
        registerAmenity(registry, amenityHandler);
        registerOrder(registry, orderHandler);
        registerEmail(registry, emailHandler);
        registerBooking(registry, bookingHandler);
        registerCustomer(registry, customerHandler);
        registerOrderDetail(registry, orderDetailHandler);
        registerRole(registry, roleHandler);
        registerDashboard(registry, dashboardHandler);
        registerDenominationDetail(registry, denominationDetailHandler);
        registerImportExport(registry, exportHandler);
        registerSurcharge(registry, surchargeHandler);
        registerPromotion(registry, promotionHandler);
        registerShift(registry, shiftHandler);
        registerShiftClose(registry, shiftCloseHandler);
        registerRoomType(registry, roomTypeHandler);
        registerJaspersoftExport(registry, jaspersoftExportHandler);
        registerRoomTools(registry, roomToolsHandler);
        registerRoom(registry, roomHandler);
        registerEmployeeShift(registry, employeeShiftHandler);
        registerPayment(registry, paymentHandler);
        registerSurchargeDetail(registry, surchargeDetailHandler);


        Dispatcher dispatcher = new Dispatcher(registry);
        new SocketServer(dispatcher).start();
    }

    private static void registerSurchargeDetail(HandlerRegistry registry, SurchargeDetailHandler surchargeDetailHandler) {
        registry.register(CommandType.SURCHARGE_DETAIL_GET_BY_ORDER, surchargeDetailHandler);
        registry.register(CommandType.SURCHARGE_DETAIL_CREATE, surchargeDetailHandler);
        registry.register(CommandType.SURCHARGE_DETAIL_UPDATE, surchargeDetailHandler);
        registry.register(CommandType.SURCHARGE_DETAIL_DELETE, surchargeDetailHandler);
        registry.register(CommandType.SURCHARGE_DETAIL_CREATE_LIST, surchargeDetailHandler);
    }

    private static void registerPayment(HandlerRegistry registry, PaymentHandler paymentHandler) {
        registry.register(CommandType.PAYMENT_CREATE, paymentHandler);
        registry.register(CommandType.PAYMENT_QUERY, paymentHandler);
    }

    private static void registerEmployeeShift(HandlerRegistry registry, EmployeeShiftHandler employeeShiftHandler) {
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_BY_ID, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_ALL, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_CREATE, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_UPDATE, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_DELETE, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_BY_EMPLOYEE_AND_DATE, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_SHIFT_BY_DATE, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_WITH_DETAILS, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_TOTAL_REVENUE, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_ACTIVE_OPEN_SHIFTS, employeeShiftHandler);
        registry.register(CommandType.EMPLOYEE_SHIFT_GET_ALL_SHIFTS_BY_DATE, employeeShiftHandler);


    }

    private static void registerRoom(HandlerRegistry registry, RoomHandler roomHandler) {
        registry.register(CommandType.ROOM_OPTIMIZE_ROOM_ALLOCATION, roomHandler);
        registry.register(CommandType.ROOM_CHECK_ROOM_CAPACITY, roomHandler);
        registry.register(CommandType.ROOM_GET_AVAILABLE_ROOMS, roomHandler);
        registry.register(CommandType.ROOM_COUNT_AVAILABLE_ROOMS, roomHandler);
        registry.register(CommandType.ROOM_UPDATE_ROOM_STATUS_BATCH, roomHandler);
        registry.register(CommandType.ROOM_GET_ALL, roomHandler);
        registry.register(CommandType.ROOM_GET_BY_KEYWORD, roomHandler);
        registry.register(CommandType.ROOM_CAN_DELETE, roomHandler);
        registry.register(CommandType.ROOM_DELETE, roomHandler);
        registry.register(CommandType.ROOM_UPDATE, roomHandler);
        registry.register(CommandType.ROOM_CREATE, roomHandler);

    }

    private static void registerRoomTools(HandlerRegistry registry, RoomToolsHandler roomToolsHandler) {
        registry.register(CommandType.ROOM_TOOL_ROOM_PRICE_WITH_DURATION, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_CALCULATE_EXTENSION_AMOUNT, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_EXTEND_ROOM_BOOKING, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_VALIDATE_TRANSFER, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_CALCULATE_SURCHARGE, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_CANCEL_ROOM_BOOKING, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_GET_ROOMS_BY_ORDER_AND_TYPE, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_GET_AVAILABLE_ROOMS_BY_TYPE, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_GET_ROOM_PRICE_BY_TYPE, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_CALCULATE_NEW_ROOM_PRICE_WITH_BOOKING_DURATION, roomToolsHandler);
        registry.register(CommandType.ROOM_TOOL_TRANSFER_ROOMS, roomToolsHandler);
    }

    private static void registerJaspersoftExport(HandlerRegistry registry, JaspersoftExportHandler jaspersoftExportHandler) {
        registry.register(CommandType.JASPERSOFT_EXPORT_ORDER_TO_PDF, jaspersoftExportHandler);
    }

    private static void registerRoomType(HandlerRegistry registry, RoomTypeHandler roomTypeHandler) {
        registry.register(CommandType.ROOM_TYPE_GET_BY_ID, roomTypeHandler);
        registry.register(CommandType.ROOM_TYPE_GET_ALL, roomTypeHandler);
        registry.register(CommandType.ROOM_TYPE_CREATE, roomTypeHandler);
        registry.register(CommandType.ROOM_TYPE_UPDATE, roomTypeHandler);
        registry.register(CommandType.ROOM_TYPE_DELETE, roomTypeHandler);
    }

    private static void registerShiftClose(HandlerRegistry registry, ShiftCloseHandler shiftCloseHandler) {
        registry.register(CommandType.SHIFT_CLOSE_CREATE, shiftCloseHandler);
        registry.register(CommandType.SHIFT_CLOSE_GET_BY_ID, shiftCloseHandler);
        registry.register(CommandType.SHIFT_CLOSE_DELETE, shiftCloseHandler);
        registry.register(CommandType.SHIFT_CLOSE_UPDATE, shiftCloseHandler);
        registry.register(CommandType.SHIFT_CLOSE_GET_TOTAL_REVENUE, shiftCloseHandler);
        registry.register(CommandType.SHIFT_CLOSE_GET_BY_EMPLOYEE_SHIFT, shiftCloseHandler);
        registry.register(CommandType.SHIFT_CLOSE_GET_ALL, shiftCloseHandler);
    }

    private static void registerShift(HandlerRegistry registry, ShiftHandler shiftHandler) {
        registry.register(CommandType.SHIFT_GET_ALL, shiftHandler);
        registry.register(CommandType.SHIFT_GET_BY_ID, shiftHandler);
        registry.register(CommandType.SHIFT_CREATE, shiftHandler);
        registry.register(CommandType.SHIFT_UPDATE, shiftHandler);
        registry.register(CommandType.SHIFT_DELETE, shiftHandler);
    }

    private static void registerPromotion(HandlerRegistry registry, PromotionHandler promotionHandler) {
        registry.register(CommandType.PROMOTION_GET_BY_ID, promotionHandler);
        registry.register(CommandType.PROMOTION_GET_ACTIVE, promotionHandler);
        registry.register(CommandType.PROMOTION_GET_ALL, promotionHandler);
        registry.register(CommandType.PROMOTION_GET_BY_KEYWORDS, promotionHandler);
        registry.register(CommandType.PROMOTION_CREATE, promotionHandler);
        registry.register(CommandType.PROMOTION_DELETE, promotionHandler);
        registry.register(CommandType.PROMOTION_UPDATE, promotionHandler);

    }

    private static void registerSurcharge(HandlerRegistry registry, SurchargeHandler surchargeHandler) {
        registry.register(CommandType.SURCHARGE_GET_BY_ID, surchargeHandler);
        registry.register(CommandType.SURCHARGE_GET_BY_NAME, surchargeHandler);
        registry.register(CommandType.SURCHARGE_GET_ALL, surchargeHandler);
        registry.register(CommandType.SURCHARGE_GET_BY_KEYWORDS, surchargeHandler);
        registry.register(CommandType.SURCHARGE_CREATE, surchargeHandler);
        registry.register(CommandType.SURCHARGE_UPDATE, surchargeHandler);
        registry.register(CommandType.SURCHARGE_DELETE, surchargeHandler);

    }

    private static void registerImportExport(HandlerRegistry registry, ImportExportHandler exportHandler) {
        registry.register(CommandType.EXPORT_EXCEL, exportHandler);
        registry.register(CommandType.IMPORT_SURCHARGES, exportHandler);
        registry.register(CommandType.IMPORT_AMENITIES, exportHandler);
        registry.register(CommandType.IMPORT_PROMOTIONS, exportHandler);
        registry.register(CommandType.IMPORT_CUSTOMERS, exportHandler);
        registry.register(CommandType.IMPORT_EMPLOYEES, exportHandler);
        registry.register(CommandType.IMPORT_ROOMS, exportHandler);
    }

    private static void registerDenominationDetail(HandlerRegistry registry, DenominationDetailHandler denominationDetailHandler) {
        registry.register(CommandType.DENOMINATION_AVAILABLE, denominationDetailHandler);
        registry.register(CommandType.DENOMINATION_DETAIL_SAVE_ALL, denominationDetailHandler);
    }

    private static void registerDashboard(HandlerRegistry registry, DashboardHandler dashboardHandler) {
        registry.register(CommandType.DASHBOARD_PEAK_HOURS, dashboardHandler);
        registry.register(CommandType.DASHBOARD_GET_DATA, dashboardHandler);
        registry.register(CommandType.DASHBOARD_GET_ROOMS, dashboardHandler);
        registry.register(CommandType.DASHBOARD_ORDER_STATISTICS, dashboardHandler);
        registry.register(CommandType.DASHBOARD_GET_DATA_EMPLOYEE, dashboardHandler);
    }

    private static void registerRole(HandlerRegistry registry, RoleHandler roleHandler) {
        registry.register(CommandType.ROLE_GET_BY_ID, roleHandler);
    }

    private static void registerOrderDetail(HandlerRegistry registry, OrderDetailHandler orderDetailHandler) {
        registry.register(CommandType.ORDER_DETAIL_CREATE, orderDetailHandler);
        registry.register(CommandType.ORDER_DETAIL_GET_BY_ID, orderDetailHandler);
        registry.register(CommandType.ORDER_DETAIL_DELETE_BY_ID, orderDetailHandler);
        registry.register(CommandType.ORDER_DETAIL_FROM_ORDER, orderDetailHandler);
        registry.register(CommandType.ORDER_DETAIL_UPDATE_FROM_ORDER, orderDetailHandler);
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
        registry.register(CommandType.EMPLOYEE_GET_BY_PHONE, employeeHandler);
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
        registry.register(CommandType.ORDER_GET_ALL_WITH_RELATIONSHIP_COMPLETE_YET, orderHandler);
        registry.register(CommandType.ORDER_GET_UN_PENDING_BY_KEYWORD, orderHandler);


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
