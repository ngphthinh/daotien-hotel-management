package iuh.fit.se.group1.handler;

import ch.qos.logback.core.net.server.Client;
import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.BookingViewDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.BookingService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class BookingHandler implements RequestHandler {

    private final BookingService bookingService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case BOOKING_GET_PRICE_FROM_BOOKING -> handleGetPriceFromBooking(request);
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

    private Response handleGetPriceFromBooking(Request request) {
        BookingViewDTO bookingViewDTO = (BookingViewDTO) request.getRequest();
        return Response.builder()
                .code(200)
                .message("Get price from booking")
                .data(bookingService.getPriceFromBooking(bookingViewDTO))

                .build();

    }
}
