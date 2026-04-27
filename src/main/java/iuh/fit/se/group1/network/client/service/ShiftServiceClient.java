package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShiftServiceClient implements ServiceClient{
    private final ClientSocketManager clientSocketManager;
    public Response getAllShifts() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_GET_ALL)
                .build()).get();
    }
    public Response getShiftById(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_GET_BY_ID)
                .request(id)
                .build()).get();
    }
    public Response createShift(Object shiftDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_CREATE)
                .request(shiftDTO)
                .build()).get();
    }
    public Response updateShift(Object shiftDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_UPDATE)
                .request(shiftDTO)
                .build()).get();
    }
    public Response deleteShift(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SHIFT_DELETE)
                .request(id)
                .build()).get();
    }

}

