package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.CustomerDTO;
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
            return switch (commandType) {
                case CUSTOMER_CREATE -> handleCreate(request);
                case CUSTOMER_GET_BY_ID -> handleGetById(request);
                case CUSTOMER_GET_ALL -> handleGetAll(request);
                case CUSTOMER_UPDATE -> handleUpdate(request);
                case CUSTOMER_DELETE -> handleDelete(request);
                case CUSTOMER_GET_BY_KEYWORDS -> handleGetByKeywords(request);
                case CUSTOMER_GET_BY_CITIZEN -> handleGetByCitizenId(request);

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
        Response response = Response.builder()
                .code(200)
                .message("Customer with ID " + customerId + " deleted")
                .build();



        return response;
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
