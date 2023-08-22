package com.orangedragon1.server.services.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangedragon1.server.models.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final String SECRET_KEY;
    private final Long TOKEN_EXPIRY;

    public JwtService(@Value("${spring.service.jwt-service.secret-key}") String secretKey) {
        this.SECRET_KEY = secretKey;
        this.TOKEN_EXPIRY = TimeUnit.HOURS.toMillis(2);
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(
                "authorities",
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
        User user = (User) userDetails;
        claims.put("first_name", user.getFirstName().toLowerCase());
        claims.put("last_name", user.getLastName().toLowerCase());

        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRY))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public List<String> extractAuthorities(String jwtToken) {
        Object authoritiesClaim = extractClaim(jwtToken, claims -> claims.get("authorities"));

        if (authoritiesClaim instanceof List) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(authoritiesClaim, new TypeReference<List<String>>() {});
        }

        return Collections.emptyList();
    }

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        if (null == claims) return null; // if jwtToken is invalid (expired or malformed)
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
