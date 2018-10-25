package com.xhinliang.birthdays.runner.scheduled;

import com.xhinliang.birthdays.runner.ApplicationRunnerOrder;
import com.xhinliang.birthdays.runner.BaseApplicationRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author xhinliang
 */
@ApplicationRunnerOrder(100)
@Component
public class ScheduledTaskManager extends BaseApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskManager.class);

    private ScheduledExecutorService executor;

    private final List<ScheduledRunningTask> tasks;

    @Autowired
    public ScheduledTaskManager(List<ScheduledRunningTask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("start scheduled task manager, tasks: {}", tasks);
        executor = Executors.newScheduledThreadPool(tasks.size());
        tasks.forEach(task -> {
            logger.info("schedule task: {}", task);
            executor.scheduleAtFixedRate(task::scheduled, task.initialDelayed(), task.fixedRate(),
                task.timeUnit());
        });
    }
}
