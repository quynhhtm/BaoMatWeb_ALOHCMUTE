package hcmute.alohcmute.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hcmute.alohcmute.entities.LoaiTaiKhoan;
import hcmute.alohcmute.entities.TaiKhoan;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = -4427990940118984261L;
	private TaiKhoan user;
	
	public UserDetailsImpl(TaiKhoan user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<LoaiTaiKhoan> loaiTaiKhoans = user.getLoaiTaiKhoans();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (LoaiTaiKhoan loaiTaiKhoan : loaiTaiKhoans) {
			authorities.add(new SimpleGrantedAuthority(loaiTaiKhoan.getTenLoai()));
		}
		return authorities;
	}
	
	public TaiKhoan getUser() {
		return user;
	}
	
	@Override
	public String getUsername() {
		return user.getTaiKhoan();
	}
	
	@Override
	public String getPassword() {
		return user.getMatKhau();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnable();
	}
}
