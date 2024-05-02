package hcmute.alohcmute.security;

import org.springframework.security.core.context.SecurityContextHolder;

import hcmute.alohcmute.entities.TaiKhoan;

public class SecurityUtil {
	
	public static TaiKhoan getMyUser() {
		UserDetailsImpl myUserInformation = (UserDetailsImpl) 
				(SecurityContextHolder
						.getContext()
						.getAuthentication()
						.getPrincipal());
		TaiKhoan user = myUserInformation.getUser();
		return user;
	}

}
