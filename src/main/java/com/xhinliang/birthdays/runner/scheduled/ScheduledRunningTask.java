package com.xhinliang.birthdays.runner.scheduled;

import java.util.concurrent.TimeUnit;

/**
 * @author xhinliang
 */
public interface ScheduledRunningTask {

    void scheduled();

    default long fixedRate() {
        return TimeUnit.HOURS.toMillis(1L);
    }

    default long initialDelayed() {
        return TimeUnit.SECONDS.toMillis(1L);
    }

    default TimeUnit timeUnit() {
        return TimeUnit.MILLISECONDS;
    }
}
