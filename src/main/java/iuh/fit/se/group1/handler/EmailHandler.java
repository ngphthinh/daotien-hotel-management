package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.EmailRequest;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.EmailSenderService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailHandler implements RequestHandler {

    private final EmailSenderService emailSenderService;


    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        if (commandType.equals(CommandType.EMAIL_SEND)) {
            try {
                emailSenderService.sendHtmlMail((EmailRequest) request.getRequest());
                return Response.builder()
                        .code(200)
                        .message("Email sent successfully")
                        .build();
            } catch (Exception e) {
                return Response.builder()
                        .code(400)
                        .message("Failed to send email: " + e.getMessage())
                        .build();
            }
        } else {
            return Response.builder()
                    .code(400)
                    .message("Unsupported command type: " + commandType)
                    .build();
        }

    }
}
