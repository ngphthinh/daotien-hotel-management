package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.ShiftDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.util.List;

@RequiredArgsConstructor
public class ShiftHandler implements RequestHandler {
    private static final Logger log=  org.slf4j.LoggerFactory.getLogger(ShiftHandler.class);
    private final ShiftService shiftService;
    @Override
    public Response handle(Request request) {
        CommandType commandType= request.getCommandType();
        try {
            return switch (commandType){
                case SHIFT_GET_BY_ID -> handleGetById(request);
                case SHIFT_GET_ALL -> handleGetAll(request);
                case SHIFT_CREATE -> handleCreate(request);
                case SHIFT_UPDATE -> handleUpdate(request);
                case SHIFT_DELETE -> handleDelete(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling shift request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleDelete(Request request) {
        Long id= (Long) request.getRequest();
        if(id==null){
            return Response.builder()
                    .code(400)
                    .message("Shift ID cannot be null")
                    .build();
        }
        try{
            shiftService.deleteShift(id);
            return Response.builder()
                    .code(200)
                    .message("Shift deleted successfully")
                    .build();
        }catch (Exception e){
            log.error("Error deleting shift with ID {}: {}", id, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Failed to delete shift: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdate(Request request) {
        ShiftDTO shiftDTO= (ShiftDTO) request.getRequest();
        if(shiftDTO==null){
            return Response.builder()
                    .code(400)
                    .message("Shift data cannot be null")
                    .build();
        }
        try{
            ShiftDTO updatedShift= shiftService.updateShift(shiftDTO);
            if (updatedShift == null) {
                return Response.builder()
                        .code(404)
                        .message("No shift found with ID: " + shiftDTO.getShiftId())
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Shift updated successfully")
                    .data(updatedShift)
                    .build();
        }catch (Exception e){
            log.error("Error updating shift: {}", e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Failed to update shift: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        ShiftDTO shiftDTO= (ShiftDTO) request.getRequest();
        if(shiftDTO==null){
            return Response.builder()
                    .code(400)
                    .message("Shift data cannot be null")
                    .build();
        }
        try{
            ShiftDTO createdShift= shiftService.createShift(shiftDTO);
            if (createdShift == null) {
                return Response.builder()
                        .code(500)
                        .message("Failed to create shift")
                        .build();
            }
            return Response.builder()
                .code(201)
                .message("Shift created successfully")
                .data(createdShift)
                .build();
            }catch (Exception e){
            log.error("Error creating shift: {}", e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Failed to create shift: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAll(Request request) {
        List<ShiftDTO> shifts= shiftService.getAllShifts();
        if(shifts==null || shifts.isEmpty()){
            return Response.builder()
                    .code(404)
                    .message("No shifts found")
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get all Shifts successfully")
                .data(shifts)
                .build();
    }

    private Response handleGetById(Request request) {
        Long id= (Long) request.getRequest();
        if(id==null){
            return Response.builder()
                    .code(400)
                    .message("Shift ID cannot be null")
                    .build();

        }
        ShiftDTO shiftDTO= shiftService.getShiftById(id);
        if(shiftDTO==null){

            return Response.builder()
                    .code(404)
                    .message("No shift found with ID: " + id)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Shift retrieved successfully")
                .data(shiftDTO)
                .build();
    }

}
