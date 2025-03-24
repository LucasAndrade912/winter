package br.edu.ifpb.webFramework.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private final String method;
    private final String url;
    private final byte[] body;
    private final Headers headers;
    private final Map<String, String> parameters;
    private final ObjectMapper mapper = new ObjectMapper();

    public Request(HttpExchange exchange, Map<String, String> pathParameters) throws IOException {
        this.method = exchange.getRequestMethod();
        this.url = exchange.getRequestURI().toString();
        this.body = exchange.getRequestBody().readAllBytes();
        this.headers = exchange.getRequestHeaders();
        this.parameters = this.parseQuery(exchange.getRequestURI().getQuery());
        this.parameters.putAll(pathParameters);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public byte[] getBody() {
        return body;
    }

    public <T> T getJson(Class<T> clazz) throws IOException {
        return this.mapper.readValue(getBody(), clazz);
    }

    public Headers getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getToken() {
        List<String> authorizationHeader = headers.get("Authorization");
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            String header = authorizationHeader.get(0);
            if (header.startsWith("Bearer ")) {
                return header.substring(7); // Extrai o token após "Bearer "
            }
        }
        return null;
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";
            result.put(key, value);
        }
        return result;
    }
}
