package se.tjing.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
	@Bean
	JavaMailSender mailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		//mailSender.setHost("smtp.gmail.com");
		//mailSender.setPort(465);
		
		mailSender.setUsername("nossredna.sennahoj@gmail.com");
		mailSender.setPassword("qksy0ye4pJg6IPAg6ShxJQ");
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.host", "smtp.mandrillapp.com");
		props.setProperty("mail.smtp.port", "587");
		props.setProperty("mail.smtp.starttls.enable", "true");
		
		//props.setProperty("mail.smtp.trust", "smtp.gmail.com");
		//props.put("mail.smtp.socketFactory.port", "587"); //SSL Port
		//props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		
		props.setProperty("mail.debug", "true");
		
		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
}
