package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.EmployeeShiftDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class EmployeeShiftServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;
    public Response addEmployeeShift(EmployeeShiftDTO employeeShiftDTO) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(iuh.fit.se.group1.network.CommandType.EMPLOYEE_SHIFT_CREATE)
                .request(employeeShiftDTO)
                .build()).get();
    }
    public Response updateEmployeeShift(EmployeeShiftDTO employeeShiftDTO) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(iuh.fit.se.group1.network.CommandType.EMPLOYEE_SHIFT_UPDATE)
                .request(employeeShiftDTO)
                .build()).get();
    }
    public Response getEmployeeShiftById(Long id) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(iuh.fit.se.group1.network.CommandType.EMPLOYEE_SHIFT_GET_BY_ID)
                .request(id)
                .build()).get();
    }
    public Response deleteEmployeeShift(Long id) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(iuh.fit.se.group1.network.CommandType.EMPLOYEE_SHIFT_DELETE)
                .request(id)
                .build()).get();
    }
    public Response getALlEmployeeShifts() throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(CommandType.EMPLOYEE_SHIFT_GET_ALL)
                .build()).get();
    }
    public Response getShiftsByEmployeeAndDate(Long employeeId, LocalDate date) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(CommandType.EMPLOYEE_SHIFT_GET_BY_EMPLOYEE_AND_DATE)
                .request(new Object[]{employeeId, date})
                .build()).get();
    }
    public Response getShiftByDate(LocalDate date) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(CommandType.EMPLOYEE_SHIFT_GET_SHIFT_BY_DATE)
                .request(date)
                .build()).get();
    }
    public Response getEmployeeShiftWithDetails(long employeeShiftId) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(CommandType.EMPLOYEE_SHIFT_GET_WITH_DETAILS)
                .request(employeeShiftId)
                .build()).get();
    }
    public Response getTotalCashRevenueForShift(long employeeShiftId) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(CommandType.EMPLOYEE_SHIFT_GET_TOTAL_REVENUE)
                .request(employeeShiftId)
                .build()).get();
    }
    public Response getActiveOpenShift(long employeeId,LocalDate date) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(CommandType.EMPLOYEE_SHIFT_GET_ACTIVE_OPEN_SHIFTS)
                .request(new Object[]{employeeId, date})
                .build()).get();
    }
}
