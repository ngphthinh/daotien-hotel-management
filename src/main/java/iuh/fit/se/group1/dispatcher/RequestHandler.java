package iuh.fit.se.group1.dispatcher;

import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;

public interface RequestHandler {
    Response handle(Request request);
}