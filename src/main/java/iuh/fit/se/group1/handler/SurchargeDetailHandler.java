package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.SurchargeDetailDTO;
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
    public final static Logger log= LoggerFactory.getLogger(SurchargeDetailHandler.class);
    private final SurchargeDetailService surchargeDetailService;
    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType){
                case SURCHARGE_DETAIL_SAVE-> handleCreate(request);
                case SURCHARGE_DETAIL_DELETE_BY_ORDER_ID -> handleDeleteByOrderId(request);
                case SURCHARGE_DETAIL_GET_BY_ORDER_ID -> handleGetByOrderId(request);
                case SURCHARGE_DETAIL_SAVE_WITH_ORDER_ID -> handleSaveWithOrderId(request);
                case SURCHARGE_DETAIL_UPDATE_WITH_ORDER_ID -> handleUpdateFromOrder(request);
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
        long surchargeDetailId = (long) request.getRequest();
        List<SurchargeDetailDTO> surchargeDetails = surchargeDetailService.getSurchargeDetailsByOrderId(surchargeDetailId);
        if (surchargeDetails == null || surchargeDetails.isEmpty()) {
            return Response.builder()
                    .code(400)
                    .message("Surcharge details list cannot be null or empty")
                    .build();
        }
        long orderId = surchargeDetails.get(0).getOrder().getOrderId();
        int quantity = surchargeDetails.get(0).getQuantity();
        try{
            surchargeDetailService.updateSurchargeDetail(surchargeDetailId,quantity,orderId);
            return Response.builder()
                    .code(200)
                    .message("Surcharge details updated successfully for Order ID " + orderId)
                    .build();
        }
        catch (Exception e){
            log.error("Error updating surcharge details for Order ID {}: {}", orderId, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleSaveWithOrderId(Request request) {
        List<SurchargeDetailDTO> surchargeDetails = (List<SurchargeDetailDTO>) request.getRequest();
        if (surchargeDetails == null || surchargeDetails.isEmpty()) {
            return Response.builder()
                    .code(400)
                    .message("Surcharge details list cannot be null or empty")
                    .build();
        }
        long orderId = surchargeDetails.get(0).getOrder().getOrderId();
        try{
            surchargeDetailService.saveWithOrderId(orderId, surchargeDetails);
            return Response.builder()
                    .code(200)
                    .message("Surcharge details saved successfully for Order ID " + orderId)
                    .build();
        }
        catch (Exception e){
            log.error("Error saving surcharge details for Order ID {}: {}", orderId, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetByOrderId(Request request) {
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
    }

    private Response handleDeleteByOrderId(Request request) {
        long orderId = (long) request.getRequest();
        try{

        surchargeDetailService.deleteByOrderId(orderId);
        return Response.builder()
                .code(200)
                .message("Surcharge details deleted successfully for Order ID " + orderId)
                .build();
        }
        catch (Exception e){
            log.error("Error deleting surcharge details for Order ID {}: {}", orderId, e.getMessage(), e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        SurchargeDetailDTO surchargeDetailDTO = (SurchargeDetailDTO) request.getRequest();
        if (surchargeDetailDTO == null) {
            return Response.builder()
                    .code(400)
                    .message("SurchargeDetail data cannot be null")
                    .build();
        }
        try{
            SurchargeDetailDTO savedSurchargeDetail = surchargeDetailService.save(surchargeDetailDTO, surchargeDetailDTO.getOrder().getOrderId());
            if (savedSurchargeDetail == null) {
                return Response.builder()
                        .code(409)
                        .message("SurchargeDetail with Surcharge ID " + surchargeDetailDTO.getSurcharge().getSurchargeId() +
                                " already exists for Order ID " + surchargeDetailDTO.getOrder().getOrderId())
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("SurchargeDetail saved successfully")
                    .data(savedSurchargeDetail)
                    .build();
        }
        catch (Exception e){
            log.error("Error saving surcharge detail: {}", surchargeDetailDTO, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }
}
