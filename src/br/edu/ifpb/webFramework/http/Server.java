package br.edu.ifpb.webFramework.http;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Server {
    private String host;
    private int port;
    private InetSocketAddress socket;
    private HttpServer server;
    private Map<String, Route> routes = new HashMap<>();
    private Map<String, HttpContext> contexts = new HashMap<>();

    public Server(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new InetSocketAddress(host, port);
        this.server = HttpServer.create(socket, 10);
    }

    public void addRoute(String path, RequestMethod method, BiConsumer<Request, Response> handler) throws IOException {
        this.routes.computeIfAbsent(path + method.getMethod(), key -> new Route(path, method, handler));
        if (this.contexts.containsKey(path)) {
            this.handleRequest(this.routes.get(path + method.getMethod()), this.contexts.get(path));
        } else {
            HttpContext context = this.server.createContext(path, exchange -> {
                this.handleRequest(this.routes.get(path + method.getMethod()), exchange);
            });
            this.contexts.put(path, context);
        }
    }

    public void handleRequest(Route route, HttpExchange exchange) throws IOException {
        Request request = new Request(exchange);
        Response response = new Response(exchange);

        route.setRequest(request);
        route.setResponse(response);

        route.getHandler().accept(request, response);
    }

    public void start() throws IOException {
        System.out.println("Server starting in " + this.host + ":" + this.port);
        this.server.start();
    }

    public void stop() throws IOException {
        this.server.stop(0);
    }
}
