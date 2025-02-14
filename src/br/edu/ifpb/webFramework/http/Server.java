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
    private Map<String, Map<String, Route>> routes = new HashMap<>();

    public Server(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new InetSocketAddress(host, port);
        this.server = HttpServer.create(socket, 10);
    }

    public void addRoute(String path, RequestMethod method, BiConsumer<Request, Response> handler) throws IOException {
        if (this.routes.get(path) == null) {
            Map<String, Route> map = new HashMap<>();
            map.put(method.getMethod(), new Route(path, method, handler));
            this.routes.put(path, map);

            this.server.createContext(path, exchange -> {
                this.handleRequest(this.routes.get(exchange.getRequestURI().getPath()).get(exchange.getRequestMethod().toLowerCase()), exchange);
            });
        } else {
            Map<String, Route> map = this.routes.get(path);
            map.put(method.getMethod(), new Route(path, method, handler));
            this.routes.put(path, map);
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
