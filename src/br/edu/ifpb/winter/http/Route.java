package br.edu.ifpb.winter.http;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Route {
    private final String path;
    private final RequestMethod method;
    private final BiConsumer<Request, Response> handler;
    private Request request;
    private Response response;

    public Route(String path, RequestMethod method, BiConsumer<Request, Response> handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
    }

    public Map<String, String> extractPathParameters(String requestPath) {
        Map<String, String> pathParameters = new HashMap<>();

        String[] routeParts = this.path.split("/");     // 0 = URL, 1 = {paramName} (definido na rota)
        String[] requestParts = requestPath.split("/"); // 0 = URL, 1 = 987 (recebido da requisição)

        for (int i = 0; i < routeParts.length; i++) {
            if (routeParts[i].startsWith("{") && routeParts[i].endsWith("}")) {
                String paramName = routeParts[i].substring(1, routeParts[i].length() - 1);
                pathParameters.put(paramName, requestParts[i]);
            }
        }

        return pathParameters;
    }

    public boolean matches(String requestPath) {
        String[] routeParts = this.path.split("/");
        String[] requestParts = requestPath.split("/");

        if (routeParts.length != requestParts.length) {
            return false;
        }

        for (int i = 0; i < routeParts.length; i++) {
            if (!routeParts[i].startsWith("{") && !routeParts[i].equals(requestParts[i])) {
                return false;
            }
        }

        return true;
    }

    public String getPath() {
        return path;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public BiConsumer<Request, Response> getHandler() {
        return handler;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Route{" +
                "path='" + path + '\'' +
                ", method=" + method +
                '}';
    }
}
