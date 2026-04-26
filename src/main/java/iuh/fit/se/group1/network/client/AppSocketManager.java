package iuh.fit.se.group1.network.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppSocketManager {

    private static final Logger log = LoggerFactory.getLogger(AppSocketManager.class);

    private static ClientSocketManager socket;
    private static volatile boolean initialized = false;

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 3637;

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    private AppSocketManager() {
    }

    // ================= INIT =================
    public static synchronized boolean initialize() {
        return initialize(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static synchronized boolean initialize(String host, int port) {
        if (initialized && socket != null && socket.isConnected()) {
            log.warn("Socket already initialized");
            return true;
        }

        if (socket == null) {
            socket = ClientSocketManager.getInstance();
        }

        int retry = 0;

        while (retry < MAX_RETRIES) {
            try {
                log.info("Connecting to {}:{}", host, port);

                if (socket.connect(host, port)) {
                    initialized = true;
                    log.info("✓ Connected");
                    return true;
                }

                retry++;
                sleep();

            } catch (Exception e) {
                log.error("Connect error: {}", e.getMessage());
                retry++;
                sleep();
            }
        }

        log.error("✗ Cannot connect after {} retries", MAX_RETRIES);
        return false;
    }

    // ================= RECONNECT =================
    public static synchronized boolean reconnect() {
        log.info("Reconnecting...");
        shutdown();
        return initialize();
    }

    // ================= SHUTDOWN =================
    public static synchronized void shutdown() {
        if (socket != null) {
            socket.disconnect();
        }
        initialized = false;
        log.info("Socket closed");
    }

    // ================= STATUS =================
    public static boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public static boolean isInitialized() {
        return initialized && isConnected();
    }

    public static String getStatus() {
        if (!initialized || socket == null) {
            return "NOT_INITIALIZED";
        }
        return socket.isConnected() ? "CONNECTED" : "DISCONNECTED";
    }

    // ================= GET SOCKET =================
    public static ClientSocketManager getSocket() {
        if (!isInitialized()) {
            throw new IllegalStateException("Socket not initialized");
        }
        return socket;
    }

    // ================= UTIL =================
    private static void sleep() {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}