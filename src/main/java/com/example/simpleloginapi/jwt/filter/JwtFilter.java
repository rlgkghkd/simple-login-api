package com.example.simpleloginapi.jwt.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.simpleloginapi.common.exception.ErrorDetailDto;
import com.example.simpleloginapi.common.exception.ErrorResponseDto;
import com.example.simpleloginapi.common.exception.Errors;
import com.example.simpleloginapi.jwt.exceptions.JwtError;
import com.example.simpleloginapi.jwt.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = jwtUtil.getTokenFromHeader(request);
		if (token != null){
			try {
				jwtUtil.validateToken(token);

				Claims claims = jwtUtil.getUserInfoFromToken(token);
				String username = claims.getSubject();
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 이미 인증 되었으므로 credentials 는 null
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} catch (SecurityException | MalformedJwtException e) {
				log.error("JWT 서명 오류");
				filterErrorResponse(response, JwtError.BAD_SIGNATURE);
				return;
			} catch (ExpiredJwtException e) {
				log.error("만료된 토큰");
				filterErrorResponse(response, JwtError.TOKEN_EXPIRED);
				return;
			} catch (UnsupportedJwtException e) {
				log.error("지원하지 않는 토큰");
				filterErrorResponse(response, JwtError.UNSUPPORTED_JWT_TOKEN);
				return;
			} catch (IllegalArgumentException e) {
				log.error("잘못된 토큰");
				filterErrorResponse(response, JwtError.INVALID_TOKEN);
				return;
			}catch (Exception e) {
				log.error("Could not set user authentication in security context: {}", e.getMessage());
				filterErrorResponse(response, JwtError.AUTHENTICATION_FAILED);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void filterErrorResponse (HttpServletResponse response, Errors error) {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(error.getStatus());
		ErrorDetailDto errorDetailDto = ErrorDetailDto.of(error);
		try{
			String json = new ObjectMapper().writeValueAsString(new ErrorResponseDto(errorDetailDto));
			response.getWriter().write(json);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
