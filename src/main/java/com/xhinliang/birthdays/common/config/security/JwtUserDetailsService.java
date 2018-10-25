package com.xhinliang.birthdays.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.db.model.UserModel;
import com.xhinliang.birthdays.common.db.repo.UserModelRepo;

/**
 * @author xhinliang
 */
@Service
@Lazy
public class JwtUserDetailsService implements UserDetailsService {

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Autowired
    private UserModelRepo userRepo;

    @Override
    public JwtTokenUser loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepo.findOneByEmail(email) //
            .orElseThrow(() -> new UsernameNotFoundException("email does not exist."));

        JwtTokenUser tokenUser = new JwtTokenUser(user);
        detailsChecker.check(tokenUser);
        return tokenUser;
    }
}
