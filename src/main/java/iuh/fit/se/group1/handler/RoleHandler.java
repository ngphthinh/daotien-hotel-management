package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.RoleService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleHandler implements RequestHandler {
    private final RoleService roleService;


    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        if (commandType.equals(CommandType.ROLE_GET_BY_ID)) {
            try {

                String roleId = (String) request.getRequest();
                return Response.builder()
                        .data(roleService.getRoleById(roleId))
                        .code(200)
                        .message("Role has been found")
                        .build();

            } catch (Exception e) {
                return Response.builder()
                        .code(500)
                        .message("Internal server error: " + e.getMessage())
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
