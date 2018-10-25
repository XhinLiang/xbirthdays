package com.xhinliang.birthdays.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xhinliang.birthdays.common.service.impl.RelationshipServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelationshipServiceTest {

    @Autowired
    private RelationshipServiceImpl relationshipService;

    @Test
    public void test() {
        relationshipService.follow(1, 2);
        relationshipService.follow(1, 3);
        relationshipService.follow(1, 4);
        relationshipService.follow(1, 5);
        List<Long> friends = relationshipService.listFollowing(1, 10, 0).getItems();
        Assert.assertTrue(friends.size() > 0);

        relationshipService.unfollow(1, 2);
        Assert.assertTrue(relationshipService.listFollowing(1, 10, 0).getItems().size() > 0);
    }
}
