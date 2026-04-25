package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.SurchargeDTO;
import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.mapper.SurchargeMapper;
import iuh.fit.se.group1.repository.jpa.SurchargeRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class SurchargeService extends Service {
    private final SurchargeRepositoryImpl surchargeRepositoryImpl;
    private final SurchargeMapper surchargeMapper;

    public SurchargeService() {
        this.surchargeRepositoryImpl = new SurchargeRepositoryImpl();
        this.surchargeMapper = new SurchargeMapper();
    }

    public SurchargeDTO createSurcharge(SurchargeDTO surcharge) {

        if (getSurchargeByName(surcharge.getName()) != null) {
            return null;
        }

        Surcharge surchargeEntity = surchargeMapper.toSurcharge(surcharge);
        return doInTransaction(entityManager -> surchargeMapper.toSurchargeDTO(surchargeRepositoryImpl.save(entityManager, surchargeEntity)));
    }

    public void deleteSurcharge(Long surchargeId) {
//        surchargeRepositoryImpl.deleteById(surchargeId);
        doInTransactionVoid(entityManager -> surchargeRepositoryImpl.deleteById(entityManager, surchargeId));
    }

    public List<SurchargeDTO> getAllSurcharges() {
//        return surchargeRepositoryImpl.findAll();
        return doInTransaction(surchargeRepositoryImpl::findAll).stream().map(surchargeMapper::toSurchargeDTO).collect(Collectors.toList());
    }

    public SurchargeDTO updateSurcharge(SurchargeDTO surcharge) {
        Surcharge surchargeEntity = surchargeMapper.toSurcharge(surcharge);


        return doInTransaction(entityManager -> surchargeMapper.toSurchargeDTO(surchargeRepositoryImpl.update(entityManager, surchargeEntity)));
    }

    public List<SurchargeDTO> getSurchargeByKeyword(String keyword) {
        return doInTransaction(entityManager -> surchargeRepositoryImpl.findBySurchargeNameOrId(entityManager, keyword)).stream().map(surchargeMapper::toSurchargeDTO).collect(Collectors.toList());
    }

    public SurchargeDTO getSurchargeByName(String name) {
//        return surchargeRepositoryImpl.findBySurchargeName(name);
        return doInTransaction(entityManager -> surchargeMapper.toSurchargeDTO(surchargeRepositoryImpl.findBySurchargeName(entityManager, name)));
    }


    public SurchargeDTO getSurchargeById(Long surchargeId) {
        return doInTransaction(entityManager -> surchargeMapper.toSurchargeDTO(surchargeRepositoryImpl.findById(entityManager, surchargeId)));
    }

//    public List<Surcharge> getSurchargeDetailsByOrderId(Long orderId) {
//    }
}
