package com.xhinliang.framework.component.util;

import java.util.Arrays;
import java.util.Objects;

import com.xhinliang.framework.component.interfaze.HasIntValue;

/**
 * @author xhinliang
 */
public class EnumParseUtils {

    public static <T extends Enum & HasIntValue> T fromValue(Class<T> clazz, int value, T defaultValue) {
        return Arrays.stream(clazz.getEnumConstants()) //
            .filter((e) -> Objects.equals(e.getValue(), value)) //
            .findFirst() //
            .orElse(defaultValue);
    }
}
