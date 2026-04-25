package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.RoomDTO;
import iuh.fit.se.group1.dto.RoomViewDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.mapper.RoomMapper;
import iuh.fit.se.group1.repository.jpa.RoomRepositoryImpl;
import iuh.fit.se.group1.repository.jpa.RoomTypeRepositoryImpl;
import iuh.fit.se.group1.util.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomService extends Service {
    private final RoomRepositoryImpl roomRepository;
    private final RoomTypeRepositoryImpl roomTypeRepositoryImpl = new RoomTypeRepositoryImpl();
    private final RoomMapper roomMapper;
    private final OrderService orderService = new OrderService();

    public RoomService() {
        this.roomRepository = new RoomRepositoryImpl();
        this.roomMapper = new RoomMapper();
    }

    public RoomViewDTO createRoom(RoomViewDTO roomViewDTO) {
        return doInTransaction(entityManager -> {
            Room room = roomMapper.toRoom(roomViewDTO);

            RoomType roomType = roomTypeRepositoryImpl.findById(entityManager, room.getRoomType().getRoomTypeId()); // ensure room type exists


            room.setCreatedAt(LocalDate.now());

            room.setRoomType(roomType);

            return roomMapper.toRoomDTO(roomRepository.save(entityManager, room));
        });

    }

    public void deleteRoom(Long roomId) {

        doInTransactionVoid(entityManager -> roomRepository.deleteById(entityManager, roomId));
    }

    public List<RoomViewDTO> getAllRooms() {
        return doInTransaction(roomRepository::findAll).stream().map(roomMapper::toRoomDTO).collect(Collectors.toList());
    }

    public RoomViewDTO updateRoom(RoomViewDTO room) {
        Room roomEntity = roomMapper.toRoom(room);
        return doInTransaction(entityManager -> roomMapper.toRoomDTO(roomRepository.update(entityManager, roomEntity)));
    }


    public boolean existsByRoomNumber(String roomNumber) {
        return doInTransaction(entityManager -> roomRepository.existsByRoomNumber(entityManager, roomNumber));
    }

    public List<RoomViewDTO> getRoomByKeyword(String keyword) {
        return doInTransaction(entityManager -> roomRepository.findByRoomNumberOrId(entityManager, keyword)).stream().map(roomMapper::toRoomDTO).toList();
    }

    public List<RoomViewDTO> getRoomByStatusOrRoomType(String roomTypeId, RoomStatus status) {
        return doInTransaction(entityManager -> roomRepository.findRoomByStatusAndRoomType(entityManager, roomTypeId, status)).stream().map(roomMapper::toRoomDTO).toList();
    }


    public Map<RoomDTO, Long> countAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut) {

        List<Room> rooms = doInTransaction(entityManager -> roomRepository.findAvailableRooms(entityManager, checkIn, checkOut, RoomStatus.AVAILABLE));

        RoomType singleRoomType = doInTransaction(entityManager -> roomTypeRepositoryImpl.findById(entityManager, Constants.SINGLE_ROOM_TYPE));
        RoomType doubleRoomType = doInTransaction(entityManager -> roomTypeRepositoryImpl.findById(entityManager, Constants.DOUBLE_ROOM_TYPE));

        RoomDTO doubleRoom = roomMapper.toRoomDTO(doubleRoomType);
        RoomDTO singleRoom = roomMapper.toRoomDTO(singleRoomType);

        Long singleRooms = rooms.stream()
                .filter(room -> room.getRoomType().getRoomTypeId().equals(Constants.SINGLE_ROOM_TYPE))
                .count();

        Long doubleRooms = rooms.stream()
                .filter(room -> room.getRoomType().getRoomTypeId().equals(Constants.DOUBLE_ROOM_TYPE))
                .count();

        Map<RoomDTO, Long> map = new LinkedHashMap<>();
        map.put(singleRoom, singleRooms);
        map.put(doubleRoom, doubleRooms);
        return map;
    }


    /**
     * Optimize room allocation based on available rooms and guest requirements.
     *
     * @param availableSingleRooms
     * @param availableDoubleRooms
     * @param adultGuests
     * @param childGuests
     * @return Map with keys: "usedSingleRooms", "usedDoubleRooms", "totalRoomsUsed",
     * "accommodatedAdults", "accommodatedChildren", "unaccommodatedAdults",
     * "unaccommodatedChildren", "unaccommodatedGuests"
     */
    public Map<String, Long> optimizeRoomAllocation(
            int availableSingleRooms,
            int availableDoubleRooms,
            int adultGuests,
            int childGuests) {

        Map<String, Long> result = new HashMap<>();

        // Constants
        final int SINGLE_ADULT_CAPACITY = 2;
        final int SINGLE_CHILD_CAPACITY = 1;
        final int DOUBLE_ADULT_CAPACITY = 4;
        final int DOUBLE_CHILD_CAPACITY = 2;

        int usedSingle = 0;
        int usedDouble = 0;
        int remainingAdults = adultGuests;
        int remainingChildren = childGuests;

        // Continue until no guests left or no rooms left
        while ((remainingAdults > 0 || remainingChildren > 0) &&
                (usedSingle < availableSingleRooms || usedDouble < availableDoubleRooms)) {

            boolean preferDouble;

            // 🎯 RULE: tự động chọn theo số người lớn còn lại
            if (remainingAdults >= 3) {
                preferDouble = true;
            } else {
                preferDouble = false;
            }

            // Nếu ưu tiên phòng đôi nhưng hết phòng đôi => dùng phòng đơn
            if (preferDouble && usedDouble >= availableDoubleRooms) {
                preferDouble = false;
            }

            // Nếu ưu tiên phòng đơn nhưng hết phòng đơn => dùng phòng đôi
            if (!preferDouble && usedSingle >= availableSingleRooms) {
                preferDouble = true;
            }

            // -------------------------------
            // Gán khách vào phòng đôi
            // -------------------------------
            if (preferDouble) {
                int adultsInRoom = Math.min(remainingAdults, DOUBLE_ADULT_CAPACITY);
                int childrenInRoom = Math.min(remainingChildren, DOUBLE_CHILD_CAPACITY);

                if (adultsInRoom == 0 && childrenInRoom == 0) break;

                remainingAdults -= adultsInRoom;
                remainingChildren -= childrenInRoom;
                usedDouble++;
            }
            // -------------------------------
            // Gán khách vào phòng đơn
            // -------------------------------
            else {
                int adultsInRoom = Math.min(remainingAdults, SINGLE_ADULT_CAPACITY);
                int childrenInRoom = Math.min(remainingChildren, SINGLE_CHILD_CAPACITY);

                if (adultsInRoom == 0 && childrenInRoom == 0) break;

                remainingAdults -= adultsInRoom;
                remainingChildren -= childrenInRoom;
                usedSingle++;
            }
        }

        // Build result
        result.put("usedSingleRooms", (long) usedSingle);
        result.put("usedDoubleRooms", (long) usedDouble);
        result.put("totalRoomsUsed", (long) (usedSingle + usedDouble));
        result.put("accommodatedAdults", (long) (adultGuests - remainingAdults));
        result.put("accommodatedChildren", (long) (childGuests - remainingChildren));
        result.put("unaccommodatedAdults", (long) remainingAdults);
        result.put("unaccommodatedChildren", (long) remainingChildren);
        result.put("unaccommodatedGuests", (long) (remainingAdults + remainingChildren));

        return result;
    }

    /**
     * Kiểm tra xem số phòng đã dùng có đủ chỗ cho số người lớn và trẻ em không
     *
     * @param adults
     * @param children
     * @param usedSingleRoomsNum
     * @param usedDoubleRoomsNum
     * @return Map với các key: "usedSingleRooms", "usedDoubleRooms",
     * "leftoverAdults", "leftoverChildren", "leftoverTotal"
     */
    public Map<String, Integer> checkRoomCapacity(
            int adults,
            int children,
            Number usedSingleRoomsNum,
            Number usedDoubleRoomsNum
    ) {
        // Updated capacity
        final int SINGLE_A = 2, SINGLE_C = 1;
        final int DOUBLE_A = 4, DOUBLE_C = 2;

        int usedSingleRooms = usedSingleRoomsNum.intValue();
        int usedDoubleRooms = usedDoubleRoomsNum.intValue();

        int totalAdultCapacity =
                usedSingleRooms * SINGLE_A +
                        usedDoubleRooms * DOUBLE_A;

        int totalChildCapacity =
                usedSingleRooms * SINGLE_C +
                        usedDoubleRooms * DOUBLE_C;

        // Remaining (nếu > 0 thì không đủ phòng)
        int leftoverAdults = Math.max(0, adults - totalAdultCapacity);
        int leftoverChildren = Math.max(0, children - totalChildCapacity);

        Map<String, Integer> result = new HashMap<>();
        result.put("usedSingleRooms", usedSingleRooms);
        result.put("usedDoubleRooms", usedDoubleRooms);
        result.put("leftoverAdults", leftoverAdults);
        result.put("leftoverChildren", leftoverChildren);
        result.put("leftoverTotal", leftoverAdults + leftoverChildren);

        return result;
    }

    public List<RoomViewDTO> getAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut) {
//        return roomRepository.findAvailableRooms(checkIn, checkOut, RoomStatus.AVAILABLE);
        return doInTransaction(entityManager -> roomRepository.findAvailableRooms(entityManager, checkIn, checkOut, RoomStatus.AVAILABLE)).stream().map(roomMapper::toRoomDTO).toList();
    }

    public RoomViewDTO getRoomByRoomId(Long roomId) {
//        return roomRepository.findById(roomId);
        return doInTransaction(entityManager -> roomMapper.toRoomDTO(roomRepository.findById(entityManager, roomId)));
    }

    public boolean canDeleteRoom(Long roomId) {
        // Tìm trong hóa đơn ordertype = 3 (Đặt trước) xem có roomId này không
        // Nếu có thì thay bằng 1 phòng khác cùng loại, đang trống và chưa được đặt trước
//        Long orderTypeBooked = 3L;
//        List<Order> ordersUsingRoom = orderService.getOrdersByRoomIdAndOrderType(roomId, orderTypeBooked);
//
//        if (!ordersUsingRoom.isEmpty()) {
//            // Lấy thông tin phòng cần xóa
////            Room roomToDelete = roomRepository.findById(roomId);
//            Room roomToDelete = doInTransaction(entityManager -> roomRepository.findById(entityManager, roomId));
//            if (roomToDelete == null) {
//                return false;
//            }
//
//            String roomTypeId = roomToDelete.getRoomType().getRoomTypeId();
//
//            // Duyệt qua từng order đang dùng phòng này
//            for (Order order : ordersUsingRoom) {
//                BookingService bookingService = new BookingService();
//
////                List<Booking> bookings = bookingService.getBookingsByOrderId(order.getOrderId());
//                List<Booking> bookings = doInTransaction(entityManager -> bookingService.getBookingsByOrderId(entityManager, order.getOrderId()));
//
//                // Lấy booking của phòng này trong order
//                Booking bookingToReplace = bookings.stream()
//                        .filter(b -> b.getRoom().getRoomId().equals(roomId))
//                        .findFirst()
//                        .orElse(null);
//
//                if (bookingToReplace == null) {
//                    continue;
//                }
//
//                // Tìm phòng thay thế: cùng loại, trống, chưa được đặt trong khoảng thời gian này
//                List<Room> availableRooms = roomRepository.findAvailableRooms(
//                        bookingToReplace.getCheckInDate(),
//                        bookingToReplace.getCheckOutDate(),
//                        RoomStatus.AVAILABLE
//                );
//
//                Room replacementRoom = availableRooms.stream()
//                        .filter(r -> r.getRoomType().getRoomTypeId().equals(roomTypeId))
//                        .filter(r -> !r.getRoomId().equals(roomId)) // Loại trừ phòng đang xóa
//                        .findFirst()
//                        .orElse(null);
//
//                if (replacementRoom == null) {
//                    // Không tìm thấy phòng thay thế -> không thể xóa
//                    return false;
//                }
//
//                // Cập nhật booking sang phòng mới
//                roomRepository.updateBookingRoom(bookingToReplace.getBookingId(), replacementRoom.getRoomId());
//            }
//        }
//
//        // Kiểm tra phòng có đang được sử dụng trong các order khác không
//        return !roomRepository.isRoomInUse(roomId);
        Long orderTypeBooked = 3L;
        List<Order> ordersUsingRoom = orderService.getOrdersByRoomIdAndOrderType(roomId, orderTypeBooked);

        if (!ordersUsingRoom.isEmpty()) {
            Room roomToDelete = doInTransaction(entityManager -> roomRepository.findById(entityManager, roomId));
            if (roomToDelete == null) {
                return false;
            }

            String roomTypeId = roomToDelete.getRoomType().getRoomTypeId();

            for (Order order : ordersUsingRoom) {
                BookingService bookingService = new BookingService();

                List<Booking> bookings = doInTransaction(entityManager -> bookingService.getBookingsEntityByOrderId(entityManager, order.getOrderId()));

                Booking bookingToReplace = bookings.stream()
                        .filter(b -> b.getRoom().getRoomId().equals(roomId))
                        .findFirst()
                        .orElse(null);

                if (bookingToReplace == null) {
                    continue;
                }

                List<Room> availableRooms = doInTransaction(entityManager -> roomRepository.findAvailableRooms(entityManager, bookingToReplace.getCheckInDate(), bookingToReplace.getCheckOutDate(), RoomStatus.AVAILABLE));

                Room replacementRoom = availableRooms.stream()
                        .filter(r -> r.getRoomType().getRoomTypeId().equals(roomTypeId))
                        .filter(r -> !r.getRoomId().equals(roomId))
                        .findFirst()
                        .orElse(null);

                if (replacementRoom == null) {
                    return false;
                }

                doInTransactionVoid(entityManager -> roomRepository.updateBookingRoom(entityManager, bookingToReplace.getBookingId(), replacementRoom.getRoomId()));
            }
        }

        return doInTransaction(entityManager -> !roomRepository.isRoomInUse(entityManager, roomId));
    }

    public void updateRoomStatusBatch(List<Long> roomIds, RoomStatus roomStatus) {
//        roomRepository.updateRoomStatusBatch(roomIds, roomStatus);
        doInTransactionVoid(entityManager -> roomRepository.updateRoomStatusBatch(entityManager, roomIds, roomStatus));
    }

    public List<RoomViewDTO> getRoomsByOrderIdAndType(long orderId, String bookingType) {
//        return roomRepository.getRoomsByOrderIdAndType(orderId, bookingType);
        return doInTransaction(entityManager -> roomRepository.getRoomsByOrderIdAndType(entityManager, orderId, bookingType)).stream().map(roomMapper::toRoomDTO).toList();
    }

    public List<RoomViewDTO> getRoomsByBookingId(long bookingId) {
//        return roomRepository.getRoomsByBookingId(bookingId);
        return doInTransaction(entityManager -> roomRepository.getRoomsByBookingId(entityManager, bookingId)).stream().map(roomMapper::toRoomDTO).toList();

    }

    public List<RoomViewDTO> getAvailableRoomsByType(String roomTypeId) {
//        return roomRepository.getAvailableRoomsByType(roomTypeId);
        return doInTransaction(entityManager -> roomRepository.getAvailableRoomsByType(entityManager, roomTypeId)).stream().map(roomMapper::toRoomDTO).toList();
    }

    public boolean transferRooms(long orderId, String bookingType, List<Long> oldRoomIds, List<Long> newRoomIds) {
//        return roomRepository.transferRooms(orderId, bookingType, oldRoomIds, newRoomIds);
        return doInTransaction(entityManager -> roomRepository.transferRooms(entityManager, orderId, bookingType, oldRoomIds, newRoomIds));
    }

}
