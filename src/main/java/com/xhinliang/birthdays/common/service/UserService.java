package com.xhinliang.birthdays.common.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.springframework.security.core.AuthenticationException;

import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.UserModel;

/**
 * @author xhinliang
 */
public interface UserService {

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    JwtTokenUser authenticate(String username, String password);

    List<UserModel> getUsers(Collection<Long> ids);

    List<UserModel> getAllUsers();

    @Nullable
    UserModel getUser(Long id);

    void updateBirthday(Long userId, BirthdayType birthdayType, long birthTime);

    void addNewUser(String email, String password, String nickname);
}
