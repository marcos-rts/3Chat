package server;

import java.io.*;
import java.net.*;

/**
 * Esta classe implementa um servidor TCP simples em Java.
 * Ele aguarda por conexões de clientes na porta 12345 e lida com cada conexão
 * em uma nova thread usando a classe ClientHandler.
 * 
 * @author Marcos Alexandre R. T. dos Santos
 */
public class Server {

    /**
     * Método principal que inicia o servidor.
     * 
     * @param args os argumentos da linha de comando (não utilizados neste caso)
     */
    public static void main(String[] args) {
        // Porta em que o servidor vai escutar por conexões
        final int PORT = 12345;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Aguardando por conexões de clientes
            System.out.println("Servidor iniciando. Aguardando conexoes...");
            while (true) {
                // Aceita uma conexão de cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);
                // Lida com a conexão do cliente em uma nova thread
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            // Exceção ocorreu, imprime o rastreamento da pilha
            e.printStackTrace();
        }
    }
}
