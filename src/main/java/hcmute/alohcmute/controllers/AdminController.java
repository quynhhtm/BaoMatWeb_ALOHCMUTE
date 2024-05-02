package hcmute.alohcmute.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import hcmute.alohcmute.entities.BaoCaoBaiViet;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.models.TaiKhoanModel;
import hcmute.alohcmute.services.IBaoCaoBaiVietService;
import hcmute.alohcmute.services.ITaiKhoanService;

@Controller
public class AdminController {

	@Autowired
	ITaiKhoanService taiKhoanService;
	
	@Autowired
	IBaoCaoBaiVietService baoCaoBaiVietService;

	@GetMapping("admin/manage/quanlynguoidung")
	public String listTaiKhoan(ModelMap model) {

		List<TaiKhoan> list = taiKhoanService.findAll();
		model.addAttribute("listTaiKhoan", list);
		return "admin/manage/quanlynguoidung";
	}

	@GetMapping("admin/manage/thongtintaikhoan/{taikhoan}")
	public String thongTinTaiKhoan(ModelMap model, @PathVariable("taikhoan") String taikhoan) {
		Optional<TaiKhoan> optTaiKhoan = taiKhoanService.findById(taikhoan);
		TaiKhoanModel taiKhoanModel = new TaiKhoanModel();
		if (optTaiKhoan.isPresent()) {
			TaiKhoan entity = optTaiKhoan.get();
			BeanUtils.copyProperties(entity, taiKhoanModel);

			model.addAttribute("taikhoan", taiKhoanModel);
			return "admin/manage/thongtintaikhoan";

		}
		model.addAttribute("message", "Error!!!");
		return "admin/manage/thongtintaikhoan";
	}

	@GetMapping("admin/manage/delete/{taikhoan}")
	public ModelAndView delete(ModelMap model, @PathVariable("taikhoan") String taikhoan) {
		taiKhoanService.deleteById(taikhoan);
		model.addAttribute("message", "Xoa thanh cong!");
		return new ModelAndView("forward:/admin/manage/quanlynguoidung", model);
	}
	
	@GetMapping(value = {"/admin/chitiet/banUser/{taikhoan}/{mabaocao}", "/admin/chitiet/unbanUser/{taikhoan}/{mabaocao}"})
	public String banUser(@PathVariable(value = "taikhoan") String taikhoan, @PathVariable(value = "mabaocao") int mabaocao) {
		Optional<TaiKhoan> Opttaikhoan = taiKhoanService.findById(taikhoan); 
		TaiKhoan userban = Opttaikhoan.get();
		if (userban.isEnable()== true) {
			userban.setEnable(false);
		}
		else {
			userban.setEnable(true);
		}
		taiKhoanService.save(userban);
		
		return "redirect:/admin/chitiet/{mabaocao}";
	}
	
	@GetMapping(value = {"/admin/quanlynguoidung/{taikhoan}"})
	public String AdminbanUser(@PathVariable(value = "taikhoan") String taikhoan) {
		Optional<TaiKhoan> Opttaikhoan = taiKhoanService.findById(taikhoan); 
		TaiKhoan userban = Opttaikhoan.get();
		if (userban.isEnable()== true) {
			userban.setEnable(false);
		}
		else {
			userban.setEnable(true);
		}
		taiKhoanService.save(userban);
		
		return "redirect:/admin/manage/thongtintaikhoan/{taikhoan}";
	}
	
	@GetMapping("/admin/dsbaocaobaiviet")
	public String listBaoCaoBaiViet(ModelMap model) {
		List<BaoCaoBaiViet> list = baoCaoBaiVietService.findAll();

		model.addAttribute("baocaobaiviet", list);
		return "admin/manage/quanlybaiviet";
	}

	@GetMapping(value = {"/admin/chophep/{mabaocao}", "/admin/chitiet/chophep/{mabaocao}"})
	public String chophepBaiViet(@PathVariable(value = "mabaocao") int mabaocao, Model model) {
		baoCaoBaiVietService.deleteById(mabaocao);
		
		List<BaoCaoBaiViet> list = baoCaoBaiVietService.findAll();
		model.addAttribute("baocaobaiviet", list);
		
		return "redirect:/admin/dsbaocaobaiviet";
	}
	
	@GetMapping("/admin/chitiet/{mabaocao}")
	public String chitietbaiviet(@PathVariable(value = "mabaocao") int mabaocao, Model model) {
		Optional<BaoCaoBaiViet> optBaocaobaiviet = baoCaoBaiVietService.findById(mabaocao);
		BaoCaoBaiViet baocaobaiviet = optBaocaobaiviet.get();

		model.addAttribute("baocaobaiviet", baocaobaiviet);
		return "admin/manage/chitietbaiviet";
	}
	
	/*
	 * @GetMapping("/admin/*") public String inforadmin(ModelMap model) {
	 * Optional<TaiKhoan> Opttaikhoan = taiKhoanService.findById("admin"); TaiKhoan
	 * tk = Opttaikhoan.get();
	 * 
	 * model.addAttribute("taikhoan", tk);
	 * 
	 * return "/admin/fragments/leftbar"; }
	 */
}
