/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package server;
import java.io.*;
import java.net.*;
/**
 *
 * @author admin_hachiman
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
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