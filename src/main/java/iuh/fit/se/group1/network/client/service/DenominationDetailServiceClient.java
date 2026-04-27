package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.DenominationDetailDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DenominationDetailServiceClient implements ServiceClient {
    private final ClientSocketManager socket;

    public Response getAvailableDenominations() throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.DEMOMINATION_AVAILABLE)

                .build()).get();
    }

    public Response saveAll(List<DenominationDetailDTO> details) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.DENOMINATION_DETAIL_SAVE_ALL)
                .request(details)
                .build()).get();
    }
}
