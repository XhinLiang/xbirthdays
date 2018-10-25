package com.xhinliang.birthdays.common.config.security.model;

import com.xhinliang.birthdays.common.db.model.UserModel;

import lombok.Data;

/**
 * @author xhinliang
 */
@Data
public class JwtSessionItem {

    private String token;
    private String email;
    private String nickname;

    public JwtSessionItem(UserModel internalUser, String token) {
        this.token = token;
        this.email = internalUser.getEmail();
        this.nickname = internalUser.getNickname();
    }
}
