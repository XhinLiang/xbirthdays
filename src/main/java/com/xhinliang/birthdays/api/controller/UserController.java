package com.xhinliang.birthdays.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.xhinliang.birthdays.api.view.UserProfileView;
import com.xhinliang.birthdays.common.config.security.JwtTokenHelper;
import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.birthdays.common.service.impl.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author xhinliang
 */
@RestController
@RequestMapping(value = "/api/xuser", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"User"})
public class UserController {

    private final UserServiceImpl userService;

    private final JwtTokenHelper tokenHelper;

    @Autowired
    public UserController(UserServiceImpl userService, JwtTokenHelper tokenHelper) {
        this.userService = userService;
        this.tokenHelper = tokenHelper;
    }

    @ApiOperation(value = "Profile", response = NormalResponse.class)
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public NormalResponse profile() {
        long userId = tokenHelper.getCurrentUserId();
        UserModel user = userService.getUser(userId);
        Preconditions.checkNotNull(user);
        return NormalResponse.ofData(new UserProfileView(user));
    }

    @ApiOperation(value = "UpdateBirthday", response = NormalResponse.class)
    @RequestMapping(value = "/updateBirthday", method = RequestMethod.POST)
    public NormalResponse updateBirthday(@RequestBody UpdateBirthdayRequest request) {
        long userId = tokenHelper.getCurrentUserId();
        userService.updateBirthday(userId, BirthdayType.of(request.getBirthdayType()),
            request.getBirthTime());
        return NormalResponse.ofEmptyData();
    }

    @Data
    @VisibleForTesting
    static class UpdateBirthdayRequest {
        private int birthdayType;
        private long birthTime;
    }
}
