package com.xhinliang.birthdays.api.controller;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.birthdays.service.TestLoginService;


/**
 * @author xhinliang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestLoginService testLoginService;

    private HttpHeaders authorizationHttpHeaders;

    @Before
    public void login() {
        this.authorizationHttpHeaders = testLoginService.getLoginHttpHeaders(restTemplate, "xhinliang1@gmail.com", "0");
    }

    /**
     * 尝试获取账户信息
     */
    @Test
    public void testUserProfile() {
        HttpEntity entity = new HttpEntity(this.authorizationHttpHeaders);
        ResponseEntity<NormalResponse> response = restTemplate.exchange("/api/xuser/profile", HttpMethod.GET, entity, NormalResponse.class);
        //noinspection unchecked
        Map<String, ?> data = (Map<String, ?>) response.getBody().getData();
        assertEquals(data.get("email"), "xhinliang1@gmail.com");
        assertEquals(data.get("userId"), 1);
        assertEquals(data.get("nickname"), "xhinliang1");
    }

    /**
     * 尝试更新自己的生日，并获取更新后的结果
     */
    @Test
    public void testUpdateBirthday() {
        UserController.UpdateBirthdayRequest request = new UserController.UpdateBirthdayRequest();
        request.setBirthdayType(BirthdayType.LUNAR.getValue());
        request.setBirthTime(1525591507387L);
        HttpEntity<UserController.UpdateBirthdayRequest> entity = new
            HttpEntity<>(request, authorizationHttpHeaders);
        ResponseEntity<NormalResponse> response = restTemplate.postForEntity("/api/xuser/updateBirthday",
            entity, NormalResponse.class);
        Assert.assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getMessage());

        HttpEntity entity1 = new HttpEntity(this.authorizationHttpHeaders);
        ResponseEntity<NormalResponse> responseEntity = restTemplate.exchange("/api/xuser/profile", HttpMethod.GET, entity1, NormalResponse.class);

        Assert.assertNotNull(responseEntity.getBody());
        //noinspection unchecked
        Map<String, ?> data = (Map<String, ?>) responseEntity.getBody().getData();
        assertEquals(data.get("email"), "xhinliang1@gmail.com");
        assertEquals(data.get("userId"), 1);
        assertEquals(data.get("nickname"), "xhinliang1");
        assertEquals(data.get("birthdayString"), "二零一八年三月廿一");
    }
}
