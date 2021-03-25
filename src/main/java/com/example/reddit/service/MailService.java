package com.example.reddit.service;


import com.example.reddit.exceptions.SpringRedditException;
import com.example.reddit.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.swing.*;

@AllArgsConstructor
@Service
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("springreddit-1b2347@inbox.mailtrap.io");
            mimeMessageHelper.setText(notificationEmail.getBody());
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
        };
        try{
            mailSender.send(mimeMessagePreparator);
        }catch (MailException e){
            log.error("Mail exception");
            throw new SpringRedditException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }
    }


}
