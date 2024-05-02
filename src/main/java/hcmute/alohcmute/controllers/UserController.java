package hcmute.alohcmute.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hcmute.alohcmute.services.ITaiKhoanService;

@Controller
public class UserController {
	@Autowired
	ITaiKhoanService taiKhoanService;
	
	@GetMapping("/user")
	public String userPage(Model model) {
		model.addAttribute("mess", "Welcome to user page");	
		return "user/home";
	}
}
