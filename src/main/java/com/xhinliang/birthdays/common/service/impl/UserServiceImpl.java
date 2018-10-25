package com.xhinliang.birthdays.common.service.impl;

import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.constant.UserRole;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.birthdays.common.db.repo.UserModelRepo;
import com.xhinliang.birthdays.common.exception.ServerException;
import com.xhinliang.birthdays.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author xhinliang
 */
@Service
@Lazy
public class UserServiceImpl implements UserService {

    private final UserModelRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserModelRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    public JwtTokenUser authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return (JwtTokenUser) authentication.getPrincipal();
        } catch (DisabledException e) {
            throw new ServerException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new ServerException("Bad credentials!", e);
        }
    }

    public List<UserModel> getUsers(Collection<Long> ids) {
        return userRepo.findXInternalUsersByIdIn(ids);
    }

    public List<UserModel> getAllUsers() {
        return userRepo.findAll();
    }

    @Nullable
    public UserModel getUser(Long id) {
        return getUsers(Collections.singleton(id)) //
            .stream() //
            .findFirst() //
            .orElse(null);
    }

    public void updateBirthday(Long userId, BirthdayType birthdayType, long birthTime) {
        userRepo.updateBirthday(userId, birthTime, birthdayType.getValue());
    }

    public void addNewUser(String email, String password, String nickname) {
        UserModel newUser = new UserModel();
        newUser.setEmail(email);
        newUser.setRole(UserRole.ROLE_USER.name());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setNickname(nickname);
        userRepo.save(newUser);
    }
}
