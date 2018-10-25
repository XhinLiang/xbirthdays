package com.xhinliang.birthdays.api.view;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.framework.component.util.DateTimeUtils;
import com.xhinliang.lunarcalendar.LunarCalendar;

/**
 * @author xhinliang
 */
@SuppressWarnings("unused")
public class UserProfileView {

    @JsonIgnore
    private UserModel user;

    public UserProfileView(UserModel user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getBirthdayType() {
        return user.getBirthdayTypeEnum().getTypeName();
    }

    public Long getBirthTime() {
        return user.getBirthTime();
    }

    public String getBirthdayString() {
        Long birthTime = user.getBirthTime();
        if (birthTime == null) {
            return "NONE";
        }
        if (user.getBirthdayTypeEnum() == BirthdayType.LUNAR) {
            return DateTimeUtils.getLunarStr(birthTime);
        }
        return DateTimeUtils.getStr(birthTime);
    }

    @Nullable
    private LunarCalendar getNextBirthday() {
        Long birthTime = user.getBirthTime();
        if (birthTime == null) {
            return null;
        }
        if (user.getBirthdayTypeEnum() == BirthdayType.LUNAR) {
            return DateTimeUtils.getNextLunarBirthday(user.getBirthTime());
        }
        return DateTimeUtils.getNextBirthday(user.getBirthTime());
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
        if (user.getBirthdayTypeEnum() == BirthdayType.LUNAR) {
            return nextBirthdayCalendar.getFullLunarStr();
        }
        return String.format("%d年%d月%d日", nextBirthdayCalendar.getYear(),
            nextBirthdayCalendar.getMonth(), nextBirthdayCalendar.getDay());
    }

    public String getNickname() {
        return user.getNickname();
    }
}
