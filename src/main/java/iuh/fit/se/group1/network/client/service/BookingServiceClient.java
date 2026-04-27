package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.BookingViewDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookingServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;


    public Response getPriceFromBooking(BookingViewDTO booking) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.BOOKING_GET_PRICE_FROM_BOOKING)
                .request(booking)
                .build()).get();
    }
}
