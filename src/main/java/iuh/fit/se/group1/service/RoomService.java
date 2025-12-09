package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.RoomDTO;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.repository.RoomRepository;
import iuh.fit.se.group1.repository.RoomTypeRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository = new RoomTypeRepository();
    private static final String SINGLE_ROOM_TYPE = "SINGLE";
    private static final String DOUBLE_ROOM_TYPE = "DOUBLE";

    public RoomService() {
        this.roomRepository = new RoomRepository();
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room updateRoom(Room room) {
        return roomRepository.update(room);
    }

    public List<Room> getRoomByKeyword(String keyword) {
        return roomRepository.findByRoomNumberOrId(keyword);
    }

    public List<Room> getRoomByStatusOrRoomType(String roomTypeId, RoomStatus status) {
        return roomRepository.findRoomByStatusAndRoomType(roomTypeId, status);
    }

    public RoomDTO toRoomDTO(RoomType rt) {
        if (rt == null) {
            return null;
        }
        int capacity = rt.getRoomTypeId().equals(SINGLE_ROOM_TYPE) ? 2 : 4;


        return new RoomDTO(
                rt.getRoomTypeId(),
                capacity,
                rt.getHourlyRate(),
                rt.getDailyRate(),
                rt.getOvernightRate(),
                rt.getAdditionalHourRate()
        );
    }




    public Map<RoomDTO, Long> countAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Room> rooms = roomRepository.findAvailableRooms(checkIn, checkOut, RoomStatus.AVAILABLE);

        RoomType singleRoomType = roomTypeRepository.findById(SINGLE_ROOM_TYPE);
        RoomType doubleRoomType = roomTypeRepository.findById(DOUBLE_ROOM_TYPE);

        RoomDTO singleRoom = toRoomDTO(singleRoomType);
        RoomDTO doubleRoom = toRoomDTO(doubleRoomType);

        Long singleRooms = rooms.stream()
                .filter(room -> room.getRoomType().getRoomTypeId().equals(SINGLE_ROOM_TYPE))
                .count();

        Long doubleRooms = rooms.stream()
                .filter(room -> room.getRoomType().getRoomTypeId().equals(DOUBLE_ROOM_TYPE))
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

    public List<Room> getAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut) {
        return roomRepository.findAvailableRooms(checkIn, checkOut, RoomStatus.AVAILABLE);
    }

    public Room getRoomByRoomId(Long roomId) {
        return roomRepository.findById(roomId);
    }
}
