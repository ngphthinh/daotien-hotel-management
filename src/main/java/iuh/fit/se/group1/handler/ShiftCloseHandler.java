package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.ShiftCloseDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.ShiftCloseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class ShiftCloseHandler implements RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(ShiftCloseHandler.class);
    private final ShiftCloseService shiftCloseService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case SHIFT_CLOSE_CREATE -> handleCreate(request);
                case SHIFT_CLOSE_GET_ALL -> handleGetAll(request);
                case SHIFT_CLOSE_GET_BY_ID -> handleGetById(request);
                case SHIFT_CLOSE_DELETE -> handleDelete(request);
                case SHIFT_CLOSE_UPDATE -> handleUpdate(request);
                case SHIFT_CLOSE_GET_BY_EMPLOYEE_SHIFT -> handleGetByEmployeeShift(request);
                case SHIFT_CLOSE_GET_TOTAL_REVENUE -> handleGetTotalRevenue(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling shift close request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetTotalRevenue(Request request) {
        Long employeeShiftId = (Long) request.getRequest();
        if (employeeShiftId == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee shift ID cannot be null")
                    .build();
        }
        try {
            BigDecimal totalRevenue = shiftCloseService.getTotalCashRevenueForShift(employeeShiftId);
            return Response.builder()
                    .code(200)
                    .message("Total revenue retrieved successfully")
                    .data(totalRevenue)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving total revenue for employee shift ID {}: {}", employeeShiftId, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetByEmployeeShift(Request request) {
        Long employeeShiftId = (Long) request.getRequest();
        if (employeeShiftId == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee shift ID cannot be null")
                    .build();
        }
        try {
            List<ShiftCloseDTO> shiftCloseDTO = shiftCloseService.getShiftCloseByEmployeeShift(employeeShiftId);
            if (shiftCloseDTO == null) {
                return Response.builder()
                        .code(404)
                        .message("Shift close not found for employee shift ID: " + employeeShiftId)
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Shift close retrieved successfully")
                    .data(shiftCloseDTO)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving shift close for employee shift ID {}: {}", employeeShiftId, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdate(Request request) {
        ShiftCloseDTO shiftCloseDTO = (ShiftCloseDTO) request.getRequest();
        if (shiftCloseDTO == null || shiftCloseDTO.getShiftCloseId() == null) {
            return Response.builder()
                    .code(400)
                    .message("Shift close data and ID cannot be null")
                    .build();
        }
        try {
            ShiftCloseDTO updatedShiftClose = shiftCloseService.updateShiftClose(shiftCloseDTO);
            if (updatedShiftClose == null) {
                return Response.builder()
                        .code(404)
                        .message("Shift close not found with ID: " + shiftCloseDTO.getShiftCloseId())
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Shift close updated successfully")
                    .data(updatedShiftClose)
                    .build();
        } catch (Exception e) {
            log.error("Error updating shift close with ID {}: {}", shiftCloseDTO.getShiftCloseId(), e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleDelete(Request request) {
        Long id = (Long) request.getRequest();
        if (id == null) {
            return Response.builder()
                    .code(400)
                    .message("Shift close ID cannot be null")
                    .build();
        }
        try {
            shiftCloseService.deleteShiftClose(id);
            return Response.builder()
                    .code(200)
                    .message("Shift close deleted successfully")
                    .build();
        } catch (Exception e) {
            log.error("Error deleting shift close with ID {}: {}", id, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetById(Request request) {
        Long id = (Long) request.getRequest();
        if (id == null) {
            return Response.builder()
                    .code(400)
                    .message("Shift close ID cannot be null")
                    .build();
        }
        try {
            ShiftCloseDTO shiftCloseDTO = shiftCloseService.getShiftCloseById(id);
            if (shiftCloseDTO == null) {
                return Response.builder()
                        .code(404)
                        .message("Shift close not found with ID: " + id)
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Shift close retrieved successfully")
                    .data(shiftCloseDTO)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving shift close with ID {}: {}", id, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAll(Request request) {
        List<ShiftCloseDTO> shiftCloses = shiftCloseService.getAllShiftClose();
        if(shiftCloses==null || shiftCloses.isEmpty()){
            return Response.builder()
                    .code(404)
                    .message("No shift closes found")
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Shift closes retrieved successfully")
                .data(shiftCloses)
                .build();

    }

    private Response handleCreate(Request request) {
        ShiftCloseDTO shiftCloseDTO = (ShiftCloseDTO) request.getRequest();
        if (shiftCloseDTO == null) {
            return Response.builder()
                    .code(400)
                    .message("Shift close data cannot be null")
                    .build();
        }
        try {
            ShiftCloseDTO createdShiftClose = shiftCloseService.saveShiftClose(shiftCloseDTO);
            return Response.builder()
                    .code(200)
                    .message("Shift close created successfully")
                    .data(createdShiftClose)
                    .build();
        } catch (Exception e) {
            log.error("Error creating shift close: {}", e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }
}
