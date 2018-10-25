package com.xhinliang.birthdays.service;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.xhinliang.birthdays.common.dto.NormalResponse;

/**
 * 模拟登录
 */
@Service
@Lazy
public class TestLoginService {

    /**
     * 登录
     */
    public HttpHeaders getLoginHttpHeaders(TestRestTemplate restTemplate, String email, String password) {
        Map<String, String> login = ImmutableMap.of("email", email, "password", password);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(login, httpHeaders);

        ResponseEntity<NormalResponse> normalResponse = restTemplate.postForEntity("/api/auth/login",
            entity, NormalResponse.class);
        Preconditions.checkNotNull(normalResponse.getBody());

        JSONObject jsonObject = new JSONObject((Map) normalResponse.getBody().getData());
        String token;
        try {
            token = jsonObject.getString("token");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        httpHeaders.set("Authorization", token);
        return httpHeaders;
    }
}
