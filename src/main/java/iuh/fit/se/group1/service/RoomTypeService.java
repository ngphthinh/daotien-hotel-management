package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.repository.jpa.RoomTypeRepositoryImpl;

import java.util.List;

public class RoomTypeService {
    private final RoomTypeRepositoryImpl roomTypeRepositoryImpl;

    public RoomTypeService() {
        this.roomTypeRepositoryImpl = new RoomTypeRepositoryImpl();
    }


    public RoomType createRoomType(RoomType roomType) {
        return roomTypeRepositoryImpl.save(roomType);
    }

    public RoomType updateRoomType(RoomType roomType) {
        return roomTypeRepositoryImpl.update(roomType);
    }

    public void deleteRoomType(String roomTypeId) {
        roomTypeRepositoryImpl.deleteById(roomTypeId);
    }

    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepositoryImpl.findAll();
    }

    public RoomType getRoomTypeById(String roomTypeId) {
        return roomTypeRepositoryImpl.findById(roomTypeId);
    }
}