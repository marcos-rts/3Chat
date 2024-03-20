package server;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * Esta classe implementa o tratamento de cliente em uma thread separada.
 * Ela recebe mensagens dos clientes, imprime no console e envia uma resposta de volta.
 * 
 * @author Marcos Alexandre R. T. dos Santos
 */
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final BufferedReader input;
    private final PrintWriter output;
    private List<ClientHandler> clients;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = input.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Mensagem recebida do cliente: " + message);
                // Encaminhar a mensagem para todos os outros clientes
                for (ClientHandler client : clients) {
                    if (client != this) {
                        client.sendMessage(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }
}
