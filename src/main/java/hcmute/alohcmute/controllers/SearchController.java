package hcmute.alohcmute.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hcmute.alohcmute.models.TaiKhoanModel;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.ITaiKhoanService;

@Controller
public class SearchController {
	@Autowired
	ITaiKhoanService taiKhoanService;
	String username;
	@GetMapping("/user/search/{keywork}")
	public String home(ModelMap model, @PathVariable(value = "keywork") String keywork) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		List<TaiKhoanModel> listTaiKhoan = taiKhoanService.findTaiKhoanByKeyword(keywork, username);
		List<TaiKhoanModel> list = new ArrayList<>();
		if (listTaiKhoan.size() != 0) {
			for (TaiKhoanModel tk : listTaiKhoan) {
				if (tk.getTaiKhoan().equals(username) == false && tk.getTaiKhoan().equals("admin") == false)
					list.add(tk);
			}
		}
		model.addAttribute("listTaiKhoan", list);
		return "user/search/search";
	}
}
