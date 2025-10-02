package com.ufersa.CodePublish.components.authentication.domain.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private Long expirationTime = 43200000L;

    private final UserDetailsService userDetailsService;

    public String generateToken(String username) {
        try{

            Algorithm algorithm = Algorithm.HMAC256(secret);

            String url = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

            String token = JWT.create()
                    .withSubject(username)
                    .withExpiresAt(generateValidity())
                    .withIssuer(url)
                    .sign(algorithm)
                    .strip();

            return token;

        } catch(JWTCreationException exception){
            throw new RuntimeException("geração de JWT falhou", exception);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
            return bearerToken;
        }
        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        try{
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch(JWTDecodeException exception){
            throw new RuntimeException("Token inválido",exception);
        }
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT jwt = JWT.decode(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Date generateValidity() {
        Date date = new Date();
        return new Date(date.getTime()+expirationTime);
    }

    public DecodedJWT decodeToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

}
