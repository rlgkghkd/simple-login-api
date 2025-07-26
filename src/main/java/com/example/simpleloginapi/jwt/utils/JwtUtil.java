package com.example.simpleloginapi.jwt.utils;

import com.example.simpleloginapi.user.entity.UserRoleAssignment;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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

    public String createToken(String username, List<UserRoleAssignment> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityTime);

		List<String> roleNames = roles.stream().map(userRoleAssignment -> userRoleAssignment.getUserRole().getAuthority()).toList();

        return Jwts.builder()
			.setSubject(username)
			.claim(AUTHORIZATION_KEY, roleNames)
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

    public void validateToken(String token) {
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    // 6. 토큰에서 사용자 정보 가져오기
    // TODO: Implement this method
    public Claims getUserInfoFromToken(String token) {
        // 여기에 토큰에서 Claims(사용자 정보)를 추출하여 반환하는 코드를 작성하세요.
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
