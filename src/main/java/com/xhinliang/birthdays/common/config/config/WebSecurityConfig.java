package com.xhinliang.birthdays.common.config.config;

import com.xhinliang.birthdays.common.config.filter.CorsFilter;
import com.xhinliang.birthdays.common.config.filter.VerifyTokenFilter;
import com.xhinliang.birthdays.common.config.security.JwtAuthenticationEntryPoint;
import com.xhinliang.birthdays.common.config.security.JwtTokenGenerator;
import com.xhinliang.birthdays.common.config.security.JwtUserDetailsService;
import com.xhinliang.birthdays.common.db.constant.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author xhinliang
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final VerifyTokenFilter verifyTokenFilter;

    private final CorsFilter corsFilter;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    public WebSecurityConfig(JwtUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, VerifyTokenFilter verifyTokenFilter, CorsFilter corsFilter, JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.verifyTokenFilter = verifyTokenFilter;
        this.corsFilter = corsFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        // Filters will not get executed for the resources
        web.ignoring() //
            .antMatchers("/", //
                "/resources/**", //
                "/static/**", //
                "/public/**", //
                "/webui/**", //
                "/h2-console/**", //
                "/configuration/**", //
                "/swagger-ui/**", //
                //"/api/out/**", //
                "/swagger-resources/**", //
                "/api-docs", //
                "/api-docs/**", //
                "/v2/api-docs/**", //
                "/*.html", //
                "/**/*.html", //
                "/**/*.css", //
                "/**/*.js", //
                "/**/*.png", //
                "/**/*.jpg", //
                "/**/*.gif", //
                "/**/*.svg", //
                "/**/*.ico", //
                "/**/*.ttf", //
                "/**/*.woff", //
                "/**/*.otf");
    }

    // If Security is not working
    // check application.properties if it is set to ignore
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http //
            // Disable Cross site references
            .csrf().disable() //
            // login filter
            .addFilterBefore(new JwtTokenGenerator("/session", authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
            // and unauthorizedHandler
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() //
            // don't create session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            // 给未登录用户设置 ROLE_ANONYMOUS 的角色
            .anonymous().authorities(UserRole.ROLE_ANONYMOUS.name()).and() //
            // 对于所有已验证的请求（ROLE_ANONYMOUS 也算哦）
            .authorizeRequests()
            // 允许这个路径下的所有访问
            .antMatchers("/api/auth/**").permitAll() //
            // 剩下的所有路径，都需要验证登录
            .anyRequest().authenticated(); //

        http //
            // Add CORS Filter
            .addFilterBefore(corsFilter, ChannelProcessingFilter.class) //
            // Custom Token based authentication based on the header previously given to the client
            .addFilterBefore(verifyTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * If You want to store encoded password in your databases and authenticate user
     * based on encoded password then uncomment the below method and provide an encoder
     **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService) //
            .passwordEncoder(passwordEncoder);
    }


}
