package com.xhinliang.birthdays.api.controller;

import com.google.common.annotations.VisibleForTesting;
import com.xhinliang.birthdays.api.view.UserProfileView;
import com.xhinliang.birthdays.common.config.security.JwtTokenHelper;
import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.birthdays.common.dto.PageItem;
import com.xhinliang.birthdays.common.service.impl.RelationshipServiceImpl;
import com.xhinliang.birthdays.common.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author xhinliang
 */
@RestController
@RequestMapping(value = "/api/friend", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Friend"})
public class FriendController {

    private final RelationshipServiceImpl relationshipService;

    private final JwtTokenHelper tokenHelper;

    private final UserServiceImpl userService;

    @Autowired
    public FriendController(RelationshipServiceImpl relationshipService, JwtTokenHelper tokenHelper, UserServiceImpl userService) {
        this.relationshipService = relationshipService;
        this.tokenHelper = tokenHelper;
        this.userService = userService;
    }

    @ApiOperation(value = "Add Friend", response = NormalResponse.class)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public NormalResponse follow(@RequestBody FriendRequest request) {
        long friendId = request.getUserId();
        JwtTokenUser tokenUser = tokenHelper.getCurrentTokenUser();
        relationshipService.follow(tokenUser.getInternalUser().getId(), friendId);
        return NormalResponse.ofEmptyData();
    }

    @ApiOperation(value = "Delete Friend", response = NormalResponse.class)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public NormalResponse unfollow(@RequestBody FriendRequest request) {
        long friendId = request.getUserId();
        JwtTokenUser tokenUser = tokenHelper.getCurrentTokenUser();
        relationshipService.unfollow(tokenUser.getInternalUser().getId(), friendId);
        return NormalResponse.ofEmptyData();
    }

    @ApiOperation(value = "List Friend", response = NormalResponse.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public NormalResponse getFollowingList(
        @ApiParam(value = "page") @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
        @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
        JwtTokenUser tokenUser = tokenHelper.getCurrentTokenUser();
        PageItem<Long> friendIds = relationshipService.listFollowing(tokenUser.getInternalUser().getId(), size, page);
        List<UserModel> friends = userService.getUsers(friendIds.getItems());
        List<UserProfileView> views = friends.stream().map(UserProfileView::new).collect(toList());
        return NormalResponse.ofData(PageItem.of(friendIds.getCurrentPage(), friendIds.isHasNextPage(), views));
    }

    @Data
    @VisibleForTesting
    static class FriendRequest {

        private long userId;
    }
}
