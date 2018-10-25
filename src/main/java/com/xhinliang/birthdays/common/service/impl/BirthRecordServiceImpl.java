package com.xhinliang.birthdays.common.service.impl;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import com.xhinliang.birthdays.common.db.repo.BirthRecordModelRepo;
import com.xhinliang.birthdays.common.dto.PageItem;
import com.xhinliang.birthdays.common.service.BirthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.util.List;

/**
 * @author xhinliang
 */
@Service
@Lazy
public class BirthRecordServiceImpl implements BirthRecordService {

    private final BirthRecordModelRepo birthRecordModelRepo;

    @Autowired
    public BirthRecordServiceImpl(BirthRecordModelRepo birthRecordModelRepo) {
        this.birthRecordModelRepo = birthRecordModelRepo;
    }

    public BirthRecordModel create(long creatorUserId, String email, String nickname, BirthdayType birthdayType,
        long birthTime) {
        BirthRecordModel recordModel = new BirthRecordModel();
        recordModel.setEmail(email);
        recordModel.setNickname(nickname);
        recordModel.setBirthdayType(birthdayType.getValue());
        recordModel.setBirthTime(birthTime);
        recordModel.setCreatorUserId(creatorUserId);
        return birthRecordModelRepo.save(recordModel);
    }

    public List<BirthRecordModel> findAll(long creatorUserId) {
        return birthRecordModelRepo.findAllByCreatorUserId(creatorUserId);
    }

    /**
     * not support yet.
     */
    @Deprecated
    public void createByUserId(long creatorUserId, long friendUserId) {
        throw new RuntimeException(new NotSupportedException());
    }

    public void deleteById(long recordId) {
        birthRecordModelRepo.deleteById(recordId);
    }

    public PageItem<BirthRecordModel> findBirthRecords(long userId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BirthRecordModel> birthRecords = birthRecordModelRepo.findAllByCreatorUserId(userId,
            pageable);
        return PageItem.of(birthRecords);
    }
}
