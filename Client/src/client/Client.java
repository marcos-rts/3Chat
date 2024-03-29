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
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Cria e inicia a thread para receber mensagens do servidor
            Thread receiverThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = input.readLine();
                        if (message != null) {
                            System.out.println("Servidor: " + message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiverThread.start();

            // Lê a mensagem de boas-vindas do servidor
            String welcomeMessage = input.readLine();
            System.out.println("Mensagem de boas-vindas recebida do servidor: " + welcomeMessage);

            // Solicita ao usuário que digite o nome de usuário
            System.out.print("Digite seu nome de usuário: ");
            String username = userInput.readLine();
            output.println(username);

            // Solicita ao usuário que digite a senha
            System.out.print("Digite sua senha: ");
            String password = userInput.readLine();
            output.println(password);

            // Aguarda a resposta do servidor sobre a autenticação
            String authenticationResult = input.readLine();
            System.out.println("Resposta de autenticação do servidor: " + authenticationResult);

            // Se a autenticação for bem-sucedida, entra no loop de envio de mensagens
            if ("Autenticação bem-sucedida.".equals(authenticationResult)) {
                System.out.println("Autenticação bem-sucedida. Você pode enviar mensagens agora.");
                System.out.println("Digite 'exit' para sair.");

                // Loop para enviar mensagens para o servidor
                while (true) {
                    System.out.print("Digite sua mensagem: ");
                    String message = userInput.readLine();
                    output.println(message);
                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                }
            } else {
                System.out.println("Falha na autenticação. Verifique seu nome de usuário e senha.");
            }

            // Fecha a conexão
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
