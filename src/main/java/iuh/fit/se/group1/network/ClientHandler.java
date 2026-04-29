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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class ClientHandler implements Runnable {
    private static final List<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private final Socket socket;


    private final Dispatcher dispatcher;
    @Getter
    private String username;

    private ObjectOutputStream objectOutputStream;

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            this.objectOutputStream = out;

            clientHandlers.add(this);

            while (true) {
                Request request = (Request) in.readObject();

                Response response = dispatcher.dispatch(request);
                response.setRequestId(request.getRequestId());


                if (response.getCode() == 200 && request.getCommandType() == CommandType.AUTH_LOGIN) {
                    username = response.getData() != null ? ((AccountDTO) response.getData()).getUsername() : null;
                }

                out.writeObject(response);
                out.flush();
            }

        } catch (EOFException e) {
            System.out.println("Client disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {
            }

            if (username != null) {
                ActiveUsersManager.getInstance().registerLogout(username);
            }
            clientHandlers.remove(this);
        }
    }

    public static void broadcast(String message, CommandType commandType) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.objectOutputStream.writeObject(Response.builder()
                        .code(999)
                        .message(message)
                        .requestId(null)
                        .commandType(commandType)
                        .build());
                clientHandler.objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
