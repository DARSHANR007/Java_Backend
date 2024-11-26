import io.jsonwebtoken.*;
import redis.clients.jedis.Jedis;

import java.util.Date;

public class AuthService {
    private final String secretKey = "mySecretKey123"; // Replace with a secure key
    private final long tokenValidity = 3600000; // 1 hour in milliseconds
    Jedis redisClient = new Jedis("localhost");

    public boolean login(String username, String password) {
        String storedPassword = redisClient.hget("user:" + username, "password");
        if (storedPassword != null && storedPassword.equals(password)) {
            return generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void register(String username, String password, String role) {
        redisClient.hset("user:" + username, "password", password);
        redisClient.hset("user:" + username, "role", role);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
