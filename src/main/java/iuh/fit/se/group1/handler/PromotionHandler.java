package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.PromotionDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class PromotionHandler implements RequestHandler {
    public static final Logger log= LoggerFactory.getLogger(PromotionHandler.class);
    public final PromotionService promotionService;

    @Override
    public Response handle(Request request) {
        CommandType commandType= request.getCommandType();
        try {
            return switch (commandType){
                case PROMOTION_GET_BY_ID -> handleGetById(request);
                case PROMOTION_GET_ALL -> handleGetAll();
                case PROMOTION_CREATE -> handleCreate(request);
                case PROMOTION_UPDATE -> handleUpdate(request);
                case PROMOTION_DELETE -> handleDelete(request);
                case PROMOTION_GET_BY_KEYWORDS -> handleGetByKeywords(request);
                case PROMOTION_GET_ACTIVE -> handleGetActive(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling promotion request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetActive(Request request) {
        BigDecimal price= (BigDecimal) request.getRequest();
        if(price==null){
            return Response.builder()
                    .code(400)
                    .message("Price cannot be null")
                    .build();
        }
        PromotionDTO promotionDTO= promotionService.getActivePromotion(price);
        if(promotionDTO==null){
            return Response.builder()
                    .code(404)
                    .message("No active promotions found for price: " + price)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Active promotions retrieved successfully")
                .data(promotionDTO)
                .build();
    }

    private Response handleGetByKeywords(Request request) {
        String keyword= (String) request.getRequest();
        if(keyword==null){
            return Response.builder()
                    .code(400)
                    .message("Keyword cannot be null or blank")
                    .build();
        }
        List<PromotionDTO> promotionDTO= promotionService.getPromotionByKeyword(keyword);
        if(promotionDTO==null){
            return Response.builder()
                    .code(404)
                    .message("No promotions found with keyword: " + keyword)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Search promotions successfully")
                .data(promotionDTO)
                .build();
    }

    private Response handleUpdate(Request request) {
        PromotionDTO promotionDTO= (PromotionDTO) request.getRequest();
        if(promotionDTO==null){
            return Response.builder()
                    .code(400)
                    .message("Promotion data cannot be null")
                    .build();
        }
        PromotionDTO updatedPromotion= promotionService.updatePromotion(promotionDTO);
        if(updatedPromotion==null){
            return Response.builder()
                    .code(404)
                    .message("Promotion not found with ID: " + promotionDTO.getPromotionId())
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Promotion updated successfully")
                .data(updatedPromotion)
                .build();
    }

    private Response handleDelete(Request request) {
        Long promotionId= (Long) request.getRequest();
        if(promotionId==null){
            return Response.builder()
                    .code(400)
                    .message("Promotion ID cannot be null")
                    .build();
        }
        try{
            promotionService.deletePromotion(promotionId);
            return Response.builder()
                    .code(200)
                    .message("Promotion deleted successfully")
                    .build();
        } catch (Exception e) {
            log.error("Error deleting promotion with ID: {}", promotionId, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }

    }

    private Response handleCreate(Request request) {
        PromotionDTO promotionDTO= (PromotionDTO) request.getRequest();
        if(promotionDTO==null) {
            return Response.builder()
                    .code(400)
                    .message("Promotion data cannot be null")
                    .build();
        }
        try {
            PromotionDTO createdPromotion = promotionService.createPromotion(promotionDTO);
            if(createdPromotion==null){
                return Response.builder()
                        .code(404)
                        .message("Failed to create promotion")
                        .build();
            }
            return Response.builder()
                    .code(200)
                    .message("Promotion created successfully")
                    .data(createdPromotion)
                    .build();
        } catch (Exception e) {
            log.error("Error creating promotion: {}", promotionDTO, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAll() {
        List<PromotionDTO> promotions= promotionService.getAllPromotions();
        if(promotions==null||promotions.isEmpty()){
            return Response.builder()
                    .code(404)
                    .message("No promotions found")
                    .build();
        }
        return Response.builder()
                .code(200)
                    .message("Get all promotions successfully")
                .data(promotions)
                .build();
    }

    private Response handleGetById(Request request) {
        Long promotionId= (Long) request.getRequest();
        if(promotionId==null){
            return Response.builder()
                    .code(400)
                    .message("Promotion ID cannot be null")
                    .build();
        }
        PromotionDTO promotionDTO= promotionService.getPromotionById(promotionId);
        if(promotionDTO==null){
            return Response.builder()
                    .code(404)
                    .message("Promotion not found with ID: " + promotionId)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get promotion by ID successfully")
                .data(promotionDTO)
                .build();
    }
}
