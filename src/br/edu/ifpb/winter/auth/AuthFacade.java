package br.edu.ifpb.winter.auth;

import br.edu.ifpb.winter.http.RequestMethod;
import br.edu.ifpb.winter.http.Server;
import br.edu.ifpb.winter.persistence.EntityHandler;
import br.edu.ifpb.winter.persistence.QueryHandler;
import br.edu.ifpb.winter.utils.JWTUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AuthFacade {
    public static void login(Server server) {
        server.addRoute("/login", RequestMethod.POST, (request, response) -> {
            try {
                AuthDTO data = request.getJson(AuthDTO.class);
                Map<String, String> params = Map.of("username", data.username());
                List<User> users = QueryHandler.createQuery(User.class, params);

                if (!users.isEmpty()) {
                    String userUsername = users.get(0).getUsername();
                    String userPassword = users.get(0).getPassword();

                    String password = data.password();

                    if (userUsername.equals(data.username()) && userPassword.equals(password)) {
                        String token = JWTUtils.createToken(data.username());
                        response.send(200, Map.of("token", token));
                    } else {
                        response.send(401, "Unauthorized");
                    }
                } else {
                    response.send(404, "Not found");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void signup(Server server) {
        server.addRoute("/register", RequestMethod.POST, (request, response) -> {
            try {
                AuthDTO data = request.getJson(AuthDTO.class);
                User user = new User(data.username(), data.password());
                EntityHandler.insert(user);
                response.send(201, user);
            } catch (IOException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
