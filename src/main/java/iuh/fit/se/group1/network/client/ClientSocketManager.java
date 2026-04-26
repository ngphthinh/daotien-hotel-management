package iuh.fit.se.group1.network.client;

import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSocketManager {

    private static ClientSocketManager instance;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private volatile boolean connected;

    private final Map<String, CompletableFuture<Response>> pending = new ConcurrentHashMap<>();

    private ClientSocketManager() {
    }

    public static synchronized ClientSocketManager getInstance() {
        if (instance == null) {
            instance = new ClientSocketManager();
        }
        return instance;
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            connected = true;
            startListener();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CompletableFuture<Response> send(Request request) throws IOException {
        if (!connected) throw new IOException("Not connected");

        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        CompletableFuture<Response> future = new CompletableFuture<>();
        pending.put(requestId, future);

        out.writeObject(request);
        out.flush();

        return future;
    }

    private void startListener() {
        Thread t = new Thread(() -> {
            while (connected) {
                try {
                    Response res = (Response) in.readObject();

                    CompletableFuture<Response> f = pending.remove(res.getRequestId());
                    if (f != null) f.complete(res);

                } catch (Exception e) {
                    connected = false;
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void disconnect() {
        connected = false;

        try {
            if (out != null) out.close();
        } catch (Exception ignored) {
        }
        try {
            if (in != null) in.close();
        } catch (Exception ignored) {
        }
        try {
            if (socket != null) socket.close();
        } catch (Exception ignored) {
        }
    }

    public boolean isConnected() {
        return connected && socket != null && socket.isConnected();
    }
}