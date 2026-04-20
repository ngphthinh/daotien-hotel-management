package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.repository.SurchargeRepositoryImpl;

import java.util.List;

public class SurchargeService {
    private final SurchargeRepositoryImpl surchargeRepositoryImpl;

    public SurchargeService() {
        this.surchargeRepositoryImpl = new SurchargeRepositoryImpl();
    }

    public Surcharge createSurcharge(Surcharge surcharge) {

        if (getSurchargeByName(surcharge.getName()) != null) {
            return null;
        }

        return surchargeRepositoryImpl.save(surcharge);
    }

    public void deleteSurcharge(Long surchargeId) {
        surchargeRepositoryImpl.deleteById(surchargeId);
    }

    public List<Surcharge> getAllSurcharges() {
        return surchargeRepositoryImpl.findAll();
    }

    public Surcharge updateSurcharge(Surcharge surcharge) {
        return surchargeRepositoryImpl.update(surcharge);
    }

    public List<Surcharge> getSurchargeByKeyword(String keyword) {
        return surchargeRepositoryImpl.findBySurchargeNameOrId(keyword);
    }

    public Surcharge getSurchargeByName(String name) {
        return surchargeRepositoryImpl.findBySurchargeName(name);
    }


    public Surcharge getSurchargeById(Long surchargeId) {
        return surchargeRepositoryImpl.findById(surchargeId);
    }



//    public List<Surcharge> getSurchargeDetailsByOrderId(Long orderId) {
//    }
}
