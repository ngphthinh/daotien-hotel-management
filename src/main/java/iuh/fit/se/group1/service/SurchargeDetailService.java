package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.repository.SurchargeDetailRepository;

import java.util.List;

public class SurchargeDetailService {

    private SurchargeDetailRepository surchargeDetailRepository;

    public SurchargeDetailService() {
        this.surchargeDetailRepository = new SurchargeDetailRepository();
    }

    public SurchargeDetail save(SurchargeDetail surchargeDetail,Long orderId) {

        boolean exists = surchargeDetailRepository.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId);

        if (exists) {
            AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
                    " already exists for Order ID " + orderId);
            return null;
        }

        return surchargeDetailRepository.save(surchargeDetail,orderId);

    }

    public List<SurchargeDetail> getSurchargeDetailsByOrderId(Long orderId) {
        return surchargeDetailRepository.findSurchargeDetailsByOrderId(orderId);
    }

    public void saveWithOrderId(Long orderId, List<SurchargeDetail> surchargesToSave) {
            for (SurchargeDetail surchargeDetail : surchargesToSave) {
                if (!surchargeDetailRepository.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId)) {
                    surchargeDetailRepository.save(surchargeDetail, orderId);
                } else {
                    AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
                            " already exists for Order ID " + orderId);
                }
            }

    }

    public void deleteByOrderId(Long orderId) {
        surchargeDetailRepository.deleteByOrderId(orderId);
    }

    public boolean saveByOrderId(Long orderId, List<SurchargeDetail> surchargeDetails) {
        return surchargeDetailRepository.saveByOrderId(orderId, surchargeDetails);
    }

    public void deleteById(long surchargeId, Long orderId) {
        surchargeDetailRepository.deleteById(surchargeId, orderId);
    }
    public void deleteById(Long orderId) {
        surchargeDetailRepository.deleteById(orderId);
    }

    public void updateSurchargeDetail(Long surchargeId, int quantity, Long orderId) {
        surchargeDetailRepository.updateSurchargeDetail(surchargeId, quantity, orderId);

    }

}
