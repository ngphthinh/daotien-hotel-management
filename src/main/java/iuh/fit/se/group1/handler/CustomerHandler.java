package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.CustomerDTO;
import iuh.fit.se.group1.network.ClientHandler;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.CustomerService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CustomerHandler implements RequestHandler {

    private final CustomerService customerService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            Response response;
            switch (commandType) {
                case CUSTOMER_CREATE -> response = handleCreate(request);
                case CUSTOMER_GET_BY_ID -> response = handleGetById(request);
                case CUSTOMER_GET_ALL -> response = handleGetAll(request);
                case CUSTOMER_UPDATE -> response = handleUpdate(request);
                case CUSTOMER_DELETE -> response = handleDelete(request);
                case CUSTOMER_GET_BY_KEYWORDS -> response = handleGetByKeywords(request);
                case CUSTOMER_GET_BY_CITIZEN -> response = handleGetByCitizenId(request);

                default -> response = Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            }


            if (isWriteCommand(commandType) && response.getCode() == 200) {
                String message = getMessage(commandType);
                ClientHandler.broadcast(message, CommandType.CUSTOMER_REFRESH);
            }
            return response;
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private String getMessage(CommandType commandType) {
        return switch (commandType) {
            case CUSTOMER_CREATE -> "Customer created";
            case CUSTOMER_UPDATE -> "Customer updated";
            case CUSTOMER_DELETE -> "Customer deleted";
            default -> "Customer has been changed";
        };
    }

    private boolean isWriteCommand(CommandType commandType) {
        return switch (commandType) {
            case CUSTOMER_CREATE, CUSTOMER_UPDATE, CUSTOMER_DELETE -> true;
            default -> false;
        };
    }

    private Response handleGetByCitizenId(Request request) {
        String citizenId = request.getRequest().toString();

        return Response.builder()
                .code(200)
                .message("Customer with cizienId " + citizenId)
                .data(customerService.getCustomerByCitizenId(citizenId))
                .build();
    }

    private Response handleGetByKeywords(Request request) {
        String keyword = request.getRequest().toString();
        return Response.builder()
                .code(200)
                .message("Customer with keywords " + keyword)
                .data(customerService.getCustomerByKeyword(keyword))
                .build();
    }

    private Response handleDelete(Request request) {
        Long customerId = (Long) request.getRequest();

        customerService.deleteCustomer(customerId);

        return Response.builder()
                .code(200)
                .message("Customer with ID " + customerId + " deleted")
                .build();
    }

    private Response handleUpdate(Request request) {
        CustomerDTO customerDTO = (CustomerDTO) request.getRequest();
        CustomerDTO updated = customerService.updateCustomer(customerDTO);

        Response response = Response.builder()
                .code(200)
                .message("Customer with ID " + customerDTO.getCustomerId() + " updated")
                .data(updated)
                .build();


        return response;
    }

    private Response handleGetAll(Request request) {
        return Response.builder()
                .code(200)
                .message("Get all customers successfully")
                .data(customerService.getAllCustomer())
                .build();
    }

    private Response handleGetById(Request request) {
        Long customerId = (Long) request.getRequest();
        return Response.builder()
                .code(200)
                .message("Customer with ID " + customerId + " found")
                .data(customerService.getCustomerById(customerId))

                .build();
    }

    private Response handleCreate(Request request) {
        CustomerDTO customerDTO = (CustomerDTO) request.getRequest();
        CustomerDTO created = customerService.createCustomer(customerDTO);

        Response response = Response.builder()
                .code(200)
                .message("Customer created successfully")
                .data(created)
                .build();

        return response;
    }
}
