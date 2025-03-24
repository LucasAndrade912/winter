package br.edu.ifpb.winter.persistence;

import br.edu.ifpb.winter.exceptions.ConnectionAlreadyExists;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private static java.sql.Connection manager;

    public static void connect(String ip, Integer port, String dbName, String user, String password) throws ConnectionAlreadyExists {
        if (manager != null)
            throw new ConnectionAlreadyExists(); // Verifica se a conexão já existe

        String url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName;

        try {
            manager = DriverManager.getConnection(url, user, password);
            System.out.println("Connecting to " + ip + ":" + port + " database: " + dbName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnect() {
        if (manager != null) {
            try {
                manager.close();
                manager = null;
                System.out.println("Successfully disconnected");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static java.sql.Connection getManager() {
        return manager;
    }
}
