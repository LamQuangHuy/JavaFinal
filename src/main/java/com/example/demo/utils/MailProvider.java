package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailProvider {
  @Autowired
  private JavaMailSender mailSender;

  public void sendSimpleEmail(String toEmail, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("tanphat200265@gmail.com");
    message.setTo(toEmail);
    message.setText(body);
    message.setSubject(subject);
    mailSender.send(message);
  }
}
