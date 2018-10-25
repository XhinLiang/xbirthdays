package com.xhinliang.birthdays.common.db.model;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.constant.ModelStatus;
import lombok.Data;

import javax.persistence.*;

/**
 * never show this model to frontend.
 *
 * @author xhinliang
 */
@Entity
@Data
@Table(name = "birth_record_model", indexes = {
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_bind_id", columnList = "bindId"),
    @Index(name = "idx_record_status", columnList = "recordStatus"),
    @Index(name = "idx_creator_user_id", columnList = "creatorUserId"),
})
public class BirthRecordModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 64)
    private String email;

    @Column
    private long bindId;

    @Column
    private long creatorUserId;

    @Column
    private String nickname;

    @Column
    private int recordStatus;

    @Column
    private long birthTime;

    @Column
    private int birthdayType;

    @Column
    private long createdTime;

    @Column
    private long updatedTime;

    public BirthRecordModel() {
    }

    @PrePersist
    protected void onCreate() {
        createdTime = System.currentTimeMillis();
        updatedTime = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = System.currentTimeMillis();
    }

    public BirthdayType getBirthdayTypeEnum() {
        return BirthdayType.of(this.birthdayType);
    }

    public ModelStatus getModelStatusEnum() {
        return ModelStatus.getFromStatusValue(this.recordStatus);
    }

    public void setModelStatusEnum(ModelStatus modelStatusEnum) {
        this.recordStatus = modelStatusEnum.getValue();
    }
}
