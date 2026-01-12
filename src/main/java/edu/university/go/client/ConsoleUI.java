package edu.university.go.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

public class ConsoleUI {

    private final Client client;
    private final BufferedReader console =
            new BufferedReader(new InputStreamReader(System.in));
    private final AtomicReference<String> assignedColor = new AtomicReference<>();

    public ConsoleUI(Client client) {
        this.client = client;
    }

    public void start() throws IOException {
        System.out.println("Connected to Go server.");
    System.out.println("Commands: MOVE x y | QUIT");

        // Start a background reader thread to print any server messages
        Thread reader = new Thread(() -> {
            try {
                String resp;
                while ((resp = client.receive()) != null) {
                    System.out.println(resp);
                    if (resp.startsWith("COLOR ")) {
                        assignedColor.set(resp.substring(6).trim());
                    }
                }
            } catch (IOException e) {
                // connection closed or error - exit thread
            }
        });
        reader.setDaemon(true);
        reader.start();

        while (true) {
            System.out.print("> ");
            String input = console.readLine();

            if (input == null || input.equalsIgnoreCase("QUIT")) {
                break;
            }

            // Auto-append assigned color if user typed 'MOVE x y' without color
            if (input.startsWith("MOVE")) {
                String[] toks = input.split(" ");
                if (toks.length == 3) {
                    String col = assignedColor.get();
                    if (col != null) {
                        input = input + " " + col;
                    }
                }
            }

            client.send(input);
        }

        client.close();
    }
}
