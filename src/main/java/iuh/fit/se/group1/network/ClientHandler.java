package iuh.fit.se.group1.network;

import iuh.fit.se.group1.dispatcher.Dispatcher;
import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.handler.ActiveUsersManager;
import lombok.RequiredArgsConstructor;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientHandler implements Runnable {

    private final Socket socket;


    private final Dispatcher dispatcher;
    private String username;

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {

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
        }
    }

}
