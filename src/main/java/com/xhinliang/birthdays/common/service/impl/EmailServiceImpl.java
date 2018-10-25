package com.xhinliang.birthdays.common.service.impl;

import com.xhinliang.birthdays.common.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.*;

/**
 * @author xhinliang
 */
@Service
@Lazy
@Profile("prod")
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final long DEFAULT_TIMEOUT = 5000L;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public boolean sendEmail(String email, String subject, String content) {
        try {
            return runWithTimeout(() -> sendEmailInternal(email, subject, content), DEFAULT_TIMEOUT,
                TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("send email error", e);
            return false;
        }
    }

    private <T> T runWithTimeout(Callable<T> callable, long timeout, TimeUnit timeUnit)
        throws Exception {
        final Future<T> future = executor.submit(callable);
        try {
            return future.get(timeout, timeUnit);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw e;
        } catch (ExecutionException e) {
            // unwrap the root cause
            Throwable t = e.getCause();
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof Exception) {
                throw (Exception) t;
            } else {
                throw new IllegalStateException(t);
            }
        }
    }

    private boolean sendEmailInternal(String email, String subject, String content)
        throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        helper.setFrom(emailFrom);
        helper.setTo(email);
        helper.setText(content);
        helper.setSubject(subject);
        javaMailSender.send(helper.getMimeMessage());
        return true;
    }
}
