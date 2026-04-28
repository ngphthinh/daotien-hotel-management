package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class SurchargeDetailServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;


    public Response getSurchargeDetailsByOrderId(Long orderId) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_DETAIL_GET_BY_ORDER)
                .request(orderId)
                .build()).get();
    }

    public Response save(SurchargeDetailDTO newSurcharge, Long orderId) throws Exception {
        SurchargeDetailCreateRequest surchargeDetailCreateRequest = SurchargeDetailCreateRequest.builder()
                .orderId(orderId)
                .newSurcharge(newSurcharge)
                .build();

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_DETAIL_CREATE)
                .request(surchargeDetailCreateRequest)
                .build()).get();
    }

    public Response updateSurchargeDetail(Long surchargeId, int quantity, Long orderId) throws Exception {
        SurchargeDetailUpdateRequest surchargeDetailUpdateRequest = SurchargeDetailUpdateRequest.builder()
                .quantity(quantity)
                .orderId(orderId)
                .surchargeId(surchargeId)
                .build();

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_DETAIL_UPDATE)
                .request(surchargeDetailUpdateRequest)
                .build()).get();
    }

    public Response deleteById(Long surchargeId, Long orderId) throws Exception {
        SurchargeDetailDeleteRequest surchargeDetailDeleteRequest = SurchargeDetailDeleteRequest.builder()
                .surchargeId(surchargeId)
                .orderId(orderId)
                .build();

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_DETAIL_DELETE)
                .request(surchargeDetailDeleteRequest)
                .build()).get();
    }

    public Response saveWithOrderId(Long orderId, List<SurchargeDetailDTO> surchargesToSave) throws Exception {
        SurchargeDetailsCreateRequest surchargeDetailCreateRequest = SurchargeDetailsCreateRequest.builder()
                .orderId(orderId)
                .surcharges(surchargesToSave)
                .build();

        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_DETAIL_CREATE_LIST)
                .request(surchargeDetailCreateRequest)
                .build()).get();

    }
}