package br.edu.ifpb.webFramework.http;

import br.edu.ifpb.webFramework.exceptions.ConnectionAlreadyExists;
import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.DDLHandler;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;
import br.edu.ifpb.webFramework.utils.ClassDataExtract;
import br.edu.ifpb.webFramework.utils.ClassDataExtracted;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class Server {
    private final String host;
    private final int port;
    private final HttpServer server;
    private final Map<String, Map<String, Route>> routes = new HashMap<>();

    public Server(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        InetSocketAddress socket = new InetSocketAddress(host, port);
        this.server = HttpServer.create(socket, 10);
    }

    public void addRoute(String path, RequestMethod method, BiConsumer<Request, Response> handler) {
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

    public void initDatabase(String ip, Integer port, String dbName, String user, String password, List<Class<?>> entities) throws ConnectionAlreadyExists {
        Connection.connect(ip, port, dbName, user, password);

        entities.forEach(entity -> {
            try {
                ClassDataExtracted extracted = ClassDataExtract.extract(entity);

                if (!extracted.getEntity()) {
                    throw new Exception("Entity not found");
                }

                DDLHandler.createTable(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Database initialized");
    }

    public void start() {
        System.out.println("Server starting in " + this.host + ":" + this.port);
        this.server.start();
    }

    public void stop() {
        Connection.disconnect();
        this.server.stop(0);
    }
}
