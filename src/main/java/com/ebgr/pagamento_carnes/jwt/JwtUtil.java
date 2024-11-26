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