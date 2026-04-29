package iuh.fit.se.group1.network;

import iuh.fit.se.group1.dispatcher.Dispatcher;
import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.handler.ActiveUsersManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ClientHandler implements Runnable {

    private static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    private final Socket socket;
    private final Dispatcher dispatcher;

    @Getter
    private String username;

    private ObjectOutputStream out;

    @Override
    public void run() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ) {
            this.out = oos;

            while (true) {
                Request request = (Request) ois.readObject();

                Response response = dispatcher.dispatch(request);
                response.setRequestId(request.getRequestId());

                // LOGIN
                if (response.getCode() == 200
                        && request.getCommandType() == CommandType.AUTH_LOGIN) {

                    username = response.getData() != null
                            ? ((AccountDTO) response.getData()).getUsername()
                            : null;

                    if (username != null) {
                        clients.put(username, this);
                    }
                }

                sendResponse(response);
            }

        } catch (EOFException e) {
            System.out.println("Client disconnected: " + username);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (out != null) out.close();
            socket.close();
        } catch (Exception ignored) {}

        if (username != null) {
            ActiveUsersManager.getInstance().registerLogout(username);
            clients.remove(username);
        }
    }

    // ===== SEND RESPONSE (thread-safe) =====
    public synchronized void sendResponse(Response response) {
        try {
            out.writeObject(response);
            out.flush();
            out.reset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean sendToUser(String username, Response response) {
        ClientHandler client = clients.get(username);

        if (client == null) return false;

        try {
            client.sendResponse(response);
            return true;
        } catch (Exception e) {
            clients.remove(username);
            return false;
        }
    }

    // ===== BROADCAST =====
    public static void broadcast(String message, CommandType commandType) {
        Response response = Response.builder()
                .code(999)
                .message(message)
                .commandType(commandType)
                .requestId(null)
                .build();

        for (var entry : clients.entrySet()) {
            try {
                entry.getValue().sendResponse(response);
            } catch (Exception e) {
                clients.remove(entry.getKey());
            }
        }
    }
}