package hcmute.alohcmute.events;

import org.springframework.context.ApplicationEvent;

import hcmute.alohcmute.entities.TaiKhoan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterVerifySendEmailEvent extends ApplicationEvent{
	private static final long serialVersionUID = -1946845590746761039L;

	private TaiKhoan taiKhoan;
	private String applicationUrl;
	
	public RegisterVerifySendEmailEvent(TaiKhoan taiKhoan, String applicationUrl) {
		super(taiKhoan);
		this.taiKhoan = taiKhoan;
		this.applicationUrl = applicationUrl;
	}
}
