package iuh.fit.se.group1.handler;

import iuh.fit.se.group1.dispatcher.RequestHandler;
import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.network.ClientHandler;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.service.AmenityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class AmenityHandler implements RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(AmenityHandler.class);
    private final AmenityService amenityService;

    @Override
    public Response handle(Request request) {
        CommandType commandType = request.getCommandType();

        try {
            Response response = null;
            switch (commandType) {
                case AMENITY_GET_BY_ID -> response = handleGetById(request);
                case AMENITY_GET_ALL -> response = handleGetAll();
                case AMENITY_GET_BY_KEYWORDS -> response = handleGetByKeywords(request);
                case AMENITY_CREATE -> response = handleCreate(request);
                case AMENITY_UPDATE -> response = handleUpdate(request);
                case AMENITY_DELETE -> response = handleDelete(request);
                default -> response = Response.builder()
                        .code(400)
                        .message("Invalid command")
                        .build();
            }

            if (isWriteCommand(commandType) && response.getCode() == 200) {
                String message = getMessage(commandType);
                ClientHandler.broadcast(message, CommandType.AMENITY_REFRESH);
            }

            return response;
        } catch (Exception e) {
            log.error("Error handling amenity request: {}", commandType, e);
            return Response.builder()
                    .code(500)
                    .message("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    private String getMessage(CommandType commandType) {
        return switch (commandType) {
            case AMENITY_CREATE -> "Amenity created";
            case AMENITY_UPDATE -> "Amenity updated";
            case AMENITY_DELETE -> "Amenity deleted";
            default -> "Amenity has been changed";
        };
    }

    private boolean isWriteCommand(CommandType commandType) {
        return switch (commandType) {
            case AMENITY_CREATE, AMENITY_UPDATE, AMENITY_DELETE -> true;
            default -> false;
        };
    }

    private Response handleGetById(Request request) {
        Long amenityId = (Long) request.getRequest();
        if (amenityId == null) {
            return Response.builder()
                    .code(400)
                    .message("Amenity ID cannot be null")
                    .build();
        }

        AmenityDTO amenity = amenityService.getAmenityById(amenityId);
        if (amenity == null) {
            return Response.builder()
                    .code(404)
                    .message("Amenity not found with ID: " + amenityId)
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get amenity successfully")
                .data(amenity)
                .build();
    }

    private Response handleGetAll() {
        List<AmenityDTO> amenities = amenityService.getAllAmenities();

        if (amenities == null || amenities.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No amenities found")
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Get all amenities successfully")
                .data(amenities)
                .build();
    }

    private Response handleGetByKeywords(Request request) {
        String keyword = request.getRequest().toString();

        List<AmenityDTO> amenities = amenityService.getAmenityByKeyword(keyword);

        if (amenities == null || amenities.isEmpty()) {
            return Response.builder()
                    .code(200)
                    .message("No amenities found matching keyword: " + keyword)
                    .data(List.of())
                    .build();
        }

        return Response.builder()
                .code(200)
                .message("Search amenities by keyword successfully")
                .data(amenities)
                .build();
    }

    private Response handleCreate(Request request) {
        AmenityDTO amenityDTO = (AmenityDTO) request.getRequest();

        if (amenityDTO == null || amenityDTO.getNameAmenity() == null) {
            return Response.builder()
                    .code(400)
                    .message("Amenity data and name cannot be null")
                    .build();
        }

        try {
            AmenityDTO created = amenityService.createAmenity(amenityDTO);

            if (created == null) {
                return Response.builder()
                        .code(400)
                        .message("Amenity with name '" + amenityDTO.getNameAmenity() + "' already exists")
                        .build();
            }

            return Response.builder()
                    .code(200)
                    .message("Amenity created successfully")
                    .data(created)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(400)
                    .message("Error creating amenity: " + e.getMessage())
                    .build();
        }
    }

    private Response handleUpdate(Request request) {
        AmenityDTO amenityDTO = (AmenityDTO) request.getRequest();

        if (amenityDTO == null || amenityDTO.getAmenityId() == null) {
            return Response.builder()
                    .code(400)
                    .message("Amenity data and amenity ID cannot be null")
                    .build();
        }

        try {
            AmenityDTO updated = amenityService.updateAmenity(amenityDTO);
            return Response.builder()
                    .code(200)
                    .message("Amenity updated successfully")
                    .data(updated)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(400)
                    .message("Error updating amenity: " + e.getMessage())
                    .build();
        }
    }

    private Response handleDelete(Request request) {
        Long amenityId = (Long) request.getRequest();

        if (amenityId == null) {
            return Response.builder()
                    .code(400)
                    .message("Amenity ID cannot be null")
                    .build();
        }

        try {
            amenityService.deleteAmenity(amenityId);
            return Response.builder()
                    .code(200)
                    .message("Amenity deleted successfully")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error deleting amenity: " + e.getMessage())
                    .build();
        }
    }
}

