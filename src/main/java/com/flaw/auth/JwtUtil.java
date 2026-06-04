package com.flaw.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.flaw.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public String generateToken(User user){
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))

                .withClaim("firstName", user.getFirstName())
                .withClaim("lastName", user.getLastName())
                .withClaim("role", user.getRole().name())
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractEmail(String token){
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean isTokenValid(String token){
        try{
            extractEmail(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
