package hcmute.alohcmute.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hcmute.alohcmute.dtos.TaiKhoanDto;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.events.ForgotPasswordSendEmailEvent;
import hcmute.alohcmute.events.RegisterVerifySendEmailEvent;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class WebController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping("/login")
	public String showLoginForm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        
        model.addAttribute("typenotify", "error");
        model.addAttribute("mess", errorMessage);
        
		return "web/dangnhap/dangnhap";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		TaiKhoanDto taiKhoanDto = new TaiKhoanDto();
		model.addAttribute("user", taiKhoanDto);
		return "web/dangky/dangky";
	}

	@PostMapping("/register/save")
	public String registration(
			@Valid @ModelAttribute("user") TaiKhoanDto taiKhoanDto, 
			BindingResult result,
			Model model, 
			HttpServletRequest request) {

		Optional<TaiKhoan> userExist = userService
				.findByTaiKhoanOrEmail(
						taiKhoanDto.getUsername(),
						taiKhoanDto.getEmail());
		if (userExist.isPresent() 
				&& userExist.get().getTaiKhoan().equals(taiKhoanDto.getUsername())) {
			System.out.println("username");
			model.addAttribute("typenotify", "error");
			model.addAttribute("mess", "Tài khoản đã được đăng ký bởi người dùng khác");
			return "web/dangky/dangky";
		}

		if (userExist.isPresent() 
				&& userExist.get().getEmail().equals(taiKhoanDto.getEmail())) {
			System.out.println("email");
			model.addAttribute("typenotify", "error");
			model.addAttribute("mess", "Email đã được đăng ký bởi người dùng khác");
			return "web/dangky/dangky";
		}

		if (result.hasErrors()) {
			System.out.println("email");
			model.addAttribute("user", taiKhoanDto);
			model.addAttribute("typenotify", "error");
			model.addAttribute("mess", "Thông tin nhập vào không hợp lệ vui lòng nhập lại");
			return "web/dangky/dangky";
		}
		
		TaiKhoan user = userService.saveTaiKhoan(taiKhoanDto);
		publisher.publishEvent(new RegisterVerifySendEmailEvent(user, applicationUrl(request)));

		return "web/dangky/dangkythanhcong";
	}

	@GetMapping("/register/verify")
	public String verifyRegistration(@RequestParam("token") String token) {
		if(token.equals("no")) {
			return "redirect:/404";
		}
		
		Optional<TaiKhoan> user = userService.findByToken(token);
		if (user.isPresent()) {
			userService.saveEnable(user.get());
			return "web/dangky/xacthucthanhcong";
		}

		return "redirect:/404";
	}

	@GetMapping("/forgotpassword")
	public String showForgotPasswordForm(Model model) {
		String email = "";
		model.addAttribute("email", email);
		return "web/quenmatkhau/quenmatkhau";
	}

	@PostMapping("/forgotpassword/find")
	public String forgotPassword(
			@RequestParam("email") String email, 
			Model model, 
			HttpServletRequest request) {
		Optional<TaiKhoan> user = userService.findByEmail(email);
		if (user.isPresent()) {
			publisher.publishEvent(new ForgotPasswordSendEmailEvent(user.get(), applicationUrl(request)));
			model.addAttribute("typenotify", "success");
			model.addAttribute("mess", "Vui lòng kiểm tra email: " + email + " để thay đổi mật khẩu");
		} else {
			model.addAttribute("typenotify", "error");
			model.addAttribute("mess", "Email không tồn tại");
		}
		return "web/quenmatkhau/quenmatkhau";
	}

	@GetMapping(value = "/forgotpassword/reset")
	public String showResetPassword(
			@RequestParam("token") String token,
			Model model) {
		if(token.equals("no")) {
			return "redirect:/404";
		}
		
		Optional<TaiKhoan> user = userService.findByToken(token);
		if (user.isPresent()) {
			model.addAttribute("token", token);
			model.addAttribute("email", user.get().getEmail());
			return "web/quenmatkhau/datlaimatkhau";
		}
		
		return "redirect:/404";
	}

	@PostMapping(value = "/forgotpassword/reset")
	public String resetPassword(
			@RequestParam("token") String token, 
			@RequestParam("email") String email, 
			@RequestParam("password") String password,
			Model model) {
		if(token.equals("no")) {
			return "redirect:/404";
		}
		
		Optional<TaiKhoan> user = userService.findByTokenAndEmail(email, token);
		if (user.isPresent()) {
			userService.saveResetPassword(user.get(), password);
			model.addAttribute("typenotify", "success");
			model.addAttribute("mess", "Thay đổi mật khẩu thành công");
			return "web/quenmatkhau/datlaimatkhau";
		}

		return "redirect:/404";
	}

	@GetMapping("/403")
	public String error403() {
		return "web/error/error403";
	}

	@GetMapping("/404")
	public String error404() {
		return "web/error/error404";
	}

	public String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@GetMapping("/test")
	public String test() {
		TaiKhoan user = SecurityUtil.getMyUser();
		
		System.out.println(user.getTaiKhoan());
		System.out.println(user.getMatKhau());
		System.out.println(user.getHoTen());
		System.out.println(user.getSDT());
		System.out.println(user.getGioiTinh());
		System.out.println(user.getToken());
		
		return "web/error/error403";
	}
};