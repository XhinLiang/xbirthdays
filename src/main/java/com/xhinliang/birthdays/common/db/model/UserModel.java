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
@Table(name = "user_model", indexes = {
    @Index(name = "idx_user_email", columnList = "email", unique = true),
    @Index(name = "idx_user_status", columnList = "userStatus"),
})
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 64)
    private String email;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column
    private int userStatus;

    @Column
    private long birthTime;

    @Column
    private int birthdayType;

    @Column
    private String role;

    @Column
    private long createdTime;

    @Column
    private long updatedTime;

    public UserModel() {
        this.password = "noPassword";
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
        return ModelStatus.getFromStatusValue(this.userStatus);
    }

    public void setModelStatusEnum(ModelStatus userStatus) {
        this.userStatus = userStatus.getValue();
    }
}
