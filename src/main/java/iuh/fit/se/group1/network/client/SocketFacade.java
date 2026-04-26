package iuh.fit.se.group1.network.client;

import iuh.fit.se.group1.network.client.service.AuthServiceClient;
import iuh.fit.se.group1.network.client.service.EmployeeServiceClient;
import lombok.Getter;

//import iuh.fit.se.group1.network.client.service.OrderServiceClient;
//@Getter
@Getter
public class SocketFacade {
    private final AuthServiceClient auth;
    private final EmployeeServiceClient employee;
//    public final OrderServiceClient order;

    public SocketFacade(ClientSocketManager socket) {
        this.auth = new AuthServiceClient(socket);
        this.employee = new EmployeeServiceClient(socket);
//        this.order = new OrderServiceClient(socket);
    }

}
