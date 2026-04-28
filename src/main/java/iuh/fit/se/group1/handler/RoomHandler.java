package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.RoomService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomHandler implements RequestHandler {

    private final RoomService roomService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case ROOM_OPTIMIZE_ROOM_ALLOCATION -> handleOptimizeRoomAllocation(request);
                case ROOM_CHECK_ROOM_CAPACITY -> handleCheckRoomCapacity(request);
                case ROOM_GET_AVAILABLE_ROOMS -> handleGetAvailableRooms(request);
                case ROOM_GET_BY_KEYWORD -> handleGetByKeyword(request);
                case ROOM_CAN_DELETE -> handleCanDelete(request);
                case ROOM_DELETE -> handleDelete(request);
                case ROOM_CREATE -> handleCreate(request);
                case ROOM_UPDATE -> handleUpdate(request);
                case ROOM_GET_ALL -> handleGetAll(request);
                case ROOM_UPDATE_ROOM_STATUS_BATCH -> handleUpdateRoomStatusBatch(request);
                case ROOM_COUNT_AVAILABLE_ROOMS -> handleCountAvailableRooms(request);

                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetByKeyword(Request request) {
        try {
            String keyword = (String) request.getRequest();

            var res = roomService.getRoomByKeyword(keyword);

            return Response.builder().code(200)
                    .message("Search rooms successfully")
                    .data(res).build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Search rooms failed: " + e.getMessage()).build();
        }
    }

    private Response handleOptimizeRoomAllocation(Request request) {
        try {
            OptimizeRoomAllocationRequest req =
                    (OptimizeRoomAllocationRequest) request.getRequest();

            var res = roomService.optimizeRoomAllocation(
                    req
            );

            return Response.builder()
                    .code(200).data(res)
                    .message("Optimize room allocation successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Optimize failed: " + e.getMessage()).build();
        }
    }

    private Response handleCheckRoomCapacity(Request request) {
        try {
            CheckRoomCapacityRequest req =
                    (CheckRoomCapacityRequest) request.getRequest();

            var res = roomService.checkRoomCapacity(req);

            return Response.builder().code(200).data(res)
                    .message("Check room capacity successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Check capacity failed: " + e.getMessage()).build();
        }
    }

    private Response handleCountAvailableRooms(Request request) {
        try {
            DateTimeRangeRequest req =
                    (DateTimeRangeRequest) request.getRequest();

            var res = roomService.countAvailableRooms(
                    req.getFrom(), req.getTo()
            );

            return Response.builder().code(200)
                    .message("Count available rooms successfully")
                    .data(res).build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Count available rooms failed: " + e.getMessage()).build();
        }
    }

    private Response handleGetAvailableRooms(Request request) {
        try {
            DateTimeRangeRequest req =
                    (DateTimeRangeRequest) request.getRequest();

            var res = roomService.getAvailableRooms(
                    req.getFrom(),
                    req.getTo()
            );

            return Response.builder().code(200).data(res)
                    .message("Get available rooms successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Get available rooms failed: " + e.getMessage()).build();
        }
    }

    private Response handleCreate(Request request) {
        try {
            RoomViewDTO req = (RoomViewDTO) request.getRequest();

            var res = roomService.createRoom(req);

            return Response.builder().code(200)
                    .message("Create room successfully")
                    .data(res).build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Create room failed: " + e.getMessage()).build();
        }
    }

    private Response handleUpdate(Request request) {
        try {
            RoomViewDTO req = (RoomViewDTO) request.getRequest();

            var res = roomService.updateRoom(req);

            Response response = Response.builder().code(200)
                    .message("Update room successfully")
                    .data(res).build();

            return response;

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Update room failed: " + e.getMessage()).build();
        }
    }

    private Response handleDelete(Request request) {
        try {
            Long roomId = (Long) request.getRequest();

            roomService.deleteRoom(roomId);

            Response response = Response.builder()
                    .code(200)
                    .message("Delete room successfully")
                    .build();


            return response;

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Delete room failed: " + e.getMessage()).build();
        }
    }

    private Response handleCanDelete(Request request) {
        try {
            Long roomId = (Long) request.getRequest();

            boolean res = roomService.canDeleteRoom(roomId);

            return Response.builder().code(200)
                    .message("Can delete room successfully")
                    .data(res).build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Check delete failed: " + e.getMessage()).build();
        }
    }

    private Response handleGetAll(Request request) {
        try {
            var res = roomService.getAllRooms();

            return Response.builder().code(200)
                    .message("Get all rooms successfully")
                    .data(res).build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Get all rooms failed: " + e.getMessage()).build();
        }
    }

    private Response handleUpdateRoomStatusBatch(Request request) {
        try {
            UpdateRoomStatusBatchRequest req =
                    (UpdateRoomStatusBatchRequest) request.getRequest();

            roomService.updateRoomStatusBatch(req);

            return Response.builder()
                    .code(200)
                    .message("Update room status batch successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder().code(500)
                    .message("Update room status batch failed: " + e.getMessage()).build();
        }
    }
}
