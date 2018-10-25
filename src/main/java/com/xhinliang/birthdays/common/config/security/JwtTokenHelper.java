package com.xhinliang.birthdays.common.config.security;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.config.security.model.JwtUserAuthentication;
import com.xhinliang.birthdays.common.db.model.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author xhinliang
 */
@Service
@Lazy
public class JwtTokenHelper {

    private static final long VALIDITY_TIME_MS = 2 * 60 * 60 * 1000;
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String SECRET = "xhinliang";

    public Authentication verifyToken(HttpServletRequest request) {
        // token 事实上保存在 HTTP Header 中
        String token = request.getHeader(AUTH_HEADER_NAME);

        if (StringUtils.isNotBlank(token)) {
            JwtTokenUser user = parseUserFromToken(token.replace("Bearer", "").trim());
            if (user != null) {
                return new JwtUserAuthentication(user);
            }
        }
        return null;
    }

    public JwtTokenUser getCurrentTokenUser() {
        JwtUserAuthentication jwtUserAuthentication = (JwtUserAuthentication) SecurityContextHolder.getContext() //
            .getAuthentication();

        Preconditions.checkNotNull(jwtUserAuthentication);
        return jwtUserAuthentication.getDetails();
    }

    public long getCurrentUserId() {
        JwtTokenUser tokenUser = getCurrentTokenUser();
        if (tokenUser == null) {
            return 0L;
        }
        return tokenUser.getInternalUser().getId();
    }

    /**
     * Get User Info from the Token
     */
    public JwtTokenUser parseUserFromToken(String token) {
        Claims claims = Jwts.parser() //
            .setSigningKey(SECRET) //
            .parseClaimsJws(token) //
            .getBody();

        UserModel user = new UserModel();
        user.setId(Long.parseLong((String) claims.get("userId")));
        user.setEmail((String) claims.get("email"));
        user.setRole((String) claims.get("role"));
        if (user.getEmail() != null && user.getRole() != null) {
            return new JwtTokenUser(user);
        } else {
            return null;
        }
    }

    /**
     * 给定一个 User，生成一个 Token
     */
    public String createTokenForUser(JwtTokenUser tokenUser) {
        return createTokenForUser(tokenUser.getInternalUser());
    }

    private String createTokenForUser(UserModel user) {
        return Jwts.builder() //
            .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS)) //
            .setSubject(user.getNickname()) //
            .claim("userId", String.valueOf(user.getId())) //
            .claim("email", user.getEmail()) //
            .claim("role", user.getRole()) //
            .signWith(SignatureAlgorithm.HS256, SECRET) //
            .compact();
    }
}
