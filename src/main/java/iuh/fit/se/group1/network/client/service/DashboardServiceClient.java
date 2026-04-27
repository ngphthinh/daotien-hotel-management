package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.DashboardSummaryDto;
import iuh.fit.se.group1.dto.DateTimeRangeRequest;
import iuh.fit.se.group1.dto.OrderStatisticsResponse;
import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DashboardServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;


    public Response getPeakHours(LocalDateTime start, LocalDateTime end) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.DASHBOARD_PEAK_HOURS)
                .request(DateTimeRangeRequest.builder()
                        .from(start)
                        .to(end)
                        .build()
                )
                .build()).get();
    }

    public Response getDashboardData(TimeType timeType) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.DASHBOARD_GET_DATA)
                .request(timeType)
                .build()).get();
    }

    public Response getRooms() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.DASHBOARD_GET_ROOMS)
                .build()).get();
    }

    public Response getOrderStatistics(TimeType currentTimeType) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.DASHBOARD_ORDER_STATISTICS)
                .request(currentTimeType)
                .build()).get();
    }

    public Response getDashboardDataEmployee(TimeType timeType) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.DASHBOARD_GET_DATA_EMPLOYEE)
                .request(timeType)
                .build()).get();
    }
}
