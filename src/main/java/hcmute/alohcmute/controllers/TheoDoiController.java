package hcmute.alohcmute.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.ITaiKhoanService;

@Controller
@RequestMapping("user/follow")
public class TheoDoiController {

	@Autowired(required = true)
	ITaiKhoanService tkSer;
	String username ;
	@GetMapping("")
	public String TheoDoi(ModelMap model) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		List<TaiKhoan> tkTheoDoi = new ArrayList<>(tkSer.findTaiKhoanTheoDoisByUsername(username));
		model.addAttribute("Listtaikhoan",tkTheoDoi);
		
		List<TaiKhoan> tkDuocTheoDoi = new ArrayList<>(tkSer.findTaiKhoanFollowersByUsername(username));
		model.addAttribute("ListTKDuocTheoDoi",tkDuocTheoDoi);
		
		Map<TaiKhoan, Integer> BanChung = tkSer.NguoiTheoDoiChung(username);
		model.addAttribute("BanChung",BanChung);
		
		return "user/banbe/banbe.html";
	}
	
	@GetMapping("unfollow")
	public ModelAndView delet(ModelMap model, @RequestParam("username") String userNameUnfollow) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		TaiKhoan user1=tkSer.findBytaiKhoan(username);
		TaiKhoan user2=tkSer.findBytaiKhoan(userNameUnfollow);
		tkSer.unfollow(user1,user2);
		//tkSer.follow(user1, user2);


		return new ModelAndView("redirect:/user/follow", model);

	}
	
	@GetMapping("addfollow")
	public ModelAndView follow(ModelMap model, @RequestParam("username") String userNameFollow) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		TaiKhoan user1=tkSer.findBytaiKhoan(username);
		TaiKhoan user2=tkSer.findBytaiKhoan(userNameFollow);
		tkSer.follow(user1,user2);


		return new ModelAndView("redirect:/user/follow", model);

	}
}
