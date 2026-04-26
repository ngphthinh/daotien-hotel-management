package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.AccountChangePassword;
import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.dto.AuthenticateRequest;
import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.AccountService;
import iuh.fit.se.group1.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class AuthenticateHandler implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateHandler.class);
    private final AuthenticateService authenticateService;
    private final AccountService accountService;
    private final ActiveUsersManager activeUsersManager = ActiveUsersManager.getInstance();

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        Response response = null;

        switch (commandType) {
            case AUTH_LOGIN -> response = handleLogin(request);
            case AUTH_LOGOUT -> response = handleLogout(request);
            case AUTH_RESET_PASSWORD -> response = handleResetPassword(request);
            case AUTH_VALIDATE_MANAGER -> response = handleValidateManager(request);
            case AUTH_CHANGE_PASSWORD -> response = handleChangePassword(request);
            default -> {
                response = Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            }
        }

        return response;
    }

    private Response handleChangePassword(Request request) {

        AccountChangePassword accountChangePassword = (AccountChangePassword) request.getRequest();

        boolean isSuccess = accountService.changePassword(accountChangePassword.getUsername(), accountChangePassword.getOldPassword(), accountChangePassword.getNewPassword());

        if (isSuccess) {
            return Response.builder()
                    .code(200)
                    .message("Change password successfully")
                    .data(accountChangePassword)
                    .build();
        }

        return Response.builder()
                .code(400)
                .message("Invalid username or password")
                .build();

    }

    private Response handleLogout(Request request) {
        String username = request.getRequest().toString();
        activeUsersManager.registerLogout(username);
        
        return Response.builder()
                .code(200)
                .message("Logout successful")
                .build();
    }

    private Response handleValidateManager(Request request) {
        AuthenticateRequest authenticateRequest = (AuthenticateRequest) request.getRequest();
        EmployeeDTO employeeDTO = authenticateService.validateManager(authenticateRequest.getUsername(), authenticateRequest.getPassword());
        if (employeeDTO == null) {
            return Response.builder()
                    .code(400)
                    .message("Invalid username or password")
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Authenticated")
                .data(employeeDTO)
                .build();

    }

    private Response handleResetPassword(Request request) {
        String username = request.getRequest().toString();
        authenticateService.resetPassword(username);

        return Response.builder()
                .code(200)
                .message("Reset Password Successful")
                .build();
    }


    private Response handleLogin(Request request) {
        AuthenticateRequest authenticateRequest = (AuthenticateRequest) request.getRequest();
        String username = authenticateRequest.getUsername();
        
        if (activeUsersManager.isUserLoggedIn(username)) {
            log.warn("Login attempt from user '{}' who is already logged in", username);
            return Response.builder()
                    .code(403)
                    .message("User already logged in from another session")
                    .build();
        }
        
        AccountDTO authenticated = authenticateService.authenticate(username, authenticateRequest.getPassword());
        
        if (authenticated != null) {
            // Register user as logged in
            if (activeUsersManager.registerLogin(username)) {
                return Response.builder()
                        .code(200)
                        .data(authenticated)
                        .message("Login successful")
                        .build();
            } else {
                // User logged in between check and registration (race condition)
                return Response.builder()
                        .code(403)
                        .message("User already logged in from another session")
                        .build();
            }
        } else {
            return Response.builder()
                    .code(401)
                    .data(null)
                    .message("Invalid username or password")
                    .build();
        }


    }
}
