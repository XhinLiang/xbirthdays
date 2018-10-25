package com.xhinliang.birthdays.runner;

import com.xhinliang.birthdays.common.db.constant.UserRole;
import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.birthdays.common.db.repo.BirthRecordModelRepo;
import com.xhinliang.birthdays.common.db.repo.UserModelRepo;
import com.xhinliang.birthdays.common.service.impl.RelationshipServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

import static com.xhinliang.birthdays.common.db.constant.BirthdayType.GREGORIAN;
import static com.xhinliang.birthdays.common.db.constant.BirthdayType.LUNAR;

/**
 * mock data for test
 *
 * @author xhinliang
 */
@ApplicationRunnerOrder(1)
@Component
public class DevDataMocker extends BaseApplicationRunner {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final BirthRecordModelRepo birthRecordModelRepo;

    private final RelationshipServiceImpl relationshipService;

    private final PasswordEncoder passwordEncoder;

    private final UserModelRepo userModelRepo;

    @Autowired
    public DevDataMocker(BirthRecordModelRepo birthRecordModelRepo, RelationshipServiceImpl relationshipService, PasswordEncoder passwordEncoder, UserModelRepo userModelRepo) {
        this.birthRecordModelRepo = birthRecordModelRepo;
        this.relationshipService = relationshipService;
        this.passwordEncoder = passwordEncoder;
        this.userModelRepo = userModelRepo;
    }

    // CHECKSTYLE:OFF
    /**
     * mock records.
     */
    private void mockRecords() {
        for (int i = 1; i < 20; ++i) {
            BirthRecordModel birthRecordModel = new BirthRecordModel();
            birthRecordModel.setCreatorUserId(1L);
            birthRecordModel.setEmail("xhinliang" + i + "@gmail.com");
            birthRecordModel.setNickname("xhinliang" + i);
            if (i % 2 == 0) {
                birthRecordModel.setBirthdayType(LUNAR.getValue());
            } else {
                birthRecordModel.setBirthdayType(GREGORIAN.getValue());
            }
            ZonedDateTime zonedDateTime = ZonedDateTime.of(1970 + (i % 20), 1 + (i % 11),
                1 + (i % 28), 0, 0, 0, 0, ZoneId.systemDefault());
            birthRecordModel.setBirthTime(zonedDateTime.toEpochSecond() * 1000);
            birthRecordModelRepo.save(birthRecordModel);
        }
    }

    private void mockFriends() {
        Random random = new Random();
        for (int i = 2; i < 10; ++i) {
            relationshipService.follow(1L, random.nextInt(40) + 1L);
        }
    }

    /**
     * add mock users.
     */
    private void mockUsers() {
        for (int i = 1; i < 50; ++i) {
            UserModel newUser = new UserModel();
            newUser.setEmail("xhinliang" + i + "@gmail.com");
            newUser.setRole(UserRole.ROLE_USER.name());
            newUser.setPassword(passwordEncoder.encode("0"));
            newUser.setNickname("xhinliang" + i);
            if (i % 2 == 0) {
                newUser.setBirthdayType(LUNAR.getValue());
            } else {
                newUser.setBirthdayType(GREGORIAN.getValue());
            }
            ZonedDateTime tempZonedDateTime = ZonedDateTime.of(1970 + (i % 20), 1 + (i % 11),
                1 + (i % 28), 0, 0, 0, 0, ZoneId.systemDefault());
            newUser.setBirthTime(tempZonedDateTime.toEpochSecond() * 1000);
            userModelRepo.save(newUser);
        }

        // add root user
        UserModel rootUser = new UserModel();
        rootUser.setEmail("root@xbirthdays.top");
        rootUser.setRole(UserRole.ROLE_ADMIN.name());
        rootUser.setPassword(passwordEncoder.encode("root"));
        rootUser.setNickname("root");
        rootUser.setBirthdayType(LUNAR.getValue());
        ZonedDateTime zonedDateTime = ZonedDateTime.of(1993, 10, 24,
            12, 1, 1, 0, ZoneId.systemDefault());
        rootUser.setBirthTime(zonedDateTime.toEpochSecond() * 1000);
        userModelRepo.save(rootUser);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (isDevProfile()) {
            mockUsers();
            mockFriends();
            mockRecords();
        }
    }

    private boolean isDevProfile() {
        if (StringUtils.isBlank(activeProfile)) {
            return false;
        }
        return activeProfile.toLowerCase().startsWith("dev");
    }
    // CHECKSTYLE:ON
}
