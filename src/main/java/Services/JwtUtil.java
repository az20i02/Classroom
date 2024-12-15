package Services;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import tables.User;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static String SECRET = "your_jwt_secret_key_here";
    private static long EXPIRATION = 86400000;

    public static void setSecret(String secret) {
        SECRET=secret;
    }
    public static void setExpiration(long expiration) {
        EXPIRATION=expiration;
    }

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }
        String token = header.substring(7);
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        return Long.valueOf(claims.getBody().getSubject());
    }

    public static String extractUserRole(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }
        String token = header.substring(7);
        Claims body = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return body.get("role", String.class);
    }
}