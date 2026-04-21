package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.repository.interfaces.RoomTypeRepository;

public class RoomTypeRepositoryImpl extends AbstractRepositoryImpl<RoomType,String>  implements RoomTypeRepository {
    public RoomTypeRepositoryImpl() {
        super(RoomType.class);
    }
}
