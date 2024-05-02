package hcmute.alohcmute.events;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.services.IEmailSendService;
import hcmute.alohcmute.services.IUserService;
import jakarta.mail.MessagingException;

@Component
public class RegisterVerifySendEmailEventListener implements ApplicationListener<RegisterVerifySendEmailEvent> {
	
	@Autowired
	private IUserService userService;
	@Autowired 
	private IEmailSendService emailSendService;
	private TaiKhoan user;
	
	@Override
	public void onApplicationEvent(RegisterVerifySendEmailEvent event) {
		user = event.getTaiKhoan();
		String verificationToken = UUID.randomUUID().toString();
		userService.saveToken(user, verificationToken);
		String url = event.getApplicationUrl() + "/register/verify?token=" + verificationToken;
		try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}
	
	public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String mailContent = 
        		"<p> "
        			+ "Xin chào, "+ user.getHoTen() + "! </p>" 
        		+ "<p> "
	                + "Cảm ơn bạn đã đăng ký sử dụng mạng xã hội ALOHCMUTE <br>" 
	                + "Vui lòng nhấp vào liên kết bên dưới để hoàn thành việc đăng ký tài khoản. "
                + "</p>" 
                	+ "<a href=\"" + url + "\"> Xác nhận tài khoản của bạn </a>"
                + "<p> "
                + "Trân trọng! <br>";
        
        emailSendService.sendEmail(user.getEmail(), subject, mailContent);
    }
}
