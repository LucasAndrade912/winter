package br.edu.ifpb.webFramework.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class Request {
    private String method;
    private String url;
    private byte[] body;
    private Headers headers;

    public Request(HttpExchange exchange) throws IOException {
        this.method = exchange.getRequestMethod();
        this.url = exchange.getRequestURI().toString();
        this.body = exchange.getRequestBody().readAllBytes();
        this.headers = exchange.getRequestHeaders();
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

    public Headers getHeaders() {
        return headers;
    }
}
