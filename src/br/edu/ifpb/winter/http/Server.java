package br.edu.ifpb.winter.http;

import br.edu.ifpb.winter.auth.AuthEndpoints;
import br.edu.ifpb.winter.auth.User;
import br.edu.ifpb.winter.exceptions.ConnectionAlreadyExists;
import br.edu.ifpb.winter.persistence.Connection;
import br.edu.ifpb.winter.persistence.DDLHandler;
import br.edu.ifpb.winter.utils.ClassDataExtract;
import br.edu.ifpb.winter.utils.ClassDataExtracted;
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

        this.server.createContext("/", exchange -> {
            String requestPath = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod().toLowerCase();

            Route matchedRoute = findMatchingRoute(requestPath, requestMethod);
            if (matchedRoute != null) {
                this.handleRequest(matchedRoute, exchange);
            } else {
                Response response = new Response(exchange);
                response.send(404, Map.of("error", "Route not found"));
            }
        });
    }

    public void addRoute(String path, RequestMethod method, BiConsumer<Request, Response> handler) {
        RouteIterator iterator = getRouteIterator();

        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (route.getPath().equals(path) && route.getMethod().equals(method)) {
                throw new IllegalArgumentException("Route already exists: " + method + " " + path);
            }
        }

        Map<String, Route> methodMap = this.routes.computeIfAbsent(path, k -> new HashMap<>());
        methodMap.put(method.getMethod(), new Route(path, method, handler));
    }

    private Route findMatchingRoute(String requestPath, String requestMethod) {
        RouteIterator iterator = getRouteIterator();

        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (matchesPath(route.getPath(), requestPath) && route.getMethod().getMethod().equalsIgnoreCase(requestMethod)) {
                return route;
            }
        }
        return null;
    }

    // Verifica se a rota corresponde ao caminho (ex.: /users/{id} -> /users/123)
    private boolean matchesPath(String routePath, String requestPath) {
        String regex = routePath.replaceAll("\\{[^/]+}", "[^/]+");
        return requestPath.matches(regex);
    }

    public void handleRequest(Route route, HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        Map<String, String> pathParams = route.extractPathParameters(requestPath);

        Request request = new Request(exchange, pathParams);
        Response response = new Response(exchange);

        route.setRequest(request);
        route.setResponse(response);

        route.getHandler().accept(request, response);
    }

    public void initDatabase(String ip, Integer port, String dbName, String user, String password, List<Class<?>> entities) throws ConnectionAlreadyExists {
        Connection.connect(ip, port, dbName, user, password);

        entities.add(0, User.class);

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

    public RouteIterator getRouteIterator() {
        return new RouteCollectionIterator(this.routes);
    }

    public void start() {
        System.out.println("Server starting in " + this.host + ":" + this.port);
        AuthEndpoints.addAuthEndpoints(this);
        this.server.start();
    }

    public void stop() {
        Connection.disconnect();
        this.server.stop(0);
    }
}
