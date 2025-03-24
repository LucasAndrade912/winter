package br.edu.ifpb.winter.exceptions;

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
