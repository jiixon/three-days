package com.itsu.threedays.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretKey = "key";

    public String createToken(Authentication authentication){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400);
        Claims claims = Jwts.claims().setSubject(String.valueOf(authentication.getPrincipal()));
        claims.put("role",authentication.getAuthorities());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }
    //토큰넘버, 만료인증시간(24시간정도),,

}
