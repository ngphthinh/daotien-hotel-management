package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.repository.SurchargeRepository;

import java.util.Arrays;
import java.util.List;

public class SurchargeService {
    private final SurchargeRepository surchargeRepository;

    public SurchargeService() {
        this.surchargeRepository = new SurchargeRepository();
    }

    public Surcharge createSurcharge(Surcharge surcharge) {
        return surchargeRepository.save(surcharge);
    }

    public void deleteSurcharge(Long surchargeId) {
        surchargeRepository.deleteById(surchargeId);
    }

    public List<Surcharge> getAllSurcharges() {
        return surchargeRepository.findAll();
    }

    public Surcharge updateSurcharge(Surcharge surcharge) {
        return surchargeRepository.update(surcharge);
    }

    public List<Surcharge> getSurchargeByKeyword(String keyword) {
        return surchargeRepository.findBySurchargeNameOrId(keyword);
    }

    public Surcharge getSurchargeByName(String name) {
        return surchargeRepository.findBySurchargeName(name);
    }

    public Surcharge getSurchargeById(Long surchargeId) {
        return surchargeRepository.findById(surchargeId);
    }

//    public List<Surcharge> getSurchargeDetailsByOrderId(Long orderId) {
//    }
}
