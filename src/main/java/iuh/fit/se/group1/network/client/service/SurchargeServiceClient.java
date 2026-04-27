package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.SurchargeDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;

import java.io.IOException;

public class SurchargeServiceClient implements ServiceClient{
    private final ClientSocketManager clientSocketManager;

     public SurchargeServiceClient(ClientSocketManager clientSocketManager) {
        this.clientSocketManager = clientSocketManager;
    }

     public Response getAllSurcharges() throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_GET_ALL)
                .build()).get();
    }

     public Response getSurchargeById(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_GET_BY_ID)
                .request(id)
                .build()).get();
    }

     public Response createSurcharge(SurchargeDTO surchargeDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_CREATE)
                .request(surchargeDTO)
                .build()).get();
    }

     public Response updateSurcharge(SurchargeDTO surchargeDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_UPDATE)
                .request(surchargeDTO)
                .build()).get();
    }

     public Response deleteSurcharge(Long id) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_DELETE)
                .request(id)
                .build()).get();
    }
    public Response getSurchargeByKeyword(String keyword) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.SURCHARGE_GET_BY_KEYWORDS)
                .request(keyword)
                .build()).get();
    }
    public Response getSurchargeByName(String name) throws Exception {
         return clientSocketManager.send(Request.builder()
                 .commandType(CommandType.SURCHARGE_GET_BY_NAME)
                 .request(name)
                 .build()).get();
    }
}
