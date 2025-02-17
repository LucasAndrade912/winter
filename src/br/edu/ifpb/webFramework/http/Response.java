package br.edu.ifpb.webFramework.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private final HttpExchange exchange;
    private final ObjectMapper mapper = new ObjectMapper();

    public Response(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void send(int status, Object data) throws IOException {
        String responseBody = mapper.writeValueAsString(data);
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, responseBytes.length);

        // Automaticamente fecha a conexão após o envio da resposta
        try (OutputStream response = exchange.getResponseBody()) {
            response.write(responseBytes);
        }
    }
}
