
// ChatClient.java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP = "localhost"; // Server address
    private static final int SERVER_PORT = 12345; // Server port number

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket(SERVER_IP, SERVER_PORT); // Connect to server
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Read messages from server
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true); // Send messages to server
            Scanner scanner = new Scanner(System.in);

            // Create a separate thread for receiving messages
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) { // Read messages from server
                        System.out.println("Server: " + message);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });

            receiveThread.start(); // Start thread to listen for messages

            // Main thread for sending messages
            while (true) {
                String userMessage = scanner.nextLine(); // Read user input
                output.println(userMessage); // Send message to server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
