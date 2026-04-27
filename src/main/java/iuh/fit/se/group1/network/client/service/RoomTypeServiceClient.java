package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomTypeServiceClient {
    private final ClientSocketManager clientSocketManager;
    public Response getAllRoomTypes() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ROOM_TYPE_GET_ALL)
                .build()).get();
    }
    public Response getRoomTypeById(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ROOM_TYPE_GET_BY_ID)
                .request(id)
                .build()).get();
    }
    public Response createRoomType(Object roomTypeDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ROOM_TYPE_CREATE)
                .request(roomTypeDTO)
                .build()).get();
    }
    public Response updateRoomType(Object roomTypeDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ROOM_TYPE_UPDATE)
                .request(roomTypeDTO)
                .build()).get();
    }
    public Response deleteRoomType(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.ROOM_TYPE_DELETE)
                .request(id)
                .build()).get();
    }
}
