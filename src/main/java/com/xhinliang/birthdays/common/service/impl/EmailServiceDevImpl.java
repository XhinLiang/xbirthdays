package com.xhinliang.birthdays.common.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.xhinliang.birthdays.common.service.EmailService;

/**
 * @author xhinliang
 */
@Service
@Lazy
@Profile("dev")
public class EmailServiceDevImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceDevImpl.class);

    @Override
    public boolean sendEmail(String email, String subject, String content) {
        logger.info("FAKE sending email:{}, {}, {}", email, subject, content);
        return false;
    }
}
