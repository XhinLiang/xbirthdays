package com.xhinliang.birthdays.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import com.xhinliang.birthdays.common.db.repo.BirthRecordModelRepo;
import com.xhinliang.birthdays.common.service.impl.BirthRecordServiceImpl;

/**
 * @author xhinliang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BirthRecordServiceTest {

    @Autowired
    private BirthRecordServiceImpl birthRecordService;

    @Autowired
    private BirthRecordModelRepo birthRecordModelRepo;

    @Test
    public void testCreate() {
        birthRecordService.create(2L, "xhinliang@gmail.com",
            "xhinliang", BirthdayType.LUNAR, 1525591507387L);

        List<BirthRecordModel> records = birthRecordModelRepo.findAllByCreatorUserId(2L);
        Assert.assertEquals(1, records.size());
    }
}
