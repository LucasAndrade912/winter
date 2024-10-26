package br.edu.ifpb.webFramework.exceptions;

public class ConnectionAlreadyExists extends Exception {
    public ConnectionAlreadyExists() {
        super("Connection already exists");
    }

    public ConnectionAlreadyExists(String message) {
        super(message);
    }

    public ConnectionAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
