package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Server {

    public static void main(String[] args) {
        final int PORT = 12345;
        List<ClientHandler> clients = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciando. Aguardando conexões...");

            Properties properties = loadProperties("src/server/db.properties");
            if (properties == null) {
                System.err.println("Erro ao carregar as propriedades do banco de dados.");
                return;
            }

            Connection connection = getConnection(properties);
            if (connection == null) {
                System.err.println("Erro ao conectar ao banco de dados.");
                return;
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);
                if (autenticarCliente(connection, clientSocket)) {
                    System.out.println("Cliente autenticado com sucesso.");
                    ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                    clients.add(clientHandler);
                    clientHandler.start();
                } else {
                    System.out.println("Falha na autenticação do cliente.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro de I/O. Verifique a conexão de rede e o servidor.");
        }
    }

    private static Properties loadProperties(String filename) {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar as propriedades do arquivo.");
            return null;
        }
    }

    private static Connection getConnection(Properties properties) {
        try {
            String url = properties.getProperty("db.url");
            String usuario = properties.getProperty("db.username");
            String senha = properties.getProperty("db.password");
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao conectar ao banco de dados.");
            return null;
        }
    }

    private static boolean autenticarCliente(Connection connection, Socket clientSocket) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            output.println("Digite seu nome de usuário:");
            String username = input.readLine();
            output.println("Digite sua senha:");
            String password = input.readLine();

            // Verifica se o usuário é um administrador
            String adminSql = "SELECT * FROM Administradores WHERE username = ? AND password = ?";
            PreparedStatement adminStatement = connection.prepareStatement(adminSql);
            adminStatement.setString(1, username);
            adminStatement.setString(2, password);
            ResultSet adminResultSet = adminStatement.executeQuery();
            boolean isAdmin = adminResultSet.next();

            if (isAdmin) {
                output.println("Autenticação bem-sucedida como administrador.");
                System.out.println("Autenticação bem-sucedida para o administrador: " + username);
                return true;
            }

            // Verifique as credenciais do usuário na tabela de usuários
            String userSql = "SELECT * FROM Usuarios WHERE username = ? AND password = ?";
            PreparedStatement userStatement = connection.prepareStatement(userSql);
            userStatement.setString(1, username);
            userStatement.setString(2, password);
            ResultSet userResultSet = userStatement.executeQuery();
            boolean isAuthenticated = userResultSet.next();

            // Envia mensagem de autenticação ao cliente
            if (isAuthenticated) {
                output.println("Autenticação bem-sucedida.");
                System.out.println("Autenticação bem-sucedida para o usuário: " + username);
            } else {
                output.println("Autenticação falhou. Verifique seu nome de usuário e senha.");
                System.out.println("Falha na autenticação para o usuário: " + username);
            }

            return isAuthenticated;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.err.println("Erro de comunicação com o cliente.");
            return false;
        }
    }
}
