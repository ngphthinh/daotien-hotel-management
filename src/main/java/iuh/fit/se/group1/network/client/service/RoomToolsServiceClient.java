package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoomToolsServiceClient implements ServiceClient {
    private final ClientSocketManager socket;


    public Response getRoomPriceWithDuration(RoomViewDTO room, BookingType bookingType, Long orderId) throws Exception {
        RoomPriceWithDurationRequest request = RoomPriceWithDurationRequest.builder()
                .room(room)
                .bookingType(bookingType)
                .orderId(orderId)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_ROOM_PRICE_WITH_DURATION)
                .request(request)
                .build()).get();

    }

    public Response calculateExtensionAmount(List<RoomViewDTO> roomsToExtend, BookingType bookingType, int extendValue) throws Exception {
        CalculateExtensionAmountRequest request = CalculateExtensionAmountRequest.builder()
                .roomsToExtend(roomsToExtend)
                .bookingType(bookingType)
                .extendValue(extendValue)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_CALCULATE_EXTENSION_AMOUNT)
                .request(request)
                .build()).get();
    }

    public Response extendRoomBooking(Long orderId, List<RoomViewDTO> roomsToExtend, int extendValue, BookingType bookingType) throws Exception {
        ExtendBookingRequest request = ExtendBookingRequest.builder()
                .orderId(orderId)
                .roomsToExtend(roomsToExtend)
                .extendValue(extendValue)
                .bookingType(bookingType)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_EXTEND_ROOM_BOOKING)
                .request(request)
                .build()).get();
    }

    public Response validateTransfer(List<RoomViewDTO> selectedOldRooms, List<RoomViewDTO> selectedNewRooms, BookingType currentBookingType) throws Exception {
        TransferRoomRequest request = TransferRoomRequest.builder()
                .selectedOldRooms(selectedOldRooms)
                .selectedNewRooms(selectedNewRooms)
                .currentBookingType(currentBookingType)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_VALIDATE_TRANSFER)
                .request(request)
                .build()).get();
    }

    public Response calculateSurcharge(List<RoomViewDTO> selectedOldRooms, List<RoomViewDTO> selectedNewRooms, BookingType currentBookingType, Long orderId) throws Exception {

        CalculateSurchargeRequest request = CalculateSurchargeRequest.builder()
                .selectedOldRooms(selectedOldRooms)
                .selectedNewRooms(selectedNewRooms)
                .currentBookingType(currentBookingType)
                .orderId(orderId)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_CALCULATE_SURCHARGE)
                .request(request)
                .build()).get();
    }

    public Response cancelRoomBooking(Long orderId, Long roomId, BookingType currentBookingType) throws Exception {
        CancelRoomBookingRequest request = CancelRoomBookingRequest.builder()
                .orderId(orderId)
                .roomId(roomId)
                .currentBookingType(currentBookingType)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_CANCEL_ROOM_BOOKING)
                .request(request)
                .build()).get();
    }

    public Response getRoomsByOrderAndType(Long orderId, BookingType currentBookingType) throws Exception {
        RoomToolsRequest request = RoomToolsRequest.builder()
                .orderId(orderId)
                .currentBookingType(currentBookingType)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_GET_ROOMS_BY_ORDER_AND_TYPE)
                .request(request)
                .build()).get();
    }

    public Response getAvailableRoomsByType(String roomTypeId) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_GET_AVAILABLE_ROOMS_BY_TYPE)
                .request(roomTypeId)
                .build()).get();
    }

    public Response getRoomPriceByType(RoomViewDTO newRoom, BookingType currentBookingType) throws Exception {
        RoomPriceByTypeRequest request = RoomPriceByTypeRequest.builder()
                .newRoom(newRoom)
                .currentBookingType(currentBookingType)
                .build();
        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_GET_ROOM_PRICE_BY_TYPE)
                .request(request)
                .build()).get();
    }

    public Response calculateNewRoomPriceWithBookingDuration(RoomViewDTO newRoom, BookingType currentBookingType, Long orderId, RoomViewDTO referenceRoom) throws Exception {
        RoomPriceByTypeRequest request = RoomPriceByTypeRequest.builder()
                .newRoom(newRoom)
                .referenceRoom(referenceRoom)
                .currentBookingType(currentBookingType)
                .orderId(orderId)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_CALCULATE_NEW_ROOM_PRICE_WITH_BOOKING_DURATION)
                .request(request)
                .build()).get();
    }

    public Response transferRooms(Long orderId, List<RoomViewDTO> selectedOldRooms, List<RoomViewDTO> selectedNewRooms, BookingType bookingType) throws Exception {

        TransferRoomRequest request = TransferRoomRequest.builder()
                .selectedOldRooms(selectedOldRooms)
                .selectedNewRooms(selectedNewRooms)
                .currentBookingType(bookingType)
                .orderId(orderId)
                .build();

        return socket.send(Request.builder()
                .commandType(CommandType.ROOM_TOOL_TRANSFER_ROOMS)
                .request(request)
                .build()).get();
    }
}
