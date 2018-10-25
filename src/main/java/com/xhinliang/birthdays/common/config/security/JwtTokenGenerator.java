package com.xhinliang.birthdays.common.config.security;

import com.xhinliang.birthdays.common.config.security.model.JwtSessionItem;
import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.framework.component.util.JsonMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xhinliang
 */
@Slf4j
public class JwtTokenGenerator extends AbstractAuthenticationProcessingFilter {

    private JwtTokenHelper tokenHelper = new JwtTokenHelper();

    public JwtTokenGenerator(String urlMapping, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(urlMapping));
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {
        try {
            String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject userJSON = new JSONObject(jsonString);
            String username = userJSON.getString("username");
            String password = userJSON.getString("password");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, password);
            // This will take to successfulAuthentication or failureAuthentication function
            return getAuthenticationManager().authenticate(authToken);
        } catch (JSONException | AuthenticationException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authToken) throws IOException {
        SecurityContextHolder //
            .getContext() //
            .setAuthentication(authToken);
        JwtTokenUser tokenUser = (JwtTokenUser) authToken.getPrincipal();

        String tokenString = tokenHelper.createTokenForUser(tokenUser);
        JwtSessionItem sessionItem = new JwtSessionItem(tokenUser.getInternalUser(), tokenString);
        NormalResponse<JwtSessionItem> response = NormalResponse.ofData(sessionItem);

        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(JsonMapperUtils.toJSON(response));
        res.getWriter().flush();
        res.getWriter().close();
        // DON'T call supper as it continue the filter chain
        // super.successfulAuthentication(req, res, chain, authResult);
    }
}
