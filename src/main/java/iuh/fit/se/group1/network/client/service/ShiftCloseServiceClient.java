package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.ShiftCloseDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ShiftCloseServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;

    public Response getAllShiftCloses() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CLOSE_GET_ALL)
                .build()).get();
    }

    public Response getShiftCloseById(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CLOSE_GET_BY_ID)
                .request(id)
                .build()).get();
    }

    public Response saveShiftClose(ShiftCloseDTO shiftCloseDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CLOSE_CREATE)
                .request(shiftCloseDTO)
                .build()).get();
    }

    public Response updateShiftClose(Object shiftCloseDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CLOSE_UPDATE)
                .request(shiftCloseDTO)
                .build()).get();
    }

    public Response deleteShiftClose(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CLOSE_DELETE)
                .request(id)
                .build()).get();
    }

    public Response getShiftCloseByEmployeeShift(Long employeeShiftId) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CLOSE_GET_BY_EMPLOYEE_SHIFT)
                .request(employeeShiftId)
                .build()).get();
    }

    public Response getTotalCashRevenueForShift(Long employeeShiftId) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CLOSE_GET_TOTAL_REVENUE)
                .request(employeeShiftId)
                .build()).get();
    }


}
