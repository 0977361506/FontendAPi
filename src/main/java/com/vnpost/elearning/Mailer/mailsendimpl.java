package com.vnpost.elearning.Mailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class mailsendimpl implements mailsend {
  @Autowired private JavaMailSender javaMailSender;

  @Override
  public void send(String to, String from, String subject, String content)
      throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
    mimeMessageHelper.setFrom(from);
    mimeMessageHelper.setTo(to);
    mimeMessageHelper.setSubject(subject);
    mimeMessageHelper.setText("", content);
    mimeMessageHelper.setSentDate(new Date());
    javaMailSender.send(mimeMessage);
  }
}
