package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.repository.jpa.SurchargeRepositoryImpl;

import java.util.List;

public class SurchargeService extends Service {
    private final SurchargeRepositoryImpl surchargeRepositoryImpl;

    public SurchargeService() {
        this.surchargeRepositoryImpl = new SurchargeRepositoryImpl();
    }

    public Surcharge createSurcharge(Surcharge surcharge) {

        if (getSurchargeByName(surcharge.getName()) != null) {
            return null;
        }

//        return surchargeRepositoryImpl.save(surcharge);
        return doInTransaction(entityManager -> surchargeRepositoryImpl.save(entityManager, surcharge));
    }

    public void deleteSurcharge(Long surchargeId) {
//        surchargeRepositoryImpl.deleteById(surchargeId);
        doInTransactionVoid(entityManager -> surchargeRepositoryImpl.deleteById(entityManager, surchargeId));
    }

    public List<Surcharge> getAllSurcharges() {
//        return surchargeRepositoryImpl.findAll();
        return doInTransaction(surchargeRepositoryImpl::findAll);
    }

    public Surcharge updateSurcharge(Surcharge surcharge) {
//        return surchargeRepositoryImpl.update(surcharge);
        return doInTransaction(entityManager -> surchargeRepositoryImpl.update(entityManager, surcharge));
    }

    public List<Surcharge> getSurchargeByKeyword(String keyword) {
//        return surchargeRepositoryImpl.findBySurchargeNameOrId(keyword);
        return doInTransaction(entityManager -> surchargeRepositoryImpl.findBySurchargeNameOrId(entityManager, keyword));
    }

    public Surcharge getSurchargeByName(String name) {
//        return surchargeRepositoryImpl.findBySurchargeName(name);
        return doInTransaction(entityManager -> surchargeRepositoryImpl.findBySurchargeName(entityManager, name));
    }


    public Surcharge getSurchargeById(Long surchargeId) {
//        return surchargeRepositoryImpl.findById(surchargeId);
        return doInTransaction(entityManager -> surchargeRepositoryImpl.findById(entityManager, surchargeId));
    }


//    public List<Surcharge> getSurchargeDetailsByOrderId(Long orderId) {
//    }
}
