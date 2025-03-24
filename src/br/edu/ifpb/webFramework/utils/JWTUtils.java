package br.edu.ifpb.webFramework.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JWTUtils {
    // Chave secreta para assinatura do token
    private static final String SECRET_KEY = "mySecretKey"; // Mude para uma chave secreta mais forte em produção
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora em milissegundos

    // Método para criar o token JWT
    public static String createToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // Método para validar o token JWT
    public static DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null; // Token inválido ou expirado
        }
    }
}
