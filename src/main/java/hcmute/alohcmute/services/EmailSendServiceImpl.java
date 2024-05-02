package hcmute.alohcmute.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendServiceImpl implements IEmailSendService {
	@Autowired
	private JavaMailSender mailSender;
	private String fromEmail = "147.qter@gmail.com";
	private String senderName = "ALOHCMUTE";
	
	@Override
	public void sendEmail(String toEmail, String subject, String body) 
			throws MessagingException, UnsupportedEncodingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
			
		messageHelper.setFrom(fromEmail, senderName);
		messageHelper.setTo(toEmail);
		messageHelper.setSubject(subject);
		messageHelper.setText(body, true);
		
		mailSender.send(message);
	}
}
