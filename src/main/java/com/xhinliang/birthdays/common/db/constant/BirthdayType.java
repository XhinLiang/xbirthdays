package com.xhinliang.birthdays.common.db.constant;

import com.xhinliang.framework.component.interfaze.HasIntValue;

/**
 * identify which birthday will be celebrate, Lunar or Gregorian.
 */
public enum BirthdayType implements HasIntValue {
    UNKNOWN(0, "未知"),
    LUNAR(1, "农历"),
    GREGORIAN(2, "阳历"),
    ;

    private int value;
    private String typeName;

    BirthdayType(int value, String typeName) {
        this.value = value;
        this.typeName = typeName;
    }

    public static BirthdayType of(int value) {
        for (BirthdayType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getValue() {
        return value;
    }
}
