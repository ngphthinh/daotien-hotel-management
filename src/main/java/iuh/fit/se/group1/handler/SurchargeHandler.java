package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.SurchargeDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.SurchargeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class SurchargeHandler implements RequestHandler {
    public static final Logger log= LoggerFactory.getLogger(SurchargeHandler.class);
    private final SurchargeService surchargeService;
    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();
        try {
            return switch (commandType){
                case SURCHARGE_GET_BY_ID -> handleGetById(request);
                case SURCHARGE_GET_ALL -> handleGetAll();
                case SURCHARGE_CREATE -> handleCreate(request);
                case SURCHARGE_UPDATE -> handleUpdate(request);
                case SURCHARGE_DELETE -> handleDelete(request);
                case SURCHARGE_GET_BY_KEYWORDS -> handleGetByKeywords(request);
                case SURCHARGE_GET_BY_NAME -> handleGetByName(request);
                default -> Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            };
        } catch (Exception e) {
            log.error("Error handling surcharge request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetByName(Request request) {
        String name= (String) request.getRequest();
        if(name==null){
            return Response.builder()
                    .code(400)
                    .message("Surcharge name cannot be null or blank")
                    .build();
        }
        SurchargeDTO surcharge= surchargeService.getSurchargeByName(name);
        if(surcharge==null){
            return Response.builder()
                    .code(404)
                    .message("No surcharges found with name: " + name)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Search surcharges successfully")
                .data(surcharge)
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
        List<SurchargeDTO> surcharges= surchargeService.getSurchargeByKeyword(keyword);
        if(surcharges==null){
            return Response.builder()
                    .code(404)
                    .message("No surcharges found matching keyword: " + keyword)
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Search surcharges successfully")
                .data(surcharges)
                .build();
    }

    private Response handleDelete(Request request) {
       Long surchargeId= (Long) request.getRequest();
         if(surchargeId==null){
                return Response.builder()
                        .code(400)
                        .message("Surcharge ID cannot be null")
                        .build();
         }
        try{
            surchargeService.deleteSurcharge(surchargeId);
            return Response.builder()
                    .code(200)
                    .message("Surcharge deleted successfully")
                    .build();
        }
        catch (Exception e){
            log.error("Error deleting surcharge with ID: {}", surchargeId, e);
            return Response.builder()
                    .code(500)
                    .message("Failed to delete surcharge: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdate(Request request) {
        SurchargeDTO surchargeDTO= (SurchargeDTO) request.getRequest();
        if(surchargeDTO==null || surchargeDTO.getSurchargeId()==null){
            return Response.builder()
                    .code(400)
                    .message("Surcharge data or ID cannot be null")
                    .build();
        }
        try{
            surchargeService.updateSurcharge(surchargeDTO);
            return Response.builder()
                    .code(200)
                    .message("Surcharge updated successfully")
                    .build();
        }
        catch (Exception e){
            log.error("Error updating surcharge with ID: {}", surchargeDTO.getSurchargeId(), e);
            return Response.builder()
                    .code(500)
                    .message("Failed to update surcharge: " + e.getMessage())
                    .build();
        }
    }

    private Response handleCreate(Request request) {
        SurchargeDTO surchargeDTO= (SurchargeDTO) request.getRequest();
        if(surchargeDTO==null){
            return Response.builder()
                    .code(400)
                    .message("Surcharge data cannot be null")
                    .build();
        }
        try{
            SurchargeDTO surchargeCreated= surchargeService.createSurcharge(surchargeDTO);
            if(surchargeCreated==null){
                return Response.builder()
                        .code(500)
                        .message("Failed to create surcharge")
                        .build();
            }
            return Response.builder()
                    .code(201)
                    .message("Surcharge created successfully")
                    .data(surchargeCreated)
                    .build();
        }
        catch (Exception e){
            log.error("Error creating surcharge: {}", surchargeDTO, e);
            return Response.builder()
                    .code(500)
                    .message("Failed to create surcharge: " + e.getMessage())
                    .build();
        }
    }

    private Response handleGetAll() {
        List<SurchargeDTO> surcharges= surchargeService.getAllSurcharges();
        if(surcharges==null||surcharges.isEmpty()){
            return Response.builder()
                    .code(500)
                    .message("No surcharges found")
                    .build();
        }
        return Response.builder()
                .code(200)
                .message("Get all surcharges successfully")
                .data(surcharges)
                .build();
    }

    private Response handleGetById(Request request) {
        Long surchargeId= (Long) request.getRequest();
        if(surchargeId==null){
            return Response.builder()
                    .code(400)
                    .message("Surcharge ID cannot be null")
                    .build();
        }
        SurchargeDTO surcharge= surchargeService.getSurchargeById(surchargeId);
        if(surcharge==null){
            return Response.builder()
                    .code(404)
                    .message("Surcharge not found with ID: " + surchargeId)
                    .build();

        }
        return Response.builder()
                .code(200)
                .message("Get surcharge successfully")
                .data(surcharge)
                .build();
    }
}
