package br.edu.ifpb.winter.utils;

import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtils {
    // Método para criar o token JWT
    public static String createToken(String username) {
        return JWTAdapter.create(username);
    }

    // Método para validar o token JWT
    public static DecodedJWT validateToken(String token) {
        return JWTAdapter.validate(token);
    }
}
