// ChatServer.java
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345; // Port number for the server
    private static Set<PrintWriter> clients = new HashSet<>(); // Stores connected clients

    public static void main(String[] args) {
        System.out.println("Chat Server started...");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT); // Create server socket
            while (true) {
                new ClientHandler(server.accept()).start(); // Accept and handle new client connection
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close(); // Close server socket
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket; // Socket for the connected client
        private PrintWriter output; // Output stream to send messages to client

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                output = new PrintWriter(clientSocket.getOutputStream(), true); // Send output to client
                synchronized (clients) {
                    clients.add(output); // Add client to the list of active clients
                }
                String message;
                while ((message = input.readLine()) != null) { // Read messages from client
                    System.out.println("Received: " + message);
                    synchronized (clients) {
                        for (PrintWriter writer : clients) {
                            writer.println(message); // Broadcast message to all clients
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                synchronized (clients) {
                    clients.remove(output); // Remove client from active clients list
                }
                try {
                    clientSocket.close(); // Close client socket
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
