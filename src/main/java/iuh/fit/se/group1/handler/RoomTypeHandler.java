package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.RoomTypeDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class RoomTypeHandler implements RequestHandler {
    public static final Logger log= LoggerFactory.getLogger(RoomTypeHandler.class);
    private final RoomTypeService roomTypeService;
    @Override
    public Response handle(Request request) {
        CommandType commandType= request.getCommandType();
        try {
            return switch (commandType){
                case ROOM_TYPE_GET_BY_ID -> handleGetById(request);
                case ROOM_TYPE_GET_ALL -> handleGetAll(request);
                case ROOM_TYPE_CREATE -> handleCreate(request);
                case ROOM_TYPE_UPDATE -> handleUpdate(request);
                case ROOM_TYPE_DELETE -> handleDelete(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling room type request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
    }
}

    private Response handleDelete(Request request) {
        String id = (String) request.getRequest();
        if (id == null || id.isBlank()) {
            return Response.builder()
                    .code(400)
                    .message("Room type ID cannot be null or blank")
                    .build();
        }

        try {
            roomTypeService.deleteRoomType(id);
            return Response.builder()
                    .code(200)
                    .message("Room type deleted successfully")
                    .build();
        } catch (Exception e) {
            log.error("Error deleting room type with ID {}: {}", id, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Fail to delete room type: " + e.getMessage())
                    .build();
        }
    }


    private Response handleUpdate(Request request) {
        RoomTypeDTO roomTypeDTO= (RoomTypeDTO) request.getRequest();
        if(roomTypeDTO==null||roomTypeDTO.getRoomTypeId()==null){
            return Response.builder()
                    .code(400)
                    .message("Room type data or id cannot be null")
                    .build();
        }
        try{
            RoomTypeDTO updated= roomTypeService.updateRoomType(roomTypeDTO);
            if(updated==null){
                return Response.builder()
                        .code(404)
                        .message("No room type found with ID: " + roomTypeDTO.getRoomTypeId())
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Room type updated successfully")
                    .data(updated)
                    .build();
        } catch (Exception e){
            log.error("Error updating room type: {}", roomTypeDTO, e);
            return Response.builder()
                    .code(500)
                    .message("Fail to update room type: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        RoomTypeDTO roomTypeDTO= (RoomTypeDTO) request.getRequest();
        if(roomTypeDTO==null){
            return Response.builder()
                    .code(400)
                    .message("Room type data cannot be null")
                    .build();
        }
        try{
            RoomTypeDTO created= roomTypeService.createRoomType(roomTypeDTO);
            if(created==null){
                return Response.builder()
                        .code(500)
                        .message("Failed to create room type")
                        .build();
            }
            return Response.builder()
                .code(201)
                .message("Room type created successfully")
                .data(created)
                .build();
        } catch (Exception e){
            log.error("Error creating room type: {}", roomTypeDTO, e);
            return Response.builder()
                    .code(500)
                    .message("Fail to create room type: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAll(Request request) {
        List<RoomTypeDTO> roomTypes= roomTypeService.getAllRoomTypes();
        if(roomTypes==null || roomTypes.isEmpty()){
            return Response.builder()
                    .code(404)
                    .message("No room types found")
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get all room types successfully")
                .data(roomTypes)
                .build();
    }

    private Response handleGetById(Request request) {
        String id= (String) request.getRequest();
        if(id==null){
            return Response.builder()
                    .code(400)
                    .message("Room type ID cannot be null or blank")
                    .build();

        }
        RoomTypeDTO roomType= roomTypeService.getRoomTypeById(id);
        if(roomType==null){
            return Response.builder()
                    .code(404)
                    .message("No room type found with ID: " + id)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Room type retrieved successfully")
                .data(roomType)
                .build();
    }
}
