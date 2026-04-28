package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.EmployeeCreateRequest;
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

public class EmployeeServiceClient implements ServiceClient {

    private final ClientSocketManager socket;

    public EmployeeServiceClient(ClientSocketManager socket) {
        this.socket = socket;
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
        return null;
    }

    public Response getEmployeesByRoleId(String roleId) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Request req = Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_BY_ROLE_ID)
                .request(roleId)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }

    public Response createEmployee(EmployeeDTO employee, String roleId) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.EMPLOYEE_CREATE)
                .request(EmployeeCreateRequest.builder()
                        .employee(employee)
                        .roleId(roleId)
                        .build())
                .build()).get();

    }

    public Response getAllAmenities() throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_ALL)
                .build()).get();
    }

    public Response getEmployeeByKeyword(String filter) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_BY_KEYWORDS)
                .request(filter)
                .build()).get();
    }

    public Response getEmployeeById(Long employeeId) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.EMPLOYEE_GET_BY_ID)
                .request(employeeId)
                .build()).get();
    }

    public Response updateEmployee(EmployeeDTO employeeUpdate) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.EMPLOYEE_UPDATE)
                .request(employeeUpdate)
                .build()).get();
    }

    public Response deleteEmployee(Long id) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.EMPLOYEE_DELETE)
                .request(id)
                .build()).get();
    }
}