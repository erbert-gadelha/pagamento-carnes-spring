package com.ebgr.pagamento_carnes.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ebgr.pagamento_carnes.controller.dto.TokenDTO;
import com.ebgr.pagamento_carnes.model.UserModel;
import java.util.Date;
import java.util.List;

public class JwtUtil {

    private static long tokenLifeTimeMilli = 3_600_000;
    private static int tokenLifeTimeSeconds = 3_600;
    private static String secret = "40028922";
    private static Algorithm algorithm = Algorithm.HMAC256(secret);



    private static String domain = "";
    public static void setDomain(String domain) {
        System.out.println("Domain defined as ("+domain+").");
        JwtUtil.domain = domain;
    }


    public static TokenDTO generateToken(UserModel userModel) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + tokenLifeTimeMilli);

        String token = JWT.create()
                .withSubject(userModel.getLogin())
                .withClaim("roles", List.of("COMMON"))//userModel.getRole())
                .withClaim("userId", userModel.getId())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new TokenDTO(
                token,
                issuedAt,
                expiresAt,
                tokenLifeTimeSeconds
        );
    }

    public static String generateCookie(TokenDTO token) {

        /*Cookie cookie = new Cookie("accessToken", yourToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Requer HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 hora
        cookie.setSameSite("None");
        response.addCookie(cookie);*/


        if(token==null)
            return "accessToken=; HttpOnly; SameSite=None; Secure; Path=/; Max-Age=0; Domain="+domain+";";
        return "accessToken="+token.value()+"; HttpOnly; SameSite=None; Secure; Path=/; Max-Age="+token.maxAge()+"; Domain="+domain+";";
    }




    public static String validateToken(String token) throws RuntimeException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}