package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Properties;

public class Server {

    public static void main(String[] args) {
        final int PORT = 12345;

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
                    new ClientHandler(clientSocket).start();
                } else {
                    System.out.println("Falha na autenticação do cliente.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties(String filename) {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
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
            return null;
        }
    }

    private static boolean autenticarCliente(Connection connection, Socket clientSocket) {
        try {
            // Crie os fluxos de entrada e saída para comunicação com o cliente
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Solicite ao cliente as credenciais de login
            output.println("Digite seu nome de usuário:");
            String username = input.readLine();
            output.println("Digite sua senha:");
            String password = input.readLine();

            // Verifique as credenciais do usuário no banco de dados
            String sql = "SELECT * FROM Usuarios WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            
            // Se houver um resultado na consulta, o usuário foi autenticado com sucesso
            return resultSet.next();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
