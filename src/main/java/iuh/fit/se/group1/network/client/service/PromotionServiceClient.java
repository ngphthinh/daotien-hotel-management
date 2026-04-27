package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromotionServiceClient implements ServiceClient{
    private final ClientSocketManager clientSocketManager;
    public Response getAllPromotions() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PROMOTION_GET_ALL)
                .build()).get();
    }
    public Response getPromotionById(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PROMOTION_GET_BY_ID)
                .request(id)
                .build()).get();
    }
    public Response createPromotion(Object promotionDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PROMOTION_CREATE)
                .request(promotionDTO)
                .build()).get();
    }
    public Response updatePromotion(Object promotionDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PROMOTION_UPDATE)
                .request(promotionDTO)
                .build()).get();
    }
    public Response deletePromotion(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PROMOTION_DELETE)
                .request(id)
                .build()).get();
    }
    public Response getPromotionByKeyword(String keyword) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PROMOTION_GET_BY_KEYWORDS)
                .request(keyword)
                .build()).get();
    }
    public Response getActivePromotion(Object price) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PROMOTION_GET_ACTIVE)
                .request(price)
                .build()).get();
    }
}
