package hcmute.alohcmute.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.BaoCaoBaiViet;
import hcmute.alohcmute.entities.BinhLuan;
import hcmute.alohcmute.entities.CheDo;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.models.BaiVietModel;
import hcmute.alohcmute.models.TaiKhoanModel;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.IBaiVietService;
import hcmute.alohcmute.services.IBaoCaoBaiVietService;
import hcmute.alohcmute.services.ICheDoService;
import hcmute.alohcmute.services.ICommentService;
import hcmute.alohcmute.services.ITaiKhoanService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("trangcanhan")
public class TrangCaNhanController {

	@Autowired
	ITaiKhoanService taiKhoanService;

	@Autowired
	IBaiVietService baiVietService;

	@Autowired
	ICommentService commentService;

	@Autowired
	IBaoCaoBaiVietService baoCaoBaiVietService;
	
	@Autowired
	ICheDoService cheDoService;

	@GetMapping("thongtintaikhoan/{taikhoan}")
	public String thongTinTaiKhoan(ModelMap model, @PathVariable("taikhoan") String taikhoan,
			@RequestParam(name = "pageNoFriends", defaultValue = "1") Integer pageNoFriends,
			@RequestParam(name = "pageNoTimeline", defaultValue = "1") Integer pageNoTimeline,
			@RequestParam(name = "tab", defaultValue = "timeline") String tab) {
		Optional<TaiKhoan> optTaiKhoan = taiKhoanService.findById(taikhoan);
		TaiKhoanModel taiKhoanModel = new TaiKhoanModel();

		model.addAttribute("chutaikhoan", SecurityUtil.getMyUser().getTaiKhoan());

		// Danh sach tai khoan theo doi theo trang
		Page<TaiKhoan> taiKhoanTheoDoi = taiKhoanService.getTaiKhoanTheoDoiByPage(taikhoan, pageNoFriends - 1, 8);
		model.addAttribute("totalPageFriends", taiKhoanTheoDoi.getTotalPages());
		model.addAttribute("currentPageFriends", pageNoFriends);
		model.addAttribute("taikhoantheodoi", taiKhoanTheoDoi);

		// Danh sach dang tai khoan theo doi
		List<TaiKhoan> taiKhoanDangTheoDoi = new ArrayList<>(taiKhoanService.findTaiKhoanTheoDoisByUsername(taikhoan));
		model.addAttribute("taikhoandangtheodoi", taiKhoanDangTheoDoi);

		List<TaiKhoan> top5TaiKhoanDuocTheoDoi = new ArrayList<>(
				taiKhoanService.findTop5TaiKhoanFollowersByUsername(taikhoan));
		model.addAttribute("top5taikhoanduoctheodoi", top5TaiKhoanDuocTheoDoi);

		// So tai khoan theo doi
		int countTaiKhoanDuocTheoDoi = taiKhoanService.countTaiKhoanFollowersByUsername(taikhoan);
		model.addAttribute("sotaikhoantheodoi", countTaiKhoanDuocTheoDoi);

		// Danh sach tai khoan dang theo doi
		List<TaiKhoan> top5TaiKhoanDangTheoDoi = new ArrayList<>(
				taiKhoanService.findTop5TaiKhoanTheoDoisByUsername(taikhoan));
		model.addAttribute("top5taikhoandangtheodoi", top5TaiKhoanDangTheoDoi);

		// So tai khoan dang theo doi
		model.addAttribute("sotaikhoandangtheodoi", taiKhoanService.findTaiKhoanTheoDoisByUsername(taikhoan).size());

		// Danh sach bai viet
		List<BaiViet> listBaiViet = baiVietService.findAllBaiVietByUsername(taikhoan);
		List<BaiVietModel> listBaiVietModel = new ArrayList<BaiVietModel>();
		// Danh sach bai viet theo trang
		Page<BaiViet> pageBaiViet = baiVietService.getBaiVietByPage(taikhoan, pageNoTimeline - 1, 3);
		model.addAttribute("totalPageTimeline", pageBaiViet.getTotalPages());
		model.addAttribute("currentPageTimeline", pageNoTimeline);
		model.addAttribute("listbaiviet", listBaiViet);
		for (BaiViet baiViet : pageBaiViet) {
			long soComment = commentService.countBinhLuanByMaBaiViet(baiViet.getMaBaiViet());
			long soTuongTac = baiVietService.demSoTuongTac(baiViet.getMaBaiViet());
			BaiVietModel baiVietModel = new BaiVietModel();
			BeanUtils.copyProperties(baiViet, baiVietModel);
			baiVietModel.setSoComment(soComment);
			baiVietModel.setSoTuongTac(soTuongTac);
			listBaiVietModel.add(baiVietModel);
		}

		model.addAttribute("baivietmodel", listBaiVietModel);
		model.addAttribute("listBaiViet", listBaiViet);

		// Thong tin tai khoan
		if (optTaiKhoan.isPresent()) {
			TaiKhoan entity = optTaiKhoan.get();
			BeanUtils.copyProperties(entity, taiKhoanModel);

			model.addAttribute("taikhoan", taiKhoanModel);

			if (tab.equals("timeline")) {
				model.addAttribute("tab", "timeline");
			} else if (tab.equals("about")) {
				model.addAttribute("tab", "about");
			} else if (tab.equals("photos")) {
				model.addAttribute("tab", "photos");
			} else if (tab.equals("friends")) {
				model.addAttribute("tab", "friends");
			}

			if (taikhoan.equals(SecurityUtil.getMyUser().getTaiKhoan())) {
				return "user/trangcanhan/trangcanhanchutaikhoan";
			} else {
				List<TaiKhoan> list = new ArrayList<>(
						taiKhoanService.findTaiKhoanTheoDoisByUsername(SecurityUtil.getMyUser().getTaiKhoan()));
				if (list.contains(entity)) {
					model.addAttribute("follows", true);
				} else {
					model.addAttribute("follows", false);
				}
				return "user/trangcanhan/trangcanhanbanbe";
			}

		}
		model.addAttribute("message", "Error!!");
		return "";
	}

	@PostMapping("update/{user}")
	public ModelAndView saveOrUpdate(ModelMap model, @PathVariable("user") String taikhoan,
			@Valid @ModelAttribute("taikhoan") TaiKhoanModel taiKhoanModel, BindingResult result) {

		if (result.hasErrors()) {
			return new ModelAndView("user/trangcanhan/trangcanhanchutaikhoan");
		}

		Optional<TaiKhoan> optTaiKhoan = taiKhoanService.findById(taikhoan);
		TaiKhoan tk = optTaiKhoan.get();
		tk.setHoTen(taiKhoanModel.getHoTen());
		tk.setGioiTinh(taiKhoanModel.getGioiTinh());
		tk.setEmail(taiKhoanModel.getEmail());
		tk.setSDT(taiKhoanModel.getSDT());

		taiKhoanService.save(tk);

		String url = "redirect:/trangcanhan/thongtintaikhoan/" + taikhoan + "?tab=about";
		return new ModelAndView(url);

	}

	@GetMapping("/deletebaiviet/{taikhoan}/{mabaiviet}")
	public ModelAndView delete(ModelMap model, @PathVariable("mabaiviet") int mabaiviet,
			@PathVariable("taikhoan") String taikhoan) {

		BaiViet baiViet = baiVietService.findById(mabaiviet).get();
		baiViet.setEnable(false);
		baiVietService.save(baiViet);

		String url = "redirect:/trangcanhan/thongtintaikhoan/" + taikhoan;
		return new ModelAndView(url, model);
	}

	@GetMapping("/deleteTaiKhoanTheoDoi/{taikhoanbitheodoi}/{taikhoantheodoi}")
	public ModelAndView deleteTaiKhoanTheoDoi(ModelMap model,
			@PathVariable("taikhoanbitheodoi") String taikhoanbitheodoi,
			@PathVariable("taikhoantheodoi") String taikhoantheodoi) {

		taiKhoanService.unfollow(taiKhoanService.findById(taikhoantheodoi).get(),
				taiKhoanService.findById(taikhoanbitheodoi).get());

		String url = "redirect:/trangcanhan/thongtintaikhoan/" + taikhoanbitheodoi + "?tab=friends";
		return new ModelAndView(url, model);
	}

	@GetMapping("/theoDoiTaiKhoan/{taikhoanbitheodoi}/{taikhoantheodoi}")
	public ModelAndView theoDoiTaiKhoan(ModelMap model, @PathVariable("taikhoanbitheodoi") String taikhoanbitheodoi,
			@PathVariable("taikhoantheodoi") String taikhoantheodoi) {

		taiKhoanService.follow(taiKhoanService.findById(taikhoantheodoi).get(),
				taiKhoanService.findById(taikhoanbitheodoi).get());

		String url = "redirect:/trangcanhan/thongtintaikhoan/" + taikhoanbitheodoi;
		return new ModelAndView(url, model);
	}

	@GetMapping("/deleteTaiKhoanDangTheoDoi/{taikhoanbitheodoi}/{taikhoantheodoi}")
	public ModelAndView deleteTaiKhoanDangTheoDoi(ModelMap model,
			@PathVariable("taikhoanbitheodoi") String taikhoanbitheodoi,

			@PathVariable("taikhoantheodoi") String taikhoantheodoi) {

		taiKhoanService.unfollow(taiKhoanService.findById(taikhoantheodoi).get(),
				taiKhoanService.findById(taikhoanbitheodoi).get());

		String url = "redirect:/trangcanhan/thongtintaikhoan/" + taikhoantheodoi;
		return new ModelAndView(url, model);
	}

	@PostMapping("update/baiviet/{mabaiviet}")
	public ModelAndView updateBaiViet(@RequestParam("noiDungChu") String noiDungChu, @PathVariable("mabaiviet") int mabaiviet) {

		BaiViet baiViet = baiVietService.findById(mabaiviet).get();
		baiViet.setNoiDungChu(noiDungChu + "#000000");

		baiVietService.save(baiViet);

		String url = "redirect:/trangcanhan/thongtintaikhoan/" + SecurityUtil.getMyUser().getTaiKhoan();
		return new ModelAndView(url);

	}

	@GetMapping("/update/chedobaiviet/{machedo}/{mabaiviet}")
	public ModelAndView updateCheDoBaiViet(ModelMap model,
			@PathVariable("machedo") int machedo,
			@PathVariable("mabaiviet") int mabaiviet 
			) {
		
		BaiViet baiViet = baiVietService.findById(mabaiviet).get();
		CheDo cheDo = cheDoService.findById(machedo).get();
		baiViet.setCheDoNhom(cheDo);
		
		baiVietService.save(baiViet);
		
		String url = "redirect:/trangcanhan/thongtintaikhoan/" + SecurityUtil.getMyUser().getTaiKhoan();
		return new ModelAndView(url, model);
	}
}
