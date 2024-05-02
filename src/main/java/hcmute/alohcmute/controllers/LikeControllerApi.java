package hcmute.alohcmute.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.BaiVietServiceImpl;
import hcmute.alohcmute.services.IBaiVietService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("user")
public class LikeControllerApi {

	@Autowired
	IBaiVietService baiVietService = new BaiVietServiceImpl();
	
	String username;

	@PostMapping("like/{baiVietId}")
	public ResponseEntity<Void> add(@PathVariable(value = "baiVietId") int baiVietId) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		baiVietService.tangLike(baiVietId, username);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PostMapping("nolike/{baiVietId}")
	public ResponseEntity<Void> delete(@PathVariable(value = "baiVietId") int baiVietId) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		baiVietService.giamLike(baiVietId, username);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
