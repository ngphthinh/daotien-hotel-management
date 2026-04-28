package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.EmployeeCreateRequest;
import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class EmployeeHandler implements RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(EmployeeHandler.class);
    private final EmployeeService employeeService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        try {
            return switch (commandType) {
                case EMPLOYEE_GET_BY_ID -> handleGetById(request);
                case EMPLOYEE_GET_BY_ACCOUNT_ID -> handleGetByAccountId(request);
                case EMPLOYEE_GET_ALL -> handleGetAll();
                case EMPLOYEE_GET_BY_KEYWORDS -> handleGetByKeywords(request);
                case EMPLOYEE_GET_BY_CITIZEN_ID -> handleGetByCitizenId(request);
                case EMPLOYEE_GET_BY_ROLE_ID -> handleGetByRoleId(request);
                case EMPLOYEE_CREATE -> handleCreate(request);
                case EMPLOYEE_UPDATE -> handleUpdate(request);
                case EMPLOYEE_DELETE -> handleDelete(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling employee request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetById(Request request) {
        Long employeeId = (Long) request.getRequest();
        if (employeeId == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee ID cannot be null")
                    .build();
        }

        EmployeeDTO employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            return Response.builder()
                    .code(404)
                    .message("Employee not found with ID: " + employeeId)
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get employee successfully")
                .data(employee)
                .build();
    }

    private Response handleGetByAccountId(Request request) {
        String accountId = request.getRequest().toString();
        if (accountId == null || accountId.isEmpty()) {
            return Response.builder()
                    .code(400)
                    .message("Account ID cannot be null or empty")
                    .build();
        }

        EmployeeDTO employee = employeeService.getEmployeeByAccountId(accountId);
        if (employee == null) {
            return Response.builder()
                    .code(404)
                    .message("Employee not found with Account ID: " + accountId)
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get employee by account ID successfully")
                .data(employee)
                .build();
    }


    private Response handleGetAll() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        if (employees == null || employees.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No employees found")
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get all employees successfully")
                .data(employees)
                .build();
    }

    private Response handleGetByKeywords(Request request) {
        String keyword = request.getRequest().toString();
        if (keyword == null || keyword.isEmpty()) {
            return Response.builder()
                    .code(400)
                    .message("Keyword cannot be null or empty")
                    .build();
        }

        List<EmployeeDTO> employees = employeeService.getEmployeeByKeyword(keyword);

        if (employees == null || employees.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No employees found matching keyword: " + keyword)
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Search employees by keyword successfully")
                .data(employees)
                .build();
    }

    private Response handleGetByCitizenId(Request request) {
        String citizenId = request.getRequest().toString();
        if (citizenId == null || citizenId.isEmpty()) {
            return Response.builder()
                    .code(400)
                    .message("Citizen ID cannot be null or empty")
                    .build();
        }

        EmployeeDTO employee = employeeService.getEmployeeByCitizenId(citizenId);
        if (employee == null) {
            return Response.builder()
                    .code(404)
                    .message("Employee not found with Citizen ID: " + citizenId)
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get employee by citizen ID successfully")
                .data(employee)
                .build();
    }

    private Response handleGetByRoleId(Request request) {
        try {
            String roleId = (String) request.getRequest();

            if (roleId == null || roleId.isEmpty()) {
                return Response.builder()
                        .code(400)
                        .message("Role ID cannot be null or empty")
                        .build();
            }

            List<EmployeeDTO> employees = employeeService.findAllByRoleId(roleId);

            return Response.builder()
                    .code(200)
                    .message(employees.isEmpty()
                            ? "No employees found with Role ID: " + roleId
                            : "Get employees by role ID successfully")
                    .data(employees)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        EmployeeCreateRequest employeeCreateRequest = (EmployeeCreateRequest) request.getRequest();
        EmployeeDTO employeeDTO = employeeCreateRequest.getEmployee();
        String roleId = employeeCreateRequest.getRoleId();

        if (employeeDTO == null || roleId == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee data and role ID cannot be null")
                    .build();
        }

        try {
            EmployeeDTO created = employeeService.createEmployee(employeeDTO, roleId);
            
            Response response = Response.builder()
                    .code(200)
                    .message("Employee created successfully")
                    .data(created)
                    .build();



            return response;
        } catch (IllegalArgumentException e) {
            return Response.builder()
                    .code(400)
                    .message("Invalid input: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdate(Request request) {
        EmployeeDTO employeeDTO = (EmployeeDTO) request.getRequest();

        if (employeeDTO == null || employeeDTO.getEmployeeId() == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee data and employee ID cannot be null")
                    .build();
        }

        try {
            EmployeeDTO updated = employeeService.updateEmployee(employeeDTO);
            
            Response response = Response.builder()
                    .code(200)
                    .message("Employee updated successfully")
                    .data(updated)
                    .build();



            return response;
        } catch (Exception e) {
            return Response.builder()
                    .code(400)
                    .message("Error updating employee: " + e.getMessage())
                    .build();
        }
    }

    private Response handleDelete(Request request) {
        Long employeeId = (Long) request.getRequest();

        if (employeeId == null) {
            return Response.builder()
                    .code(400)
                    .message("Employee ID cannot be null")
                    .build();
        }

        try {
            employeeService.deleteEmployee(employeeId);
            
            Response response = Response.builder()
                    .code(200)
                    .message("Employee deleted successfully")
                    .build();



            return response;
        } catch (IllegalStateException e) {
            return Response.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error deleting employee: " + e.getMessage())
                    .build();
        }
    }
}
