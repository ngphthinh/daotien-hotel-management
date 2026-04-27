package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.CustomerDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomerServiceClient implements ServiceClient {
    private final ClientSocketManager socket;


    public Response getCustomerByCitizenId(String citizenId) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.CUSTOMER_GET_BY_CITIZEN)
                .request(citizenId)
                .build()).get();
    }

    public Response updateCustomer(CustomerDTO customerSaveDB) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.CUSTOMER_UPDATE)
                .request(customerSaveDB)
                .build()).get();
    }

    public Response getAllCustomer() throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.CUSTOMER_GET_ALL)
                .build()).get();
    }

    public Response getCustomerByKeyword(String keyword) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.CUSTOMER_GET_BY_KEYWORDS)
                .request(keyword)
                .build()).join();

    }

    public Response getCustomerById(Long id) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.CUSTOMER_GET_BY_ID)
                .request(id)
                .build()).get();


    }

    public Response deleteCustomer(Long id) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.CUSTOMER_DELETE)
                .request(id)
                .build()).get();
    }
}
