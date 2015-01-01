package com.geoc.app;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Value("${mail.host}")
	private String host;
	@Value("${mail.port}")
	private Integer port;
	@Value("${mail.username}")
	private String username;
	@Value("${mail.password}")
	private String password;
	
	@Value("${app.domain}")
	private static String domain;
	

	@Bean
	public JavaMailSender mailSender(){
		JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
		mailsender.setHost(host);
		mailsender.setPort(port);
		mailsender.setUsername(username);
		mailsender.setPassword(password);
		Properties defaults = new Properties();
		defaults.setProperty("mail.smtp.auth", "true");
		defaults.setProperty("mail.smtp.starttls.enable", "true");
		mailsender.setJavaMailProperties(defaults);
		return mailsender;
	}
	
	@Bean 
	public SimpleMailMessage preConfiguredMail(){
		SimpleMailMessage pre = new SimpleMailMessage();
		pre.setFrom(username);
		pre.setSubject("LocalWall Secret Code");
		return pre;
	}
	
	public static String prefixDomain(){
		return "http://"+domain+"/";
	}
}

/**
<bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
	<property name="host" value="smtp.gmail.com" />
	<property name="port" value="587" />
	<property name="username" value="username" />
	<property name="password" value="password" />
 
	<property name="javaMailProperties">
	   <props>
       	      <prop key="mail.smtp.auth">true</prop>
       	      <prop key="mail.smtp.starttls.enable">true</prop>
       	   </props>
	</property>
</bean>
*/