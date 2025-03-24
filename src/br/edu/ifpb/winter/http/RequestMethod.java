package br.edu.ifpb.winter.http;

public enum RequestMethod {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete");

    private final String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
