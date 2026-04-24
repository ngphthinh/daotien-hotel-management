package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.repository.jpa.SurchargeDetailRepositoryImpl;

import java.util.List;

public class SurchargeDetailService extends Service {

    private SurchargeDetailRepositoryImpl surchargeDetailRepositoryImpl;

    public SurchargeDetailService() {
        this.surchargeDetailRepositoryImpl = new SurchargeDetailRepositoryImpl();
    }

    public SurchargeDetail save(SurchargeDetail surchargeDetail, Long orderId) {

//        boolean exists = surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId);

        boolean exists = doInTransaction(entityManager -> surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(entityManager, surchargeDetail.getSurcharge().getSurchargeId(), orderId));

        if (exists) {
            AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
                    " already exists for Order ID " + orderId);
            return null;
        }

//        return surchargeDetailRepositoryImpl.save(surchargeDetail, orderId);

        return doInTransaction(entityManager -> surchargeDetailRepositoryImpl.save(entityManager, surchargeDetail, orderId));

    }

    public List<SurchargeDetail> getSurchargeDetailsByOrderId(Long orderId) {
//        return surchargeDetailRepositoryImpl.findSurchargeDetailsByOrderId(orderId);
        return doInTransaction(entityManager -> surchargeDetailRepositoryImpl.findSurchargeDetailsByOrderId(entityManager, orderId));
    }

    public void saveWithOrderId(Long orderId, List<SurchargeDetail> surchargesToSave) {
//        for (SurchargeDetail surchargeDetail : surchargesToSave) {
//            if (!surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId)) {
//                surchargeDetailRepositoryImpl.save(surchargeDetail, orderId);
//            } else {
//                AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
//                        " already exists for Order ID " + orderId);
//            }
//        }

        doInTransactionVoid(entityManager -> {
            for (SurchargeDetail surchargeDetail : surchargesToSave) {
                if (!surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(entityManager, surchargeDetail.getSurcharge().getSurchargeId(), orderId)) {
                    surchargeDetailRepositoryImpl.save(entityManager, surchargeDetail, orderId);
                } else {
                    AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
                            " already exists for Order ID " + orderId);
                }
            }
        });
    }

    public void deleteByOrderId(Long orderId) {
//        surchargeDetailRepositoryImpl.deleteByOrderId(orderId);
        doInTransactionVoid(entityManager -> surchargeDetailRepositoryImpl.deleteByOrderId(entityManager, orderId));
    }

    public boolean saveByOrderId(Long orderId, List<SurchargeDetail> surchargeDetails) {
//        return surchargeDetailRepositoryImpl.saveByOrderId(orderId, surchargeDetails);
        return doInTransaction(entityManager -> surchargeDetailRepositoryImpl.saveByOrderId(entityManager, orderId, surchargeDetails));
    }

    public void deleteById(long surchargeId, Long orderId) {
//        surchargeDetailRepositoryImpl.deleteById(surchargeId, orderId);
        doInTransactionVoid(entityManager -> surchargeDetailRepositoryImpl.deleteById(entityManager, surchargeId, orderId));
    }

    public void deleteById(Long orderId) {
//        surchargeDetailRepositoryImpl.deleteById(orderId);
        doInTransactionVoid(entityManager -> surchargeDetailRepositoryImpl.deleteById(entityManager, orderId));
    }

    public void updateSurchargeDetail(Long surchargeId, int quantity, Long orderId) {
//        surchargeDetailRepositoryImpl.updateSurchargeDetail(surchargeId, quantity, orderId);
        doInTransactionVoid(entityManager -> surchargeDetailRepositoryImpl.updateSurchargeDetail(entityManager, surchargeId, quantity, orderId));

    }

}
