package server;

import java.io.*;
import java.net.*;

/**
 * Esta classe implementa o tratamento de cliente em uma thread separada.
 * Ela recebe mensagens dos clientes, imprime no console e envia uma resposta de volta.
 * 
 * @author Marcos Alexandre R. T. dos Santos
 */
public class ClientHandler extends Thread {
    private final Socket clientSocket;

    // Construtor que recebe o socket do cliente
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out;
            // Cria um BufferedReader para ler as mensagens do cliente
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                // Cria um PrintWriter para enviar mensagens de volta para o cliente
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String message;
                // Loop para ler mensagens do cliente enquanto houver comunicação
                while ((message = in.readLine()) != null) {
                    // Imprime a mensagem do cliente no console do servidor
                    System.out.println("Mensagem do cliente: " + message);
                    
                    // Envia uma mensagem de resposta de volta para o cliente
                    out.println("Mensagem recebida pelo servidor: " + message);
                }
            }
            // Fecha o PrintWriter e o socket do cliente quando a comunicação termina
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            // Exceção ocorreu, imprime o rastreamento da pilha
            e.printStackTrace();
        }
    }
}
