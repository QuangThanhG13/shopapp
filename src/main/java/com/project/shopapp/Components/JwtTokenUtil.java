package com.project.shopapp.Components;

import com.project.shopapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration;//lu vao 1 bien moi truong(evironment variable)

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception{
        //properties -> claims(ma hoa cac thuoc tinh)
        Map<String, Object> claims = new HashMap<>();
//        this.generateSecretKey();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            String token = Jwts.builder()
                    //dang chuan hoa user cua minh voi doi tg user sec
                    .setClaims(claims)//ham trich xuat thong tin tu token
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L ))
                    //cau hoi bao 1, 1 cai sigh key
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            //you can "inject" logger , insterad of using System.out.println
//            Logger.getLogger(JwtTokenUtil.class.getName()).severe("Cannot craete jwt token, error: " + e.getMessage());
            throw new InvalidParameterException("Cannot craete jwt token, error: " + e.getMessage());
//            return null;
        }
     }

     private Key getSigningKey() {
       byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
     }

//     private String generateSecretKey() {
//         SecureRandom random = new SecureRandom();
//         byte[] keyBytes = new byte[32];
//         random.nextBytes(keyBytes);
//         String secreKey = Encoders.BASE64.encode(keyBytes);
//         return secreKey;
//     }

     public Claims extractAllClaims(String token) {
        Claims claims;
        try {
           claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Cannot craete jwt token, error: " + e.getMessage());
            claims = null;
        }
        return claims;
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
     final Claims claims =  this.extractAllClaims(token);

      return  claimsResolver.apply(claims);
    }

    //kiem tra token da het han chua
    private Boolean isTokenExpired(String token) {
        Date exprirationDate = this.extractClaim(token, Claims::getExpiration);
        return exprirationDate.before(new Date());
    }

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
        //kiem tra xem token co hop le hay khong (con han)
    }
}
