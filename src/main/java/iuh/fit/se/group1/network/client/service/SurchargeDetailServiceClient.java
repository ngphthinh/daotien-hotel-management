package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.SurchargeDetailDTO;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SurchargeDetailServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;
    public Response save(SurchargeDetailDTO surchargeDetailDTO, long orderId) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(iuh.fit.se.group1.network.CommandType.SURCHARGE_DETAIL_SAVE)
                .request(new Object[]{surchargeDetailDTO, orderId})
                .build()).get();
    }
    public Response getSurchargeDetailsByOrderId(long orderId) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(iuh.fit.se.group1.network.CommandType.SURCHARGE_DETAIL_GET_BY_ORDER_ID)
                .request(orderId)
                .build()).get();
    }
    public Response saveWithOrderId(long orderId, Object surchargeDetailsToSaveDtos) throws Exception {
        return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                .commandType(iuh.fit.se.group1.network.CommandType.SURCHARGE_DETAIL_SAVE_WITH_ORDER_ID)
                .request(new Object[]{orderId, surchargeDetailsToSaveDtos})
                .build()).get();
    }
        public Response updateSurchargeDetail(long surchargeDetailId, int quantity, long orderId) throws Exception {
            return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                    .commandType(iuh.fit.se.group1.network.CommandType.SURCHARGE_DETAIL_UPDATE_WITH_ORDER_ID)
                    .request(new Object[]{surchargeDetailId, quantity, orderId})
                    .build()).get();
        }
        public Response deleteByOrderId(long orderId) throws Exception {
            return clientSocketManager.send(iuh.fit.se.group1.network.Request.builder()
                    .commandType(iuh.fit.se.group1.network.CommandType.SURCHARGE_DETAIL_DELETE_BY_ORDER_ID)
                    .request(orderId)
                    .build()).get();
        }
}
