package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.SurchargeDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class SurchargeDetailHandler implements RequestHandler {
    public final static Logger log = LoggerFactory.getLogger(SurchargeDetailHandler.class);
    private final SurchargeDetailService surchargeDetailService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType) {
                case SURCHARGE_DETAIL_CREATE -> handleCreate(request);
                case SURCHARGE_DETAIL_CREATE_LIST -> handleSaveWithOrderId(request);
                case SURCHARGE_DETAIL_GET_BY_ORDER -> handleGetByOrderId(request);
                case SURCHARGE_DETAIL_UPDATE -> handleUpdateFromOrder(request);
                case SURCHARGE_DETAIL_DELETE -> handleDeleteByOrderId(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling surcharge detail request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdateFromOrder(Request request) {

        try {
            SurchargeDetailUpdateRequest updateRequest = (SurchargeDetailUpdateRequest) request.getRequest();
            surchargeDetailService.updateSurchargeDetail(updateRequest.getSurchargeId(), updateRequest.getQuantity(), updateRequest.getOrderId());

            Response response = Response.builder()
                    .code(200)
                    .message("Surcharge details updated successfully for Order ID " + updateRequest.getOrderId())
                    .build();



            return response;
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleSaveWithOrderId(Request request) {

        try {
            SurchargeDetailsCreateRequest surchargeDetails = (SurchargeDetailsCreateRequest) request.getRequest();
            if (surchargeDetails.getSurcharges() == null || surchargeDetails.getSurcharges().isEmpty()) {
                return Response.builder()
                        .code(400)
                        .message("Surcharge details list cannot be null or empty")
                        .build();
            }
            long orderId = surchargeDetails.getOrderId();
            surchargeDetailService.saveWithOrderId(orderId, surchargeDetails.getSurcharges());

            Response response = Response.builder()
                    .code(200)
                    .message("Surcharge details saved successfully for Order ID " + orderId)
                    .build();


            return response;
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetByOrderId(Request request) {
        try {
            List<SurchargeDetailDTO> surchargeDetails = surchargeDetailService.getSurchargeDetailsByOrderId((long) request.getRequest());
            if (surchargeDetails == null || surchargeDetails.isEmpty()) {
                return Response.builder()
                        .code(404)
                        .message("No surcharge details found for Order ID " + request.getRequest())
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Surcharge details retrieved successfully for Order ID " + request.getRequest())
                    .data(surchargeDetails)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving surcharge details for Order ID {}: {}", request.getRequest(), e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleDeleteByOrderId(Request request) {
        try {

            SurchargeDetailDeleteRequest deleteRequest = (SurchargeDetailDeleteRequest) request.getRequest();
            long orderId = deleteRequest.getOrderId();
            long surchargeId = deleteRequest.getSurchargeId();

            surchargeDetailService.deleteById(surchargeId, orderId);

            Response response = Response.builder()
                    .code(200)
                    .message("Surcharge details deleted successfully for Order ID " + orderId)
                    .build();


            return response;
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        try {

            SurchargeDetailCreateRequest surchargeDetailCreateRequest = (SurchargeDetailCreateRequest) request.getRequest();
            SurchargeDetailDTO surchargeDetailDTO = surchargeDetailService.save(surchargeDetailCreateRequest.getNewSurcharge(), surchargeDetailCreateRequest.getOrderId());

            Response response = Response.builder()
                    .code(200)
                    .message("Surcharge detail created successfully for Order ID " + surchargeDetailCreateRequest.getOrderId())
                    .data(surchargeDetailDTO)
                    .build();



            return response;

        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }
}