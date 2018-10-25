package com.xhinliang.birthdays.common.config.security.model;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.xhinliang.birthdays.common.db.model.UserModel;


/**
 * @author xhinliang
 */
public class JwtTokenUser extends User {

    private UserModel internalUser;

    public JwtTokenUser(UserModel internalUser) {
        super(internalUser.getEmail(), internalUser.getPassword(),
            AuthorityUtils.createAuthorityList(internalUser.getRole()));
        this.internalUser = internalUser;
    }

    public UserModel getInternalUser() {
        return internalUser;
    }

    public String getRole() {
        return internalUser.getRole();
    }
}
