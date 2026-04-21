package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.repository.interfaces.ShiftRepository;

public class ShiftRepositoryImpl extends AbstractRepositoryImpl<Shift, Long> implements ShiftRepository {
    public ShiftRepositoryImpl() {
        super(Shift.class);
    }

}
