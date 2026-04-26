package iuh.fit.se.group1.network.client;

import iuh.fit.se.group1.network.Response;

/**
 * Callback interface for handling async responses from server
 */
@FunctionalInterface
public interface ResponseHandler {
    /**
     * Called when response is received from server
     */
    void onResponseReceived(Response response);
}

