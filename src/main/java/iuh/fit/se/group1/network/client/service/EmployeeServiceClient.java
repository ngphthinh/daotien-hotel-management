package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EmployeeServiceClient {

    private final ClientSocketManager socket;

    public EmployeeServiceClient(ClientSocketManager socket) {
        this.socket = socket;
    }

    public Response getById(Long id) throws Exception {
        Request req = Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_BY_ID)
                .request(id)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }

    public Response create(EmployeeDTO dto, String roleId) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("employee", dto);
        data.put("roleId", roleId);

        Request req = Request.builder()
                .commandType(CommandType.EMPLOYEE_CREATE)
                .request(data)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }

    public Response getEmployeeByAccountId(String accountId) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Request req = Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_BY_ACCOUNT_ID)
                .request(accountId)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }

    public Response getEmployeeByCitizenId(String citizenId) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Request req = Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_BY_CITIZEN_ID)
                .request(citizenId)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }

    public Response getEmployeesByRoleId(String roleId) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Request req = Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_BY_ROLE_ID)
                .request(roleId)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }
}