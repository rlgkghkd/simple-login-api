package com.example.simpleloginapi.jwt.utils;

import com.example.simpleloginapi.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    @Value("${jwt.expiration.time}") // Token 만료 시간
    private long tokenValidityTime;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String username, UserRole role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityTime);

        return Jwts.builder()
			.setSubject(username)
			.claim(AUTHORIZATION_KEY, role)
			.signWith(key, signatureAlgorithm)
			.setIssuedAt(now)
			.setExpiration(validity)
			.compact();
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)){
			return bearerToken.substring(BEARER_PREFIX.length());
		}
        return null;
    }

    public boolean validateToken(String token) {
        try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
        } catch (SecurityException | MalformedJwtException e) {
			log.error("JWT 서명 오류");
        } catch (ExpiredJwtException e) {
			log.error("만료된 토큰");
		} catch (UnsupportedJwtException e) {
			log.error("지원하지 않는 토큰");
		} catch (IllegalArgumentException e) {
			log.error("잘못된 토큰");
		}
        return false;
    }

    // 6. 토큰에서 사용자 정보 가져오기
    // TODO: Implement this method
    public Claims getUserInfoFromToken(String token) {
        // 여기에 토큰에서 Claims(사용자 정보)를 추출하여 반환하는 코드를 작성하세요.
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
