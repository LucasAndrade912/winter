package br.edu.ifpb.webFramework.persistence;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private static java.sql.Connection manager;

    public static void connect(String ip, Integer port, String dbName, String user, String password) {
        if (manager != null) return; // Verifica se a conexão já existe

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
}
