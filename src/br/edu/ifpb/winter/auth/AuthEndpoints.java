package br.edu.ifpb.winter.auth;

import br.edu.ifpb.winter.http.Server;

public class AuthEndpoints {
    public static void addAuthEndpoints(Server server) {
        AuthFacade.login(server);
        AuthFacade.signup(server);
    }
}
