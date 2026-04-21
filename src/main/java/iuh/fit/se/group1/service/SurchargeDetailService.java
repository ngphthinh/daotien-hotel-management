package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.repository.jpa.SurchargeDetailRepositoryImpl;

import java.util.List;

public class SurchargeDetailService {

    private SurchargeDetailRepositoryImpl surchargeDetailRepositoryImpl;

    public SurchargeDetailService() {
        this.surchargeDetailRepositoryImpl = new SurchargeDetailRepositoryImpl();
    }

    public SurchargeDetail save(SurchargeDetail surchargeDetail, Long orderId) {

        boolean exists = surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId);

        if (exists) {
            AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
                    " already exists for Order ID " + orderId);
            return null;
        }

        return surchargeDetailRepositoryImpl.save(surchargeDetail, orderId);

    }

    public List<SurchargeDetail> getSurchargeDetailsByOrderId(Long orderId) {
        return surchargeDetailRepositoryImpl.findSurchargeDetailsByOrderId(orderId);
    }

    public void saveWithOrderId(Long orderId, List<SurchargeDetail> surchargesToSave) {
        for (SurchargeDetail surchargeDetail : surchargesToSave) {
            if (!surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId)) {
                surchargeDetailRepositoryImpl.save(surchargeDetail, orderId);
            } else {
                AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
                        " already exists for Order ID " + orderId);
            }
        }

    }

    public void deleteByOrderId(Long orderId) {
        surchargeDetailRepositoryImpl.deleteByOrderId(orderId);
    }

    public boolean saveByOrderId(Long orderId, List<SurchargeDetail> surchargeDetails) {
        return surchargeDetailRepositoryImpl.saveByOrderId(orderId, surchargeDetails);
    }

    public void deleteById(long surchargeId, Long orderId) {
        surchargeDetailRepositoryImpl.deleteById(surchargeId, orderId);
    }

    public void deleteById(Long orderId) {
        surchargeDetailRepositoryImpl.deleteById(orderId);
    }

    public void updateSurchargeDetail(Long surchargeId, int quantity, Long orderId) {
        surchargeDetailRepositoryImpl.updateSurchargeDetail(surchargeId, quantity, orderId);

    }

}
