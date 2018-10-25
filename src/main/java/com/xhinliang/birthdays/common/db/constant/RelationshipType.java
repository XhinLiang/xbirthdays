package com.xhinliang.birthdays.common.db.constant;

import com.xhinliang.framework.component.interfaze.HasIntValue;

/**
 * @author xhinliang <xhinliang@gmail.com>
 * Created on 2018-10-15
 */
public enum RelationshipType implements HasIntValue {
    UNKNOWN(0), //
    NONE(1), //
    A_TO_B(2), //
    B_TO_A(3), //
    DUPLEX(4), //
    ;
    int status;

    RelationshipType(int status) {
        this.status = status;
    }

    @Override
    public int getValue() {
        return status;
    }

    public static RelationshipType ofValue(int value) {
        for (RelationshipType relationshipType : values()) {
            if (relationshipType.status == value) {
                return relationshipType;
            }
        }
        return UNKNOWN;
    }
}
