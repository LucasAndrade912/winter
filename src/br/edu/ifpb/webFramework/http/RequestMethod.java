package br.edu.ifpb.webFramework.http;

public enum RequestMethod {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete");

    private String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
