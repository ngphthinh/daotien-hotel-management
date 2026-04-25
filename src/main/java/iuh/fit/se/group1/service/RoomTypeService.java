package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.RoomTypeDTO;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.mapper.RoomTypeMapper;
import iuh.fit.se.group1.repository.jpa.RoomTypeRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class RoomTypeService extends Service {
    private final RoomTypeRepositoryImpl roomTypeRepositoryImpl;
    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeService() {
        this.roomTypeRepositoryImpl = new RoomTypeRepositoryImpl();
        this.roomTypeMapper = new RoomTypeMapper();
    }


    public RoomTypeDTO createRoomType(RoomTypeDTO roomType) {
//        return roomTypeRepositoryImpl.save(roomType);

        RoomType roomTypeEntity = roomTypeMapper.toRoomType(roomType);

        return doInTransaction(entityManager -> roomTypeMapper.toRoomTypeDTO(roomTypeRepositoryImpl.save(entityManager, roomTypeEntity)));
    }

    public RoomTypeDTO updateRoomType(RoomTypeDTO roomType) {
        RoomType roomTypeEntity = roomTypeMapper.toRoomType(roomType);

        return doInTransaction(entityManager -> roomTypeMapper.toRoomTypeDTO(roomTypeRepositoryImpl.update(entityManager, roomTypeEntity)));
    }

    public void deleteRoomType(String roomTypeId) {
        doInTransactionVoid(entityManager -> roomTypeRepositoryImpl.deleteById(entityManager, roomTypeId));
    }

    public List<RoomTypeDTO> getAllRoomTypes() {
//        return roomTypeRepositoryImpl.findAll();
        return doInTransaction(roomTypeRepositoryImpl::findAll).stream().map(roomTypeMapper::toRoomTypeDTO).collect(Collectors.toList());
    }

    public RoomTypeDTO getRoomTypeById(String roomTypeId) {
//        return roomTypeRepositoryImpl.findById(roomTypeId);
        return doInTransaction(entityManager -> roomTypeMapper.toRoomTypeDTO(roomTypeRepositoryImpl.findById(entityManager, roomTypeId)));
    }
}