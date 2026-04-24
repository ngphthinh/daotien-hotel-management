package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.repository.jpa.RoomTypeRepositoryImpl;

import java.util.List;

public class RoomTypeService extends Service {
    private final RoomTypeRepositoryImpl roomTypeRepositoryImpl;

    public RoomTypeService() {
        this.roomTypeRepositoryImpl = new RoomTypeRepositoryImpl();
    }


    public RoomType createRoomType(RoomType roomType) {
//        return roomTypeRepositoryImpl.save(roomType);
        return doInTransaction(entityManager -> roomTypeRepositoryImpl.save(entityManager, roomType));
    }

    public RoomType updateRoomType(RoomType roomType) {
//        return roomTypeRepositoryImpl.update(roomType);
        return doInTransaction(entityManager -> roomTypeRepositoryImpl.update(entityManager, roomType));
    }

    public void deleteRoomType(String roomTypeId) {
//        roomTypeRepositoryImpl.deleteById(roomTypeId);
        doInTransactionVoid(entityManager -> roomTypeRepositoryImpl.deleteById(entityManager, roomTypeId));
    }

    public List<RoomType> getAllRoomTypes() {
//        return roomTypeRepositoryImpl.findAll();
        return doInTransaction(roomTypeRepositoryImpl::findAll);
    }

    public RoomType getRoomTypeById(String roomTypeId) {
//        return roomTypeRepositoryImpl.findById(roomTypeId);
        return doInTransaction(entityManager -> roomTypeRepositoryImpl.findById(entityManager, roomTypeId));
    }
}