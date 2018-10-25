package com.xhinliang.birthdays.api.controller;

import com.google.common.annotations.VisibleForTesting;
import com.xhinliang.birthdays.common.config.security.JwtTokenHelper;
import com.xhinliang.birthdays.common.config.security.model.JwtSessionItem;
import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.birthdays.common.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xhinliang
 */
@RestController
@Api(tags = {"AuthRequest"})
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserServiceImpl userService;

    private final JwtTokenHelper tokenHelper;

    @Autowired
    public AuthController(UserServiceImpl userService, JwtTokenHelper tokenHelper) {
        this.userService = userService;
        this.tokenHelper = tokenHelper;
    }

    @ApiResponses(value = {@ApiResponse(code = HttpServletResponse.SC_OK, message = "Register", response = NormalResponse.class)})
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NormalResponse register(@RequestBody Register request) {
        // register
        userService.addNewUser(request.getEmail(), request.getPassword(), request.getNickname());

        // login
        JwtTokenUser tokenUser = userService.authenticate(request.getEmail(), request.getPassword());
        String tokenString = tokenHelper.createTokenForUser(tokenUser);
        JwtSessionItem sessionItem = new JwtSessionItem(tokenUser.getInternalUser(), tokenString);
        return NormalResponse.ofData(sessionItem);
    }

    @ApiResponses(value = {@ApiResponse(code = HttpServletResponse.SC_OK, message = "Login", response = NormalResponse.class)})
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NormalResponse login(@RequestBody Login loginRequest) {
        String email = loginRequest.getEmail(), password = loginRequest.getPassword();
        JwtTokenUser tokenUser = userService.authenticate(email, password);
        String tokenString = tokenHelper.createTokenForUser(tokenUser);
        JwtSessionItem sessionItem = new JwtSessionItem(tokenUser.getInternalUser(), tokenString);
        return NormalResponse.ofData(sessionItem);
    }

    @Data
    @VisibleForTesting
    static class Register {

        private String email;
        private String password;
        private String nickname;
    }

    @Data
    private static class Login {

        private String email;
        private String password;
    }
}
