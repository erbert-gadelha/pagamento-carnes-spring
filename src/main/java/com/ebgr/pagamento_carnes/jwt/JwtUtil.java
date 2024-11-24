package com.ebgr.pagamento_carnes.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ebgr.pagamento_carnes.controller.dto.TokenDTO;
import com.ebgr.pagamento_carnes.model.UserModel;
import java.util.Date;

public class JwtUtil {

    private static final long tokenLifeTimeMilli = 3_600_000;
    private static final int tokenLifeTimeSeconds = 3_600;
    private static final String secret = "my-secret";



    public static TokenDTO generateToken(UserModel userModel) {

        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + tokenLifeTimeMilli);

        String token = JWT.create()
                .withSubject(userModel.getLogin())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(secret));

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
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            return null;
            //throw new RuntimeException("Invalid Token");
        }
    }
}