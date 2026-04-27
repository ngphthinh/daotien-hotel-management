package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class AmenityServiceClient implements ServiceClient {
    private final ClientSocketManager socket;

    public Response getAllAmenities() throws Exception {
        Request request = Request.builder()
                .commandType(CommandType.AMENITY_GET_ALL)
                .build();

        return socket.send(request).get();
    }

    public Response updateAmenity(AmenityDTO build) throws Exception {
        Request request = Request.builder()
                .commandType(CommandType.AMENITY_UPDATE)
                .request(build)
                .build();

        return socket.send(request).get();

    }

    public Response deleteAmenity(Long id) throws Exception {

        return socket.send(Request.builder()
                .commandType(CommandType.AMENITY_DELETE)
                .request(id)
                .build()).get();
    }

    public Response getAmenityByKeyword(String text) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.AMENITY_GET_BY_KEYWORDS)
                .request(text)
                .build()).get();
    }

    public Response createAmenity(AmenityDTO build) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.AMENITY_CREATE)
                .request(build)
                .build()).get();
    }

//    public

}
