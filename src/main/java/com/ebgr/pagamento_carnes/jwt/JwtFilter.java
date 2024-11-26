package com.ebgr.pagamento_carnes.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class JwtFilter extends OncePerRequestFilter {


    private String findCookie(String cookieName, Cookie [] cookies) {
        if(cookies != null)
            for (Cookie cookie : cookies)
                if(cookieName.equals(cookie.getName()))
                    return cookie.getValue();
        return null;
    }

    private List<GrantedAuthority> getRoles(Claim claim) {
        if(claim == null)
            return null;
        try {
            return claim
                    .asList(String.class)
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Adiciona o prefixo "ROLE_"
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            token = authorizationHeader.substring(7);
        else
            token = findCookie("accessToken", request.getCookies());



        if(token != null) {
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);

            if (decodedJWT != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        decodedJWT.getSubject(),
                        null,
                        getRoles(decodedJWT.getClaim("roles"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
