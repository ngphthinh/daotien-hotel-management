package iuh.fit.se.group1.network.client.service;

import iuh.fit.se.group1.dto.EmailRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;

public class EmailServiceClient implements ServiceClient {

    private final ClientSocketManager socket;

    public EmailServiceClient(ClientSocketManager socket) {
        this.socket = socket;
    }

    public Response sendEmail(EmailRequest emailRequest) throws Exception {
        Request request = Request.builder()
                .request(emailRequest)
                .commandType(CommandType.EMAIL_SEND)
                .build();

        return socket.send(request).get();
    }

}
