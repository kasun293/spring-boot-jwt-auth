package com.example.spring_jwt_tutorial.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    public String generateToken(String userName, UserDetails userDetails, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .claims(claims)
                .expiration(new Date(new Date().getTime() + jwtExpirationDate))
                .signWith(key())
                .issuer("JWT")
                .claim("roles", roles)
                .claim("permissions", userDetails.getAuthorities())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        return !claims.getExpiration().before(new Date());
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
    }
}
