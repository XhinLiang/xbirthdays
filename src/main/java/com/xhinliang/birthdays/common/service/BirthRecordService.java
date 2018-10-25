package com.xhinliang.birthdays.common.service;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import com.xhinliang.birthdays.common.dto.PageItem;

import java.util.List;

/**
 * @author xhinliang
 */
public interface BirthRecordService {

    BirthRecordModel create(long creatorUserId, String email, String nickname, BirthdayType birthdayType,
        long birthTime);

    List<BirthRecordModel> findAll(long creatorUserId);

    /**
     * not support yet.
     */
    @Deprecated
    void createByUserId(long creatorUserId, long friendUserId);

    void deleteById(long recordId);

    PageItem<BirthRecordModel> findBirthRecords(long userId, int size, int page);
}
