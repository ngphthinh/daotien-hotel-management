package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.CountRoomDashboard;
import iuh.fit.se.group1.dto.DateTimeRangeRequest;
import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.DashboardService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DashboardHandler implements RequestHandler {
    private final DashboardService dashboardService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case DASHBOARD_PEAK_HOURS -> handleGetPeakHours(request);
                case DASHBOARD_GET_DATA -> handleGetDashboardData(request);
                case DASHBOARD_GET_ROOMS -> handleGetRooms(request);
                case DASHBOARD_ORDER_STATISTICS -> handleGetOrderStatistics(request);
                case DASHBOARD_GET_DATA_EMPLOYEE -> handleGetDashboardDataEmployee(request);
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

    private Response handleGetDashboardDataEmployee(Request request) {
        try {
            TimeType timeType = (TimeType) request.getRequest();
            return Response.builder()
                    .code(200)
                    .message("Dashboard data for employee")
                    .data(dashboardService.getDashboardEmployee(timeType))
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get dashboard data for employee: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetOrderStatistics(Request request) {
        try {
            TimeType timeType = (TimeType) request.getRequest();
            return Response.builder()
                    .code(200)
                    .message("Order statistics")
                    .data(dashboardService.getOrderStatistics(timeType))
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get order statistics: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetRooms(Request request) {
        try {

            CountRoomDashboard countRoomDashboard = dashboardService.getCountRooms();

            return Response.builder()
                    .code(200)
                    .message("Count rooms for dashboard")
                    .data(countRoomDashboard)
                    .build();


        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get order statistics: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetDashboardData(Request request) {
        try {
            TimeType timeType = (TimeType) request.getRequest();
            return Response.builder()
                    .code(200)
                    .message("Dashboard data")
                    .data(dashboardService.getDashboardDataAll(timeType))
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get dashboard data: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetPeakHours(Request request) {
        try {
            DateTimeRangeRequest dateTimeRangeRequest = (DateTimeRangeRequest) request.getRequest();
            return Response.builder()
                    .code(200)
                    .message("Peak hours")
                    .data(dashboardService.getPeakHours(dateTimeRangeRequest.getFrom(), dateTimeRangeRequest.getTo()))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get peak hours: " + e.getMessage())
                    .build();
        }
    }
}
