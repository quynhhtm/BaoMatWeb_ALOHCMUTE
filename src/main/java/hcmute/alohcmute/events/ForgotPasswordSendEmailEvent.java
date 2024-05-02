package hcmute.alohcmute.events;

import org.springframework.context.ApplicationEvent;

import hcmute.alohcmute.entities.TaiKhoan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordSendEmailEvent extends ApplicationEvent{
	private static final long serialVersionUID = 4753917531241269295L;
	
	private TaiKhoan taiKhoan;
	private String applicationUrl;
	
	public ForgotPasswordSendEmailEvent(TaiKhoan taiKhoan, String applicationUrl) {
		super(taiKhoan);
		this.taiKhoan = taiKhoan;
		this.applicationUrl = applicationUrl;
	}
}
