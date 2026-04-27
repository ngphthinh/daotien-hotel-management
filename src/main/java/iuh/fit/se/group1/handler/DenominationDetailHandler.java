package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.DenominationDetailDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.service.AuthServiceClient;
import iuh.fit.se.group1.service.DenominationDetailService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DenominationDetailHandler implements RequestHandler {
    private final DenominationDetailService denominationDetailService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case DEMOMINATION_AVAILABLE -> handleGetAvailableDenominations(request);
                case DENOMINATION_DETAIL_SAVE_ALL -> handleSaveAll(request);

                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleSaveAll(Request request) {
        try {
            List<DenominationDetailDTO> denominationDetailDTOList = (List<DenominationDetailDTO>) request.getRequest();
            denominationDetailService.saveAll(denominationDetailDTOList);
            return Response.builder()
                    .code(200)
                    .message("Success")

                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAvailableDenominations(Request request) {
        try {
            return Response.builder()
                    .code(200)
                    .message("Success")
                    .data(denominationDetailService.getAvailableDenominations())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }


}
