package iuh.fit.se.group1.dispatcher;

import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;

public class Dispatcher {

    private final HandlerRegistry registry;

    public Dispatcher(HandlerRegistry registry) {
        this.registry = registry;
    }

    public Response dispatch(Request req) {
        RequestHandler handler = registry.get(req.getCommandType());

        System.out.println("Dispatching command: " + req.getCommandType());

        if (handler == null) {
            return Response.builder()
                    .data(null)
                    .code(400)
                    .message("Unknown action")
                    .requestId(req.getRequestId())
                    .build();
        }

        return handler.handle(req);
    }
}
