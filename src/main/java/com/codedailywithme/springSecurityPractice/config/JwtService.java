package com.codedailywithme.springSecurityPractice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    private  static  final String secretKey="Ac411u0rfcxwoXyhRSDDKaRIkbGfTLRKe0ieitKYLlAri0lBUI6/ykwiaYTDvyqU";

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);

    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(
            Map<String,Object> extraClaims,UserDetails userDetails){
        return  Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*2))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final  Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return  Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername())&&! isTokenExpired(token));

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
    return extractClaim(token,Claims::getExpiration);
    }
}