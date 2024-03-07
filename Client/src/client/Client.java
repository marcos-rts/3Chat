package client;

import java.io.*;
import java.net.*;

/**
 * Esta classe implementa um cliente TCP simples em Java.
 * Ela se conecta a um servidor na mesma máquina (localhost) na porta 12345.
 * O cliente envia uma mensagem de teste para o servidor e imprime a resposta recebida.
 * 
 * @author Marcos Alexandre R. T. dos Santos
 */
public class Client {
    public static void main(String[] args) {
        // Definindo o endereço do servidor
        final String SERVER_ADDRESS = "localhost";
        // Definindo a porta do servidor
        final int SERVER_PORT = 12345;

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // Conexão estabelecida com o servidor
            System.out.println("Conexão estabelecida com o servidor.");

            // Enviando uma mensagem de teste para o servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Mensagem de teste do cliente.");

            // Recebendo a resposta do servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("Resposta do servidor: " + response);
        } catch (IOException e) {
            // Exceção ocorreu, imprime o rastreamento da pilha
            e.printStackTrace();
        }
    }
}

