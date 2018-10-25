package com.xhinliang.birthdays.api.controller;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.dto.BirthRecordDto;
import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.birthdays.common.dto.PageItem;
import com.xhinliang.birthdays.service.TestLoginService;
import com.xhinliang.framework.component.util.JsonMapperUtils;
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


/**
 * @author xhinliang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BirthRecordControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestLoginService testLoginService;

    private HttpHeaders authorizationHttpHeaders;

    /**
     * 登录
     */
    @Before
    public void login() {
        this.authorizationHttpHeaders = testLoginService.getLoginHttpHeaders(restTemplate, "xhinliang1@gmail.com", "0");
    }

    @Test
    public void testAddAndDelete() {
        // test add
        BirthRecordDto requestDto = new BirthRecordDto();
        requestDto.setEmail("testRecord@gmail.com");
        requestDto.setBirthdayType(BirthdayType.GREGORIAN.getValue());
        requestDto.setBirthTime(System.currentTimeMillis());
        requestDto.setNickname("testNickname");
        HttpEntity<BirthRecordDto> entity = new HttpEntity<>(requestDto, authorizationHttpHeaders);
        ResponseEntity<NormalResponse> responseEntity = restTemplate.exchange("/api/birthRecord/add",
            HttpMethod.POST, entity, NormalResponse.class);
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(responseEntity.getBody().getData());
        BirthRecordDto responseDto = JsonMapperUtils.value(responseEntity.getBody().getData(), BirthRecordDto.class);
        long recordId = responseDto.getId();
        responseDto.setId(requestDto.getId());
        Assert.assertEquals(requestDto, responseDto);

        // test delete
        BirthRecordController.DeleteRequest deleteRequest = new BirthRecordController.DeleteRequest();
        deleteRequest.setRecordId(recordId);
        HttpEntity<BirthRecordController.DeleteRequest> deleteRequestEntry = new HttpEntity<>(deleteRequest, authorizationHttpHeaders);
        ResponseEntity<NormalResponse> deleteResponseEntry = restTemplate.exchange("/api/birthRecord/delete",
            HttpMethod.POST, deleteRequestEntry, NormalResponse.class);
        Assert.assertNotNull(deleteResponseEntry.getBody());
        Assert.assertEquals(0, deleteResponseEntry.getBody().getCode());
        Assert.assertEquals("OK", deleteResponseEntry.getBody().getMessage());
    }

    /**
     * 尝试获取最近好友生日列表
     */
    @Test
    public void testList() {
        HttpEntity entity = new HttpEntity(authorizationHttpHeaders);
        ResponseEntity<NormalResponse> responseEntity = restTemplate
            .exchange("/api/birthRecord/list", HttpMethod.GET, entity, NormalResponse.class);

        Assert.assertNotNull(responseEntity.getBody());
        Object list = responseEntity.getBody().getData();
        PageItem<?> pageItem = JsonMapperUtils.value(list, PageItem.class);
        Assert.assertTrue(pageItem.getItems().size() > 0);
    }
}
