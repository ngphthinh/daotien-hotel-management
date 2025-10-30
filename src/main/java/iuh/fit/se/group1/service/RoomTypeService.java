package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.repository.RoomTypeRepository;
import java.util.List;

public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService() {
        this.roomTypeRepository = new RoomTypeRepository();
    }

    public RoomType createRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    public RoomType updateRoomType(RoomType roomType) {
        return roomTypeRepository.update(roomType);
    }

    public void deleteRoomType(String roomTypeId) {
        roomTypeRepository.deleteById(roomTypeId);
    }

    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeById(String roomTypeId) {
        return roomTypeRepository.findById(roomTypeId);
    }
}