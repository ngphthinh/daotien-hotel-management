package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import iuh.fit.se.group1.service.RoleService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleServiceClient implements ServiceClient {

    private final ClientSocketManager socket;

    public Response getRoleById(String roleId) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.ROLE_GET_BY_ID)
                .request(roleId)
                .build()).get();
    }
}
