package com.example.zozo.web.util;

import com.example.zozo.web.model.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.SignatureException;
import java.util.Date;

@Service
public class JwtTokenUtil {

    private static final String SECRET_KEY = "em96byN3ZWIjc2VjcmV0I2tleSNhdXRob3IjY2FpI2x1byNhZGRyZXNzI21pbHBpdGFzI2NhI3ppcCM5NTAzNSNjb3VudHJ5I3Vz";

    public static String generateToken(String email) {
        long expirationTime = 1000 * 60 * 60;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Method to verify and parse JWT token
    public static Claims verifyToken(String token) throws Exception {
        try {
            // Parse the token to extract claims (payload)
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY) // Use the secret key to verify signature
                    .parseClaimsJws(token)      // Parse the token
                    .getBody();

            return claims;  // Return the token claims (payload)
        } catch (ExpiredJwtException e) {
            throw new Exception("Token expired");
        } catch (Exception e) {
            throw new Exception("Invalid token");
        }
    }

    public static String getEmailFromToken(String token) throws Exception {
        Claims claims = verifyToken(token);
        return claims.getSubject();
    }

    public static boolean isTokenExpired(String token) throws Exception {
        Claims claims = verifyToken(token);
        return claims.getExpiration().before(new Date());
    }
}
