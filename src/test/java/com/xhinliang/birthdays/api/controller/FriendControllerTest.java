package com.xhinliang.birthdays.api.controller;

import static org.junit.Assert.assertEquals;

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

import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.birthdays.common.dto.PageItem;
import com.xhinliang.birthdays.service.TestLoginService;
import com.xhinliang.framework.component.util.JsonMapperUtils;


/**
 * @author xhinliang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendControllerTest {

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
     * 尝试增加十个好友
     */
    @Test
    public void testAddFriend() {
        for (int i = 2; i < 10; ++i) {
            FriendController.FriendRequest request = new FriendController.FriendRequest();
            request.setUserId(i);
            HttpEntity<FriendController.FriendRequest> entity = new HttpEntity<>(request, authorizationHttpHeaders);
            ResponseEntity<NormalResponse> responseEntity = restTemplate.postForEntity("/api/friend/add", entity, NormalResponse.class);
            Assert.assertNotNull(responseEntity.getBody());
            assertEquals("OK", responseEntity.getBody().getMessage());
        }
    }

    /**
     * 尝试增加是个好友，并在好友列表中获取到
     */
    @Test
    public void testList() {
        for (int i = 2; i < 10; ++i) {
            FriendController.FriendRequest request = new FriendController.FriendRequest();
            request.setUserId(i);
            HttpEntity<FriendController.FriendRequest> entity = new HttpEntity<>(request, authorizationHttpHeaders);
            restTemplate.postForEntity("/api/friend/add", entity, NormalResponse.class);
        }

        HttpEntity entity = new HttpEntity(authorizationHttpHeaders);
        ResponseEntity<NormalResponse> responseEntity = restTemplate
            .exchange("/api/friend/list", HttpMethod.GET, entity, NormalResponse.class);

        Assert.assertNotNull(responseEntity.getBody());
        Object map = responseEntity.getBody().getData();
        PageItem<?> pageItem = JsonMapperUtils.value(map, PageItem.class);
        Assert.assertTrue(pageItem.getItems().size() > 0);
    }
}
