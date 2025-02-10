package br.edu.ifpb.webFramework.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private String host;
    private int port;
    private InetSocketAddress socket;
    private HttpServer server;

    public Server(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new InetSocketAddress(host, port);
        this.server = HttpServer.create(socket, 10);
    }

    public void start() throws IOException {
        System.out.println("Server starting in " + this.host + ":" + this.port);
        this.server.start();
    }

    public void stop() throws IOException {
        this.server.stop(0);
    }
}
