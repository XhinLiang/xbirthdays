package com.xhinliang.birthdays.common.service;

import com.xhinliang.birthdays.common.dto.PageItem;

/**
 * @author xhinliang
 */
public interface RelationshipService {

    void follow(long userId, long friendId);

    void unfollow(long userId, long friendId);

    PageItem<Long> listFollowing(long userId, int size, int page);
}
