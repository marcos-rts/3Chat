package client;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 12345;

        try {
            // Conecta ao servidor
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado ao servidor.");

            // Cria os fluxos de entrada e saída
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Lê a mensagem de boas-vindas do servidor
            String welcomeMessage = input.readLine();
            System.out.println("Servidor: " + welcomeMessage);

            // Solicita ao usuário que digite o nome de usuário e a senha
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome de usuário: ");
            String username = userInput.readLine();
            output.println(username);
            System.out.print("Digite sua senha: ");
            String password = userInput.readLine();
            output.println(password);

            // Aguarda a resposta do servidor sobre a autenticação
            String authenticationResult = input.readLine();
            System.out.println("Servidor: " + authenticationResult);

            // Fecha a conexão
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
