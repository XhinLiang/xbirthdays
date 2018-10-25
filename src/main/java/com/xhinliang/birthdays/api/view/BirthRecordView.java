package com.xhinliang.birthdays.api.view;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import com.xhinliang.framework.component.util.DateTimeUtils;
import com.xhinliang.lunarcalendar.LunarCalendar;

/**
 * @author xhinliang
 */
@SuppressWarnings("unused")
public class BirthRecordView {

    @JsonIgnore
    private BirthRecordModel record;

    public BirthRecordView(BirthRecordModel record) {
        this.record = record;
    }

    public Long getId() {
        return record.getId();
    }

    public Long getUserId() {
        return record.getBindId();
    }

    public String getEmail() {
        return record.getEmail();
    }

    public String getBirthdayType() {
        return record.getBirthdayTypeEnum().getTypeName();
    }

    public Long getBirthTime() {
        return record.getBirthTime();
    }

    public String getBirthdayString() {
        long birthTime = record.getBirthTime();
        if (record.getBirthdayTypeEnum() == BirthdayType.LUNAR) {
            return DateTimeUtils.getLunarStr(birthTime);
        }
        return DateTimeUtils.getStr(birthTime);
    }

    @Nullable
    private LunarCalendar getNextBirthday() {
        Long birthTime = record.getBirthTime();
        if (record.getBirthdayTypeEnum() == BirthdayType.LUNAR) {
            return DateTimeUtils.getNextLunarBirthday(record.getBirthTime());
        }
        return DateTimeUtils.getNextBirthday(record.getBirthTime());
    }

    public long getNextBirthdayTimestamp() {
        LunarCalendar nextBirthdayCalendar = getNextBirthday();
        if (nextBirthdayCalendar == null) {
            return 0L;
        }
        return nextBirthdayCalendar.getMillis();
    }

    public String getNextBirthdayString() {
        LunarCalendar nextBirthdayCalendar = getNextBirthday();
        if (nextBirthdayCalendar == null) {
            return "NONE";
        }
        if (record.getBirthdayTypeEnum() == BirthdayType.LUNAR) {
            return nextBirthdayCalendar.getFullLunarStr();
        }
        return String.format("%d年%d月%d日", nextBirthdayCalendar.getYear(),
            nextBirthdayCalendar.getMonth(), nextBirthdayCalendar.getDay());
    }

    public String getNickname() {
        return record.getNickname();
    }
}
