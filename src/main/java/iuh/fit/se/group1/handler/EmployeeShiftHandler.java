package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.EmployeeShiftActiveRequest;
import iuh.fit.se.group1.dto.EmployeeShiftDTO;
import iuh.fit.se.group1.dto.EmployeeShiftByEmpDateRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.EmployeeShiftService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class EmployeeShiftHandler implements RequestHandler {
    private final static Logger log = LoggerFactory.getLogger(EmployeeShiftHandler.class);
    private final EmployeeShiftService employeeShiftService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case EMPLOYEE_SHIFT_GET_ALL -> handleGetAll(request);
                case EMPLOYEE_SHIFT_CREATE -> handleCreate(request);
                case EMPLOYEE_SHIFT_UPDATE -> handleUpdate(request);
                case EMPLOYEE_SHIFT_DELETE -> handleDelete(request);
                case EMPLOYEE_SHIFT_GET_BY_ID -> handleGetById(request);
                case EMPLOYEE_SHIFT_GET_BY_EMPLOYEE_AND_DATE -> handleGetByEmployeeAndDate(request);
                case EMPLOYEE_SHIFT_GET_SHIFT_BY_DATE -> handleGetShiftByDate(request);
                case EMPLOYEE_SHIFT_GET_WITH_DETAILS -> handleGetWithDetails(request);
                case EMPLOYEE_SHIFT_GET_TOTAL_REVENUE -> handleGetTotalRevenue(request);
                case EMPLOYEE_SHIFT_GET_ACTIVE_OPEN_SHIFTS -> handleGetActiveOpenShifts(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling employee shift request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetTotalRevenue(Request request) {
        long employeeShiftId = (Long) request.getRequest();
        try {
            BigDecimal totalRevenue = employeeShiftService.getTotalCashRevenueForShift(employeeShiftId);
            return Response.builder()
                    .code(200)
                    .message("Success")
                    .data(totalRevenue)
                    .build();
        } catch (Exception e) {
            log.error("Error: ", e);
            return Response.builder()
                    .code(500)
                    .message("Server error: " + e.getMessage())
                    .build();
        }
    }
    private Response handleGetActiveOpenShifts(Request request) {
        EmployeeShiftActiveRequest req = (EmployeeShiftActiveRequest) request.getRequest();
        if (req == null) {
            return Response.builder()
                    .code(400)
                    .message("Request cannot be null")
                    .build();
        }
        Long employeeId = req.getEmployeeId();
        LocalDate date = req.getShiftDate();
        if (date == null) {
            date = LocalDate.now();
        }
        try {
            EmployeeShiftDTO shifts = employeeShiftService.getActiveOpenShift(employeeId, date);
            if (shifts == null ) {
                return Response.builder()
                        .code(404)
                        .message("No active open shifts found")
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Success")
                    .data(shifts)
                    .build();
        } catch (Exception e) {
            log.error("Error: ", e);
            return Response.builder()
                    .code(500)
                    .message("Server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetByEmployeeAndDate(Request request) {
        EmployeeShiftByEmpDateRequest req = (EmployeeShiftByEmpDateRequest) request.getRequest();
        if (req == null) {
            return Response.builder()
                    .code(400)
                    .message("Request cannot be null")
                    .build();
        }
        if (req.getEmployeeId() <= 0) {
            return Response.builder()
                    .code(400)
                    .message("Employee id required")
                    .build();
        }
        Long employeeId = req.getEmployeeId();
        LocalDate date = req.getShiftDate();
        if (date == null) {
            date = LocalDate.now();
        }
        try {
            List<EmployeeShiftDTO> shifts = employeeShiftService.getShiftsByEmployeeAndDate(employeeId, date);
            if (shifts == null || shifts.isEmpty()) {
                return Response.builder()
                        .code(404)
                        .message("No shifts found")
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Success")
                    .data(shifts)
                    .build();
        } catch (Exception e) {
            log.error("Error: ", e);
            return Response.builder()
                    .code(500)
                    .message("Server error: " + e.getMessage())
                    .build();
        }
    }


    private Response handleGetById(Request request) {
        Long id = (Long) request.getRequest();
        if (id == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee shift id cannot be null")
                    .build();
        }
        EmployeeShiftDTO employeeShift = employeeShiftService.findEmployeeShiftById(id);
        if (employeeShift == null) {
            return Response.builder()
                    .code(404)
                    .message("Employee shift not found with id: " + id)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get employee shift by id successfully")
                .data(employeeShift)
                .build();
    }

    private Response handleDelete(Request request) {
        Long id = (Long) request.getRequest();
        if (id == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee shift id cannot be null")
                    .build();
        }
        try {
            employeeShiftService.deleteEmployeeShift(id);
            return Response.builder()
                    .code(200)
                    .message("Employee shift deleted successfully")
                    .build();
        } catch (Exception e) {
            log.error("Error deleting employee shift", e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdate(Request request) {
        EmployeeShiftDTO employeeShiftDTO = (EmployeeShiftDTO) request.getRequest();
        if (employeeShiftDTO == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee shift data cannot be null")
                    .build();
        }
        try {
            EmployeeShiftDTO updatedEmployeeShift = employeeShiftService.updateEmployeeShift(employeeShiftDTO);
            if (updatedEmployeeShift == null) {
                return Response.builder()
                        .code(404)
                        .message("Employee shift not found with id: " + employeeShiftDTO.getEmployeeShiftId())
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Employee shift updated successfully")
                    .data(updatedEmployeeShift)
                    .build();
        } catch (Exception e) {
            log.error("Error updating employee shift", e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        EmployeeShiftDTO employeeShiftDTO = (EmployeeShiftDTO) request.getRequest();
        if (employeeShiftDTO == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee shift data cannot be null")
                    .build();
        }
        try {
            EmployeeShiftDTO createdEmployeeShift = employeeShiftService.addEmployeeShift(employeeShiftDTO);
            if (createdEmployeeShift == null) {
                return Response.builder()
                        .code(500)
                        .message("Failed to create employee shift")
                        .build();
            }
            return Response.builder()
                    .code(201)
                    .message("Employee shift created successfully")
                    .data(createdEmployeeShift)
                    .build();
        } catch (Exception e) {
            log.error("Error creating employee shift", e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAll(Request request) {
        List<EmployeeShiftDTO> employeeShifts = employeeShiftService.getAllEmployeeShifts();
        if (employeeShifts.isEmpty()) {
            return Response.builder()
                    .code(404)
                    .message("No employee shifts found")
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get all employee shifts successfully")
                .data(employeeShifts)
                .build();
    }

    private Response handleGetShiftByDate(Request request) {
        LocalDate date = (LocalDate) request.getRequest();
        if (date == null) {
            return Response.builder()
                    .code(400)
                    .message("Shift date cannot be null")
                    .build();
        }
        List<EmployeeShiftDTO> employeeShifts = employeeShiftService.getShiftsByEmployeeAndDate(null, date);
        if (employeeShifts.isEmpty()) {
            return Response.builder()
                    .code(404)
                    .message("No employee shifts found for date: " + date)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get employee shifts by date successfully")
                .data(employeeShifts)
                .build();
    }

    private Response handleGetWithDetails(Request request) {
        Long id = (Long) request.getRequest();
        if (id == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee shift id cannot be null")
                    .build();
        }
        EmployeeShiftDTO employeeShift = employeeShiftService.getEmployeeShiftWithDetails(id);
        if (employeeShift == null) {
            return Response.builder()
                    .code(404)
                    .message("Employee shift not found with id: " + id)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get employee shift with details successfully")
                .data(employeeShift)
                .build();
    }

}