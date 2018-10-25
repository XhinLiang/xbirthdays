package com.xhinliang.birthdays.common.service;

/**
 * @author xhinliang
 */
public interface EmailService {

    boolean sendEmail(String email, String subject, String content);
}
