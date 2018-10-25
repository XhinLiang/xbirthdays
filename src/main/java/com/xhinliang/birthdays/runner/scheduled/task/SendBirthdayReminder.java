package com.xhinliang.birthdays.runner.scheduled.task;

import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.birthdays.common.service.BirthRecordService;
import com.xhinliang.birthdays.common.service.EmailService;
import com.xhinliang.birthdays.common.service.UserService;
import com.xhinliang.framework.component.util.DateTimeUtils;
import com.xhinliang.birthdays.runner.scheduled.ScheduledRunningTask;
import com.xhinliang.lunarcalendar.LunarCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xhinliang
 */
@Service
@Lazy
public class SendBirthdayReminder implements ScheduledRunningTask {

    private static final Logger logger = LoggerFactory.getLogger(SendBirthdayReminder.class);

    private static final long INITIAL_DELAYED = TimeUnit.HOURS.toMillis(1L);

    private final EmailService emailService;

    private final BirthRecordService birthRecordService;

    private final UserService userService;

    @Autowired
    public SendBirthdayReminder(EmailService emailService, BirthRecordService birthRecordService, UserService userService) {
        this.emailService = emailService;
        this.birthRecordService = birthRecordService;
        this.userService = userService;
    }

    @Override
    public void scheduled() {
        logger.info("tick");
        List<UserModel> users = userService.getAllUsers();
        users.forEach(this::checkUserBirthRecords);
    }

    private void checkUserBirthRecords(UserModel userModel) {
        List<BirthRecordModel> birthRecordModels = birthRecordService.findAll(userModel.getId());
        birthRecordModels.forEach(birthRecordModel -> checkIsTodayAndSendEmail(userModel, birthRecordModel));
    }

    private void checkIsTodayAndSendEmail(UserModel userModel, BirthRecordModel birthRecordModel) {
        LunarCalendar nextBirthday = buildNextDay(birthRecordModel);
        if (nextBirthday.isToday()) {
            boolean sent = emailService.sendEmail(userModel.getEmail(),
                birthRecordModel.getNickname(), birthRecordModel.getEmail());
            logger.info("user:{}, birthRecord: {}, sent birth remind: {}",
                userModel.getId(), birthRecordModel.getId(), sent);
        }
    }

    private LunarCalendar buildNextDay(BirthRecordModel birthRecordModel) {
        LunarCalendar nextBirthday;
        if (birthRecordModel.getBirthdayTypeEnum() == BirthdayType.LUNAR) {
            nextBirthday = DateTimeUtils.getNextLunarBirthday(birthRecordModel.getBirthTime());
        } else {
            nextBirthday = DateTimeUtils.getNextBirthday(birthRecordModel.getBirthTime());
        }
        return nextBirthday;
    }

    @Override
    public long fixedRate() {
        return INITIAL_DELAYED;
    }
}
