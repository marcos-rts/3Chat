/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package client;
import java.io.*;
import java.net.*;
/**
 *
 * @author admin_hachiman
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        final String SERVER_ADDRESS = "localhost"; // Endereço do servidor
        final int SERVER_PORT = 12345; // Porta do servidor

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Conexão estabelecida com o servidor.");

            // Envia uma mensagem de teste para o servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Mensagem de teste do cliente.");

            // Recebe a resposta do servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("Resposta do servidor: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
