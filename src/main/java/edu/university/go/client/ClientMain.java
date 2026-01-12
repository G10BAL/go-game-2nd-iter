package edu.university.go.client;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost", 9999);
        client.connect();

        ConsoleUI ui = new ConsoleUI(client);
        ui.start();
    }
}
