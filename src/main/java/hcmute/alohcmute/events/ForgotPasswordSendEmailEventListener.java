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
public class ForgotPasswordSendEmailEventListener implements ApplicationListener<ForgotPasswordSendEmailEvent> {
	
	@Autowired
	private IUserService userService;
	@Autowired 
	private IEmailSendService emailSendService;
	private TaiKhoan user;
	
	@Override
	public void onApplicationEvent(ForgotPasswordSendEmailEvent event) {
		user = event.getTaiKhoan();
		String resetPasswordToken = UUID.randomUUID().toString();
		userService.saveToken(user, resetPasswordToken);
		String url = event.getApplicationUrl() + "/forgotpassword/reset?token=" + resetPasswordToken;
		try {
			sendForgotPasswordEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}
	
	public void sendForgotPasswordEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Forgot Password";
        String mailContent = 
        		"<p> "
        			+ "Xin chào, "+ user.getHoTen() + "! </p>" 
        		+ "<p> " 
	                + "Vui lòng nhấp vào liên kết bên dưới để đặt lại mật khẩu việc đăng ký tài khoản. "
                + "</p>" 
                	+ "<a href=\"" + url + "\"> Đặt lại mật khẩu </a>"
                + "<p> "
                + "Trân trọng! <br>";
        
        emailSendService.sendEmail(user.getEmail(), subject, mailContent);
    }
}
