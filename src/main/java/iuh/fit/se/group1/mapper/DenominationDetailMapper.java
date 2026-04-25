package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.DenominationDetailDTO;
import iuh.fit.se.group1.entity.DenominationDetail;

public class DenominationDetailMapper {


    private EmployeeShiftMapper employeeShiftMapper = new EmployeeShiftMapper();

    public DenominationDetailMapper() {
        employeeShiftMapper = new EmployeeShiftMapper();
    }

    public DenominationDetailDTO toDTO(DenominationDetail denominationDetail) {
        if (denominationDetail == null) {
            return null;
        }

        return DenominationDetailDTO.builder()
                .denominationDetailId(denominationDetail.getDenominationDetailId())
                .denomination(denominationDetail.getDenomination())
                .quantity(denominationDetail.getQuantity())
                .employeeShift(employeeShiftMapper.toEmployeeShiftDTO(denominationDetail.getEmployeeShift()))
                .createdAt(denominationDetail.getCreatedAt())
                .build();
    }

    public DenominationDetail toDenominationDetail(DenominationDetailDTO denominationDetail) {
        if (denominationDetail == null) {
            return null;
        }
        return DenominationDetail.builder()
                .denominationDetailId(denominationDetail.getDenominationDetailId())
                .denomination(denominationDetail.getDenomination())
                .quantity(denominationDetail.getQuantity())
                .employeeShift(employeeShiftMapper.toEmployeeShift(denominationDetail.getEmployeeShift()))
                .createdAt(denominationDetail.getCreatedAt())
                .build();
    }
}
