package br.edu.ifpb.webFramework.http;

import java.util.function.BiConsumer;

public class Route {
    private String path;
    private RequestMethod method;
    private BiConsumer<Request, Response> handler;
    private Request request;
    private Response response;

    public Route(String path, RequestMethod method, BiConsumer<Request, Response> handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
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
}
