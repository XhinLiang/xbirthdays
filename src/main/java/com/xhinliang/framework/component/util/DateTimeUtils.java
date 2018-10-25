package com.xhinliang.framework.component.util;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.xhinliang.lunarcalendar.Lunar;
import com.xhinliang.lunarcalendar.LunarCalendar;

/**
 * @author xhinliang
 */
public class DateTimeUtils {

    private static final int MAX_FIND_DAY_COUNT = 2000;

    public static LunarCalendar getNextBirthday(long birthTimestamp) {
        return getNextBirthday(getLunarCalendar(birthTimestamp), System.currentTimeMillis(), 0);
    }

    private static LunarCalendar getNextBirthday(LunarCalendar birthTime, long timestamp,
        int count) {
        if (count > MAX_FIND_DAY_COUNT) {
            throw new RuntimeException("not found!");
        }
        LunarCalendar today = getLunarCalendar(timestamp);
        if (isSameMonthAndSameDay(birthTime, today)) {
            return today;
        }
        return getNextBirthday(birthTime, timestamp + TimeUnit.DAYS.toMillis(1), count + 1);
    }

    private static boolean isSameMonthAndSameDay(LunarCalendar a, LunarCalendar b) {
        return a.getMonth() == b.getMonth() && a.getDay() == b.getDay();
    }

    public static LunarCalendar getNextLunarBirthday(long birthTimestamp) {
        LunarCalendar birthTime = getLunarCalendar(birthTimestamp);
        return getNextLunarBirthday(birthTime, System.currentTimeMillis(), 0);
    }

    private static LunarCalendar getNextLunarBirthday(LunarCalendar birthTime, long timestamp,
        int count) {
        if (count > MAX_FIND_DAY_COUNT) {
            throw new RuntimeException("not found!");
        }
        LunarCalendar today = getLunarCalendar(timestamp);
        if (isSameLunarMonthAndSameLunarDay(birthTime, today)) {
            return today;
        }
        return getNextLunarBirthday(birthTime, timestamp + TimeUnit.DAYS.toMillis(1), count + 1);
    }

    private static boolean isSameLunarMonthAndSameLunarDay(LunarCalendar a, LunarCalendar b) {
        Lunar lunarA = a.getLunar();
        Lunar lunarB = b.getLunar();
        return lunarA.day == lunarB.day && lunarA.month == lunarB.month;
    }

    public static String getLunarStr(long timestamp) {
        LunarCalendar lunarCalendar = getLunarCalendar(timestamp);
        return lunarCalendar.getFullLunarStr();
    }

    private static LunarCalendar getLunarCalendar(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(TimeZone.getDefault().toZoneId());
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();
        return LunarCalendar.obtainCalendar(year, month, day);
    }

    public static String getStr(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(TimeZone.getDefault().toZoneId());
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();
        return String.format("%d年%d月%d日", year, month, day); //
    }
}
