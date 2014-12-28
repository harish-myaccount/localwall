package com.geoc.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailDispatcher {

	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private SimpleMailMessage preConfiguredMail;
	
	public void sendMail( String to, String msg) {
		 
		SimpleMailMessage message = new SimpleMailMessage(preConfiguredMail);
		message.setTo(to);
		message.setText(msg);
		mailSender.send(message);	
	}


}
