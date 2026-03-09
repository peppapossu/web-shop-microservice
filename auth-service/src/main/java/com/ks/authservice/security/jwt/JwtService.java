package com.ks.authservice.security.jwt;


import com.ks.authservice.entity.appUser.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final RsaKeyProperties rsaKeys;
    private final JwtProperties jwtProperties;

    private final static String ROLES = "roles";
    private final static String TYPE = "type";
    private final static String REFRESH_TOKEN = "refresh_token";


    public String generateAccessToken(AppUser user) {

        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .toList();


        return Jwts.builder()
                .subject(user.getUsername())
                .claim(ROLES,roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(rsaKeys.getPrivateKey())
                .compact();
    }

    public String generateRefreshToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .claim(TYPE, REFRESH_TOKEN)
                .signWith(rsaKeys.getPrivateKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(rsaKeys.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
