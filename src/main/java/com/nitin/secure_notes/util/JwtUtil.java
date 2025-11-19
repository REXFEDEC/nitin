package com.nitin.secure_notes.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "my-secret-key-my-secret-key-my-secret-key"; // 32+ chars
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    // Secret key
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // usually email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    // Extract username (subject) from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if token is valid
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        Date expiry = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiry.before(new Date());
    }
}
