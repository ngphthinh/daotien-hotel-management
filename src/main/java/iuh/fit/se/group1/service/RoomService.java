package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.repository.RoomRepository;

import java.util.List;

public class RoomService {
    private final RoomRepository roomRepository;

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
}
