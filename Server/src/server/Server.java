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
            Properties properties = loadProperties("db.properties");
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
}
