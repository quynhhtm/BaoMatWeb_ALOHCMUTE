package hcmute.alohcmute.services;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;

public interface IEmailSendService {

	void sendEmail(String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException;

}
