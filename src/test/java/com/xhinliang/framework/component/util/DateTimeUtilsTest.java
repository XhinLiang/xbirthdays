package com.xhinliang.framework.component.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xhinliang.lunarcalendar.LunarCalendar;

public class DateTimeUtilsTest {

    @Test
    public void getLunarStr() {
        String s = DateTimeUtils.getLunarStr(1525591507387L);
        assertEquals("二零一八年三月廿一", s);

        String s1 = DateTimeUtils.getLunarStr(-1525591507387L);
        System.out.println(s1);
    }

    @Test
    public void getStr() {
        String s = DateTimeUtils.getStr(1525591507387L);
        assertEquals("2018年5月6日", s);
    }

    @Test
    public void getNextBirthday() {
        LunarCalendar nextLunarBirthday = DateTimeUtils.getNextLunarBirthday(748828800000L);
        System.out.println(nextLunarBirthday.getFullLunarStr());

        LunarCalendar nextBirthday = DateTimeUtils.getNextBirthday(748828800000L);
        System.out.println(String.format("%d-%d-%d", nextBirthday.getYear(), nextBirthday.getMonth(), nextBirthday.getDay()));
    }
}
