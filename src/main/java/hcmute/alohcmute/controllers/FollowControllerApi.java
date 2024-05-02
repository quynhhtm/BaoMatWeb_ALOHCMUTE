package hcmute.alohcmute.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.ITaiKhoanService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("user")
public class FollowControllerApi {

	@Autowired
	ITaiKhoanService taiKhoanService;
	
	String username;
	
	@PostMapping("follow/{taiKhoan}")
	public ResponseEntity<Void> follow(@PathVariable(value = "taiKhoan") String usernameTheoDoi) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		TaiKhoan taiKhoan = taiKhoanService.findBytaiKhoan(username);
		TaiKhoan taiKhoanTheoDoi = taiKhoanService.findBytaiKhoan(usernameTheoDoi);
		taiKhoanService.follow(taiKhoan, taiKhoanTheoDoi);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping("unfollow/{taiKhoan}")
	public ResponseEntity<Void> unfollow(@PathVariable(value = "taiKhoan") String usernameTheoDoi) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		TaiKhoan taiKhoan = taiKhoanService.findBytaiKhoan(username);
		TaiKhoan taiKhoanTheoDoi = taiKhoanService.findBytaiKhoan(usernameTheoDoi);
		taiKhoanService.unfollow(taiKhoan, taiKhoanTheoDoi);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
