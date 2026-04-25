package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.dto.SurchargeDetailDTO;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.mapper.SurchargeDetailMapper;
import iuh.fit.se.group1.mapper.SurchargeMapper;
import iuh.fit.se.group1.repository.jpa.SurchargeDetailRepositoryImpl;

import java.util.List;

public class SurchargeDetailService extends Service {

    private final SurchargeDetailRepositoryImpl surchargeDetailRepositoryImpl;
    private final SurchargeDetailMapper surchargeDetailMapper;

    public SurchargeDetailService() {
        this.surchargeDetailRepositoryImpl = new SurchargeDetailRepositoryImpl();
        this.surchargeDetailMapper = new SurchargeDetailMapper();
    }

    public SurchargeDetailDTO save(SurchargeDetailDTO surchargeDetailDTO, Long orderId) {

//        boolean exists = surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId);

        boolean exists = doInTransaction(entityManager -> surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(entityManager, surchargeDetailDTO.getSurcharge().getSurchargeId(), orderId));

        if (exists) {
            AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetailDTO.getSurcharge().getSurchargeId() +
                    " already exists for Order ID " + orderId);
            return null;
        }

//        return surchargeDetailRepositoryImpl.save(surchargeDetail, orderId);

        SurchargeDetail surchargeDetail = surchargeDetailMapper.toSurchargeDetail(surchargeDetailDTO);

        return doInTransaction(entityManager -> surchargeDetailMapper.toDTO(surchargeDetailRepositoryImpl.save(entityManager, surchargeDetail, orderId)));

    }

    public List<SurchargeDetailDTO> getSurchargeDetailsByOrderId(Long orderId) {
//        return surchargeDetailRepositoryImpl.findSurchargeDetailsByOrderId(orderId);
        return doInTransaction(entityManager -> surchargeDetailRepositoryImpl.findSurchargeDetailsByOrderId(entityManager, orderId)).stream().map(surchargeDetailMapper::toDTO)
                .toList();
    }

    public void saveWithOrderId(Long orderId, List<SurchargeDetailDTO> surchargesToSaveDtos) {
//        for (SurchargeDetail surchargeDetail : surchargesToSave) {
//            if (!surchargeDetailRepositoryImpl.existsBySurchargeIdAndOrderId(surchargeDetail.getSurcharge().getSurchargeId(), orderId)) {
//                surchargeDetailRepositoryImpl.save(surchargeDetail, orderId);
//            } else {
//                AppLogger.info("SurchargeDetail with Surcharge ID " + surchargeDetail.getSurcharge().getSurchargeId() +
//                        " already exists for Order ID " + orderId);
//            }
//        }
        List<SurchargeDetail> surchargesToSave = surchargesToSaveDtos.stream().map(surchargeDetailMapper::toSurchargeDetail).toList();

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

    public boolean saveByOrderId(Long orderId, List<SurchargeDetailDTO> surchargeDetailsDtos) {
        List<SurchargeDetail> surchargeDetails = surchargeDetailsDtos.stream().map(surchargeDetailMapper::toSurchargeDetail).toList();
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
