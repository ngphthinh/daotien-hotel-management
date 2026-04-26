package iuh.fit.se.group1.dispatcher;

import iuh.fit.se.group1.network.CommandType;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {

    private final Map<CommandType, RequestHandler> handlers = new HashMap<>();

    public void register(CommandType action, RequestHandler handler) {
        handlers.put(action, handler);
    }

    public RequestHandler get(CommandType action) {
        return handlers.get(action);
    }
}