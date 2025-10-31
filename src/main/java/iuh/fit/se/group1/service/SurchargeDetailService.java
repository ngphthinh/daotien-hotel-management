package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.repository.SurchargeDetailRepository;
import iuh.fit.se.group1.repository.SurchargeRepository;

import java.util.Arrays;
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
}
