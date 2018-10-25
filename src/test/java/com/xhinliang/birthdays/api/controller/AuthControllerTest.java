package com.xhinliang.birthdays.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.xhinliang.birthdays.common.dto.NormalResponse;

/**
 * @author xhinliang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * 尝试注册一个帐号
     */
    @Test
    public void testRegister() {
        AuthController.Register register = new AuthController.Register();
        register.setEmail("123@gmail.com");
        register.setNickname("123测试");
        register.setPassword("123");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AuthController.Register> entity = new HttpEntity<>(register, httpHeaders);
        ResponseEntity<NormalResponse> normalResponse = restTemplate
            .postForEntity("/api/auth/register", entity, NormalResponse.class);
        assertNotNull(normalResponse.getBody());
        assertNotNull(normalResponse.getBody().getData());
        Map<String, String> responseBody = (Map<String, String>) normalResponse.getBody().getData();
        assertNotNull(responseBody.get("token"));
        assertNotNull(responseBody.get("email"));
        assertNotNull(responseBody.get("nickname"));
        assertEquals(responseBody.get("email"), "123@gmail.com");
        assertEquals(responseBody.get("nickname"), "123测试");
    }
}
