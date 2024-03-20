package adminserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminServer {

    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 12345;

        try {
            // Solicita ao usuário que digite o nome de usuário e a senha
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome de usuário: ");
            String username = userInput.readLine();
            System.out.print("Digite sua senha: ");
            String password = userInput.readLine();

            // Conecta ao servidor
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado ao servidor.");

            // Cria os fluxos de entrada e saída
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Envia o nome de usuário e a senha para o servidor
            output.println(username);
            output.println(password);

            // Aguarda e exibe a resposta do servidor
            String response = input.readLine();
            System.out.println("Resposta do servidor: " + response);

            // Fecha a conexão
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
