package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Properties;

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
            
            // Carrega as informações de conexão do arquivo de propriedades
            Properties properties = loadProperties("src/server/db.properties");
            if (properties == null) {
                System.err.println("Erro ao carregar as propriedades do banco de dados.");
                return;
            }
            
            // Estabelece a conexão com o banco de dados
            Connection connection = getConnection(properties);
            if (connection == null) {
                System.err.println("Erro ao conectar ao banco de dados.");
                return;
            }
            
            while (true) {
                // Aceita uma conexão de cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);
                // Lida com a conexão do cliente em uma nova thread
                // Autenticação do cliente com o banco de dados
                if (autenticarCliente(connection, clientSocket)) {
                    System.out.println("Cliente autenticado com sucesso.");
                    new ClientHandler(clientSocket).start();
                } else {
                    System.out.println("Falha na autenticação do cliente.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            // Exceção ocorreu, imprime o rastreamento da pilha
            e.printStackTrace();
        }
    }
    
    /**
     * Método para carregar as informações de conexão do arquivo de propriedades.
     * 
     * @param filename o nome do arquivo de propriedades
     * @return um objeto Properties contendo as informações de conexão, ou null se ocorrer um erro
     */
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
        
    /**
     * Método para estabelecer a conexão com o banco de dados usando as informações de propriedades.
     * 
     * @param properties o objeto Properties contendo as informações de conexão
     * @return a conexão com o banco de dados, ou null se ocorrer um erro
     */
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
        
    /**
     * Método para autenticar o cliente com o banco de dados.
     * 
     * @param connection a conexão com o banco de dados
     * @param clientSocket o socket do cliente
     * @return true se a autenticação for bem-sucedida, false caso contrário
     */
    private static boolean autenticarCliente(Connection connection, Socket clientSocket) {
        try {
            // Aqui você deve implementar a lógica de autenticação do cliente
            // Utilize a conexão com o banco de dados para verificar as credenciais do cliente
            // Retorne true se a autenticação for bem-sucedida, ou false caso contrário
            return true; // Exemplo: sempre autentica com sucesso
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
