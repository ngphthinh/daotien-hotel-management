package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.dto.RoomPriceByTypeRequest;
import iuh.fit.se.group1.service.RoomToolsService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class RoomToolsHandler implements RequestHandler {

    private final RoomToolsService roomToolsService;


    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case ROOM_TOOL_TRANSFER_ROOMS -> handleTransferRooms(request);
                case ROOM_TOOL_CALCULATE_EXTENSION_AMOUNT -> handleCalculateExtensionAmount(request);
                case ROOM_TOOL_EXTEND_ROOM_BOOKING -> handleExtendRoomBooking(request);
                case ROOM_TOOL_ROOM_PRICE_WITH_DURATION -> handleRoomPriceWithDuration(request);
                case ROOM_TOOL_VALIDATE_TRANSFER -> handleValidateTransfer(request);
                case ROOM_TOOL_GET_AVAILABLE_ROOMS_BY_TYPE -> handleGetAvailableRoomsByType(request);
                case ROOM_TOOL_CANCEL_ROOM_BOOKING -> handleCancelRoomBooking(request);
                case ROOM_TOOL_CALCULATE_NEW_ROOM_PRICE_WITH_BOOKING_DURATION ->
                        handleCalculateNewRoomPriceWithBookingDuration(request);
                case ROOM_TOOL_GET_ROOMS_BY_ORDER_AND_TYPE -> handleGetRoomsByOrderAndType(request);
                case ROOM_TOOL_GET_ROOM_PRICE_BY_TYPE -> handleGetRoomPriceByType(request);
                case ROOM_TOOL_CALCULATE_SURCHARGE -> handleCalculateSurcharge(request);

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

    private Response handleCalculateSurcharge(Request request) {
        try {
            TransferRoomRequest transferRoomRequest = (TransferRoomRequest) request.getRequest();

            TransferResult transferResult = roomToolsService.transferRooms(
                    transferRoomRequest.getOrderId(),
                    transferRoomRequest.getSelectedOldRooms(),
                    transferRoomRequest.getSelectedNewRooms(),
                    transferRoomRequest.getCurrentBookingType()
            );

            return Response.builder()
                    .code(200)
                    .message("Surcharge calculated successfully")
                    .data(transferResult)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to calculate surcharge: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetRoomPriceByType(Request request) {

        try {

            RoomPriceByTypeRequest roomPriceByTypeRequest = (RoomPriceByTypeRequest) request.getRequest();

            long price = roomToolsService.getRoomPriceByType(roomPriceByTypeRequest.getNewRoom(), roomPriceByTypeRequest.getCurrentBookingType());

            return Response.builder()
                    .code(200)
                    .message("Room price calculated successfully")
                    .data(price)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get room price by type: " + e.getMessage())
                    .build();
        }

    }

    private Response handleGetRoomsByOrderAndType(Request request) {
        try {
            RoomToolsRequest roomToolsRequest = (RoomToolsRequest) request.getRequest();
            return Response.builder()
                    .code(200)
                    .message("Rooms retrieved successfully")
                    .data(roomToolsService.getRoomsByOrderAndType(roomToolsRequest.getOrderId(), roomToolsRequest.getCurrentBookingType()))
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get rooms by order and type: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCalculateNewRoomPriceWithBookingDuration(Request request) {
        try {
            RoomPriceByTypeRequest roomPriceByTypeRequest = (RoomPriceByTypeRequest) request.getRequest();

            long price = roomToolsService.calculateNewRoomPriceWithBookingDuration(
                    roomPriceByTypeRequest.getNewRoom(),
                    roomPriceByTypeRequest.getCurrentBookingType(),
                    roomPriceByTypeRequest.getOrderId(),
                    roomPriceByTypeRequest.getReferenceRoom());

            return Response.builder()
                    .code(200)
                    .message("New room price calculated successfully")
                    .data(price)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to calculate new room price with booking duration: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCancelRoomBooking(Request request) {

        try {
            CancelRoomBookingRequest cancelRoomBookingRequest = (CancelRoomBookingRequest) request.getRequest();

            boolean isSuccess =
                    roomToolsService.cancelRoomBooking(cancelRoomBookingRequest.getOrderId(), cancelRoomBookingRequest.getRoomId(), cancelRoomBookingRequest.getCurrentBookingType());

            return Response.builder()
                    .code(isSuccess ? 200 : 400)
                    .message(isSuccess ? "Room booking cancelled successfully" : "Failed to cancel room booking")
                    .data(isSuccess)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to cancel room booking: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAvailableRoomsByType(Request request) {
        try {

            String roomType = request.getRequest().toString();

            return Response.builder()
                    .code(200)
                    .message("Available rooms retrieved successfully")
                    .data(roomToolsService.getAvailableRoomsByType(roomType))
                    .build();

        } catch (
                Exception e
        ) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get available rooms by type: " + e.getMessage())
                    .build();
        }
    }

    private Response handleValidateTransfer(Request request) {
        try {
            TransferRoomRequest transferRoomRequest = (TransferRoomRequest) request.getRequest();

            ValidationResult validationResult = roomToolsService.validateTransfer(
                    transferRoomRequest.getSelectedOldRooms(),
                    transferRoomRequest.getSelectedNewRooms(),
                    transferRoomRequest.getCurrentBookingType()
            );


            return Response.builder()
                    .code(200)
                    .message("Transfer successfully")
                    .data(validationResult)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to validate transfer: " + e.getMessage())
                    .build();
        }
    }

    private Response handleRoomPriceWithDuration(Request request) {
        try {
            RoomPriceWithDurationRequest roomPriceWithDurationRequest = (RoomPriceWithDurationRequest) request.getRequest();
            long price = roomToolsService.getRoomPriceWithDuration(
                    roomPriceWithDurationRequest.getRoom(),
                    roomPriceWithDurationRequest.getBookingType(),
                    roomPriceWithDurationRequest.getOrderId()
            );

            return Response.builder()
                    .code(200)
                    .message("Room price calculated successfully")
                    .data(price)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get room price with duration: " + e.getMessage())
                    .build();
        }
    }

    private Response handleExtendRoomBooking(Request request) {
        try {
            ExtendBookingRequest extendBookingRequest = (ExtendBookingRequest) request.getRequest();

            roomToolsService.extendRoomBooking(
                    extendBookingRequest.getOrderId(),
                    extendBookingRequest.getRoomsToExtend(),
                    extendBookingRequest.getExtendValue(),
                    extendBookingRequest.getBookingType()
            );

            return Response.builder()
                    .code(200)
                    .message("Extend room booking successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to get extend room booking: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCalculateExtensionAmount(Request request) {

        try {
            CalculateExtensionAmountRequest calculateExtensionAmountRequest = (CalculateExtensionAmountRequest) request.getRequest();
            BigDecimal value = roomToolsService.calculateExtensionAmount(
                    calculateExtensionAmountRequest.getRoomsToExtend(),
                    calculateExtensionAmountRequest.getBookingType(),
                    calculateExtensionAmountRequest.getExtendValue()
            );

            return Response.builder()
                    .code(200)
                    .message("Extension amount calculated successfully")
                    .data(value)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to calculate extension amount: " + e.getMessage())
                    .build();
        }
    }

    private Response handleTransferRooms(Request request) {
        try {
            TransferRoomRequest transferRoomRequest = (TransferRoomRequest) request.getRequest();
            TransferResult transferResult = roomToolsService.transferRooms(
                    transferRoomRequest.getOrderId(),
                    transferRoomRequest.getSelectedOldRooms(),
                    transferRoomRequest.getSelectedNewRooms(),
                    transferRoomRequest.getCurrentBookingType()
            );

            return Response.builder()
                    .code(200)
                    .message("Rooms transferred successfully")
                    .data(transferResult)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Failed to transfer rooms: " + e.getMessage())
                    .build();
        }
    }
}
