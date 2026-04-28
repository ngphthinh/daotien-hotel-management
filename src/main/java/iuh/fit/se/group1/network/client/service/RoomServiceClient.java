package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class RoomServiceClient implements ServiceClient {
    private final ClientSocketManager socket;

    public Response optimizeRoomAllocation(int singleQuantity, int doubleQuantity, int adults, int children) throws Exception {

        OptimizeRoomAllocationRequest request = OptimizeRoomAllocationRequest.builder()
                .singleQuantity(singleQuantity)
                .doubleQuantity(doubleQuantity)
                .adults(adults)
                .children(children)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_OPTIMIZE_ROOM_ALLOCATION)
                .request(request)
                .build()).get();

    }

    public Response checkRoomCapacity(int adults, int children, Number usedSingleRooms, Number usedDoubleRooms) throws Exception {
        CheckRoomCapacityRequest request = CheckRoomCapacityRequest.builder()
                .adults(adults)
                .children(children)
                .usedSingleRooms(usedSingleRooms.intValue())
                .usedDoubleRooms(usedDoubleRooms.intValue())
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_CHECK_ROOM_CAPACITY)
                .request(request)
                .build()).get();

    }

    public Response getAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut) throws Exception {
        DateTimeRangeRequest request = DateTimeRangeRequest.builder()
                .from(checkIn)
                .to(checkOut)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_GET_AVAILABLE_ROOMS)
                .request(request)
                .build()).get();
    }

    public Response countAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut) throws Exception {
        DateTimeRangeRequest request = DateTimeRangeRequest.builder()
                .from(checkIn)
                .to(checkOut)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_COUNT_AVAILABLE_ROOMS)
                .request(request)
                .build()).get();
    }

    public Response updateRoomStatusBatch(List<Long> roomIds, RoomStatus roomStatus) throws Exception {
        UpdateRoomStatusBatchRequest updateRoomStatusBatchRequest = UpdateRoomStatusBatchRequest.builder()
                .roomIds(roomIds)
                .roomStatus(roomStatus)
                .build();
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_UPDATE_ROOM_STATUS_BATCH)
                .request(updateRoomStatusBatchRequest)
                .build()).join();
    }

    public Response getAllRooms() throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_GET_ALL)
                .build()).get();
    }

    public Response getRoomByKeyword(String text) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_GET_BY_KEYWORD)
                .request(text)
                .build()).get();
    }

    public Response canDeleteRoom(Long roomId) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_CAN_DELETE)
                .request(roomId)
                .build()).get();

    }

    public Response deleteRoom(Long roomId) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_DELETE)
                .request(roomId)
                .build()).get();

    }

    public Response updateRoom(RoomViewDTO roomFromModal) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_UPDATE)
                .request(roomFromModal)
                .build()).get();

    }

    public Response createRoom(RoomViewDTO newRoom) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_CREATE)
                .request(newRoom)
                .build()).get();
    }
}
