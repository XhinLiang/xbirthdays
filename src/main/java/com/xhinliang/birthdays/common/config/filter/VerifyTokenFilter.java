package com.xhinliang.birthdays.common.config.filter;

import com.xhinliang.birthdays.common.config.security.JwtTokenHelper;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xhinliang
 */
@Service
@Lazy
public class VerifyTokenFilter extends GenericFilterBean {

    private final JwtTokenHelper tokenHelper;

    @Autowired
    public VerifyTokenFilter(JwtTokenHelper tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            Authentication authentication = tokenHelper.verifyToken(request);
            if (authentication != null) {
                SecurityContextHolder //
                    .getContext() //
                    .setAuthentication(authentication);
            } else {
                // 这块没懂是干啥的，先留着
//                SecurityContextHolder //
//                        .getContext() //
//                        .setAuthentication(null);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } finally {
            // 这块没懂是干啥的，先留着
//            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
