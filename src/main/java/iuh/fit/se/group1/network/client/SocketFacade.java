package iuh.fit.se.group1.network.client;

import iuh.fit.se.group1.network.client.service.*;
import lombok.Getter;

//import iuh.fit.se.group1.network.client.service.OrderServiceClient;
//@Getter
@Getter
public class SocketFacade {
    private final AuthServiceClient auth;
    private final EmployeeServiceClient employee;
    private final EmailServiceClient email;
    private final AmenityServiceClient amenity;
    private final OrderServiceClient order;
    private final BookingServiceClient booking;
    private final CustomerServiceClient customer;
    private final OrderDetailServiceClient orderDetail;
    private final RoleServiceClient role;
    private final DashboardServiceClient dashboard;
    private final DenominationDetailServiceClient denominationDetail;
    private final ImportExportExcelServiceClient importExportExcel;
    private final SurchargeServiceClient surcharge;
    private final PromotionServiceClient promotion;
    private final ShiftServiceClient shift;
    private final ShiftCloseServiceClient shiftClose;
    private final RoomTypeServiceClient roomType;
    private final JaspersoftExportServiceClient jaspersoftExport;
    private final RoomToolsServiceClient roomTools;
    private final RoomServiceClient room;
    private final EmployeeShiftServiceClient employeeShift;

    public SocketFacade(ClientSocketManager socket) {
        this.auth = new AuthServiceClient(socket);
        this.employee = new EmployeeServiceClient(socket);
        this.email = new EmailServiceClient(socket);
        this.amenity = new AmenityServiceClient(socket);
        this.order = new OrderServiceClient(socket);
        this.booking = new BookingServiceClient(socket);
        this.customer = new CustomerServiceClient(socket);
        this.orderDetail = new OrderDetailServiceClient(socket);
        this.role = new RoleServiceClient(socket);
        this.dashboard = new DashboardServiceClient(socket);
        this.denominationDetail = new DenominationDetailServiceClient(socket);
        this.importExportExcel = new ImportExportExcelServiceClient(socket);
        this.surcharge = new SurchargeServiceClient(socket);
        this.promotion = new PromotionServiceClient(socket);
        this.shift = new ShiftServiceClient(socket);
        this.shiftClose = new ShiftCloseServiceClient(socket);
        this.roomType = new RoomTypeServiceClient(socket);
        this.jaspersoftExport = new JaspersoftExportServiceClient(socket);
        this.roomTools = new RoomToolsServiceClient(socket);
        this.room = new RoomServiceClient(socket);
        this.employeeShift = new EmployeeShiftServiceClient(socket);
    }

    private static SocketFacade instance;

    public static SocketFacade getInstance() {
        if (instance == null) {
            instance = new SocketFacade(ClientSocketManager.getInstance());
        }
        return instance;
    }

}
