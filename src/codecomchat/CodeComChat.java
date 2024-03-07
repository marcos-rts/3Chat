package codecomchat;

import java.io.*;
import java.net.*;


/**
 * @author Marcos Alexnadre Reis Toruato dos Santos
 */
public class CodeComChat {
    public static void main(String[] args) {
        final int PORT = 12345;
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            //aguarda por conexoes de clientes
            System.out.println("Servidor iniciando. Aguardando conexoes...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);
                //lida com a conexao do cliente em uma nova thread
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
