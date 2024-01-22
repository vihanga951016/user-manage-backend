package com.manage.userbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final String secretKey = "asdasndnasoduihuibdiubuibduiashdfh9oshidbiucnos89foahsdohjbdiIUISUDBIBASBiubasiudh8hhuih9d8hauhdiudhjijbasidbaisbdu9ahsdbaisbdiabsubcasbiuasbdishd9u";

    private static JwtTokenProvider instance;

    public static JwtTokenProvider getInstance() {
        if (instance == null) {
            instance = new JwtTokenProvider();
        }
        return instance;
    }

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    public String createToken(String username, Map<String, Object> claimMap, long validityInMillis) {

        Claims claims = Jwts.claims().setSubject(username);
        logger.info("Token generating for subject >> " + username + " | validity millis " + validityInMillis);
        if (claimMap != null) {
            for (String key : claimMap.keySet()) {
                claims.put(key, claimMap.get(key));
            }
        }

        return getToken(username, validityInMillis, claims);
    }

    private String getToken(String username, long validityInMillis, Claims claims) {

        Date validity = new Date( validityInMillis);
        logger.info("Token validate until " + validity);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("lms")
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(getEncodedKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.info("Token generated for username >> " + username);

        return token;
    }

    public Claims getClaims(String jwt) throws ExpiredJwtException {

        logger.info("subject >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " +jwt);

        Claims cl = Jwts.parserBuilder()
                .setSigningKey(getEncodedKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return cl;
    }

    private Key getEncodedKey() {
        String keyBytes = Encoders.BASE64.encode(secretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBytes.getBytes());
    }
}
