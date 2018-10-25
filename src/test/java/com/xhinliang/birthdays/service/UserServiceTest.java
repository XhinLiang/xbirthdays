package com.xhinliang.birthdays.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.birthdays.common.service.impl.UserServiceImpl;
import com.xhinliang.framework.component.util.DateTimeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void test() {
        UserModel user1 = userService.getUser(1L);
        assertNotNull(user1);

        userService.updateBirthday(1L, BirthdayType.LUNAR, System.currentTimeMillis());
        user1 = userService.getUser(1L);
        assertNotNull(user1);
        System.out.println(user1.getBirthdayTypeEnum());
        System.out.println(user1.getBirthTime());
        System.out.println(DateTimeUtils.getLunarStr(user1.getBirthTime()));

        user1 = userService.getUser(2L);
        assertNotNull(user1);
        System.out.println(user1.getBirthdayTypeEnum());
        System.out.println(user1.getBirthTime());
        System.out.println(DateTimeUtils.getLunarStr(user1.getBirthTime()));

        user1 = userService.getUser(3L);
        assertNotNull(user1);
        System.out.println(user1.getBirthdayTypeEnum());
        System.out.println(user1.getBirthTime());
        System.out.println(DateTimeUtils.getLunarStr(user1.getBirthTime()));
    }
}
