package com.ite.pickon.security;

import com.ite.pickon.config.EnvConfig;
import com.ite.pickon.domain.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final UserService userService;

    /**
     * JwtTokenProvider 생성자
     *
     * @param userService 사용자 서비스 객체
     */
    public JwtTokenProvider(@Lazy UserService userService) {
        // Secret 키
        String secretKey = EnvConfig.getEnv("JWT_SECRET_KEY");
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
        this.userService = userService;
    }

    /**
     * 인증 정보를 기반으로 JWT 토큰 생성
     *
     * @param authentication 인증 객체
     * @param userId         사용자 ID
     * @return 생성된 JWT 토큰 객체
     */
    public JwtToken generateToken(Authentication authentication, Long userId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 36))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 리프레시 토큰을 이용해 새로운 액세스 토큰 생성
     *
     * @param refreshToken 리프레시 토큰
     * @return 새로 생성된 액세스 토큰 객체
     */
    public JwtToken generateAccessTokenFromRefreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Claims claims = parseClaims(refreshToken);
        String username = claims.getSubject();
        String authorities = claims.get("auth").toString();
        Long userId = claims.get("userId", Long.class);

        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities)
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * JWT 액세스 토큰을 기반으로 인증 객체 반환
     *
     * @param accessToken JWT 액세스 토큰
     * @return 인증 객체
     */
    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 주어진 JWT 토큰의 유효성 검사
     *
     * @param token JWT 토큰
     * @return 토큰이 유효한지 여부
     */
    public boolean validateToken(String token) {
        if (userService.isTokenBlacklisted(token)) {
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * 주어진 JWT 액세스 토큰에서 클레임 파싱
     *
     * @param accessToken JWT 액세스 토큰
     * @return 클레임 객체
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 주어진 JWT 토큰에서 사용자 ID 추출
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long getUserIdFromToken(String token) {
        String authToken = token.replace("Bearer ", "");
        Claims claims = parseClaims(authToken);
        return claims.get("userId", Long.class);
    }
}
