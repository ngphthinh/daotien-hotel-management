package iuh.fit.se.group1.network.client;

import iuh.fit.se.group1.network.client.service.*;
import lombok.Getter;

import java.net.Socket;

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

    public SocketFacade(ClientSocketManager socket) {
        this.auth = new AuthServiceClient(socket);
        this.employee = new EmployeeServiceClient(socket);
        this.email = new EmailServiceClient(socket);
        this.amenity = new AmenityServiceClient(socket);
        this.order = new OrderServiceClient(socket);
        this.booking = new BookingServiceClient(socket);
    }

    private static SocketFacade instance;

    public static SocketFacade getInstance() {
        if (instance == null) {
            instance = new SocketFacade(ClientSocketManager.getInstance());
        }
        return instance;
    }

}
