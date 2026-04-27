package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.AccountChangePassword;
import iuh.fit.se.group1.dto.AuthenticateRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;

import java.util.concurrent.TimeUnit;

public class AuthServiceClient implements ServiceClient {

    private final ClientSocketManager socket;

    public AuthServiceClient(ClientSocketManager socket) {
        this.socket = socket;
    }

    public Response login(String username, String password) throws Exception {
        Request req = Request.builder()
                .commandType(CommandType.AUTH_LOGIN)
                .request(new AuthenticateRequest(username, password))
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }

    public Response logout(String username) throws Exception {
        Request req = Request.builder()
                .commandType(CommandType.AUTH_LOGOUT)
                .request(username)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }


    public Response resetPassword(String username) throws Exception {
        Request req = Request.builder()
                .commandType(CommandType.AUTH_RESET_PASSWORD)
                .request(username)
                .build();

        return socket.send(req).get(30, TimeUnit.SECONDS);
    }

    public Response changePassword(String username, String oldPass, String newPass) throws Exception {

        return socket.send(Request.builder()
                .commandType(CommandType.AUTH_CHANGE_PASSWORD)
                .request(AccountChangePassword.builder()
                        .username(username)
                        .oldPassword(oldPass)
                        .newPassword(newPass)
                        .build())
                .build()).get(30, TimeUnit.SECONDS);
    }

    public Response validateManager(String username, String password) throws Exception {
        return socket.send(Request.builder()
                .commandType(CommandType.AUTH_VALIDATE_MANAGER)
                .request(new AuthenticateRequest(username, password))
                .build()).get();
    }
}
