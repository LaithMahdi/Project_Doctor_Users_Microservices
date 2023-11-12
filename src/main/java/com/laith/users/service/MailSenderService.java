package com.laith.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String toEmail,String subject,String body) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("Mahdilaith380@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		System.out.println("Mail send successfully");
	}

}
