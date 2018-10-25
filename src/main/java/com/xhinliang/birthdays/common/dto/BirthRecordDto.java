package com.xhinliang.birthdays.common.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class BirthRecordDto {
    private long id;

    private String email;

    private long userId;

    private long creatorUserId;

    private String nickname;

    private long birthTime;

    private int birthdayType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BirthRecordDto)) return false;
        BirthRecordDto that = (BirthRecordDto) o;
        return getId() == that.getId() &&
            getUserId() == that.getUserId() &&
            getCreatorUserId() == that.getCreatorUserId() &&
            getBirthTime() == that.getBirthTime() &&
            getBirthdayType() == that.getBirthdayType() &&
            Objects.equals(getEmail(), that.getEmail()) &&
            Objects.equals(getNickname(), that.getNickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getUserId(), getCreatorUserId(), getNickname(), getBirthTime(), getBirthdayType());
    }
}
