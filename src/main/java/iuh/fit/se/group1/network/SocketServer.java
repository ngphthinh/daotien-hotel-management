package iuh.fit.se.group1.network;

import iuh.fit.se.group1.dispatcher.Dispatcher;
import iuh.fit.se.group1.handler.ActiveUsersManager;
import iuh.fit.se.group1.util.PropertiesReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    private final int PORT = Integer.parseInt(PropertiesReader.getInstance().get("server.port"));
    private final Dispatcher dispatcher;
    public SocketServer(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void start() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("Server started at port " + PORT);


            while (true) {
                Socket socket = server.accept();

                System.out.println("Client connected: " + socket.getInetAddress());
                executor.execute(new ClientHandler(socket, dispatcher));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
