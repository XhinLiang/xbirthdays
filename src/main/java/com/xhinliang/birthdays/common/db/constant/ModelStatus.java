package com.xhinliang.birthdays.common.db.constant;

import com.xhinliang.framework.component.interfaze.HasIntValue;

/**
 * mark a database model is active or deleted.
 *
 * @author xhinliang
 */
public enum ModelStatus implements HasIntValue {
    MODEL_STATUS_ACTIVE(0), //
    MODEL_STATUS_DELETED(1), //
    ;

    private int statusValue;

    ModelStatus(int statusValue) {
        this.statusValue = statusValue;
    }

    public static ModelStatus getFromStatusValue(int statusValue) {
        for (ModelStatus modelStatus : values()) {
            if (modelStatus.getValue() == statusValue) {
                return modelStatus;
            }
        }
        return MODEL_STATUS_ACTIVE;
    }

    @Override
    public int getValue() {
        return statusValue;
    }
}
