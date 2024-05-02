package hcmute.alohcmute.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.BinhLuan;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.BaiVietServiceImpl;
import hcmute.alohcmute.services.CommentSerrviceImpl;
import hcmute.alohcmute.services.IBaiVietService;
import hcmute.alohcmute.services.ICommentService;
import hcmute.alohcmute.services.ITaiKhoanService;
import hcmute.alohcmute.services.IThongBaoService;
import hcmute.alohcmute.services.TaiKhoanServiceImpl;
import hcmute.alohcmute.services.ThongBaoServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class CommentController {
	@Autowired
	ICommentService commentService = new CommentSerrviceImpl();
	@Autowired
    IBaiVietService baiVietService = new BaiVietServiceImpl();
	@Autowired
    ITaiKhoanService taiKhoanService = new TaiKhoanServiceImpl();
	
	@Autowired
	IThongBaoService thongBaoService = new ThongBaoServiceImpl();
	
	@Autowired
	ServletContext app;
	
	String username;

	@GetMapping("/comment/{baiVietId}")
	public String reviewComment(ModelMap model, @PathVariable(value = "baiVietId") int id) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		List<BinhLuan> comments = commentService.findCommentByMaBaiViet(id);
		long soLuongBinhLuan = commentService.countBinhLuanByMaBaiViet(id);
		long demSoTuongTac = baiVietService.demSoTuongTac(id);
		BaiViet baiViet = baiVietService.getById(id);
		model.addAttribute("isLiked", baiVietService.checkLiked(id, username));
		model.addAttribute("soLuongBinhLuan", soLuongBinhLuan);
		model.addAttribute("soLike", demSoTuongTac);
		model.addAttribute("comments", comments);
		model.addAttribute("baiViet", baiViet);
		BinhLuan binhLuan = new BinhLuan(); 
		model.addAttribute("binhLuan", binhLuan);		
		List<TaiKhoan> aa = taiKhoanService.findTaiKhoanFollowersByUsername(username);
		List<String> kq = new ArrayList<>();
		for (TaiKhoan ds : aa) {
			kq.add(ds.getTaiKhoan());
		}
		model.addAttribute("listbanbe",kq);
		return "user/comment/comment";
	}
	
	@PostMapping("/comment/{baiVietId}")
	public String addComment(@Valid BinhLuan binhLuan, BindingResult result, ModelMap model, @PathVariable(value = "baiVietId") int id,@RequestParam("file") MultipartFile noidunghinhanh) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		if (result.hasErrors()) {
			return "user/comment/comment";
		}
		
		List<BinhLuan> comments = commentService.findCommentByMaBaiViet(id);
		model.addAttribute("comments", comments);
		if (binhLuan.getNoiDungChu() != "" && binhLuan.getNoiDungChu() != null) {
			binhLuan.setNgay(java.time.LocalDate.now());
			binhLuan.setThoiGian(java.time.LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
			binhLuan.setBaiViet(baiVietService.getById(id));
			binhLuan.setTaiKhoan(taiKhoanService.findBytaiKhoan(username));
			
			TaiKhoan taikhoan=taiKhoanService.findBytaiKhoan(username);
			
			
			Pattern pattern = Pattern.compile("@\\w+");

	        // Create a matcher for the input string
	        Matcher matcher = pattern.matcher(binhLuan.getNoiDungChu());

	        // Find and print all occurrences
	        while (matcher.find()) {
	            String match = matcher.group();
	            ThongBao tb = new ThongBao();
	            tb.setNgay(java.time.LocalDate.now());
	            String NoiDung = taikhoan.getHoTen()+" đã nhắc đến bạn trong một bình luận";
	            tb.setNoiDung(NoiDung);
	            tb.setLinkThongBao("/user/comment/"+ binhLuan.getBaiViet().getMaBaiViet());
	            String user=match.substring(1);
	            tb.setTaiKhoan(taiKhoanService.findBytaiKhoan(user));
	            tb.setThoiGian(java.time.LocalTime.now());
	            thongBaoService.save(tb);
	        }
	        String filename="";
			String projectRoot = System.getProperty("user.dir");

			// Specify the name of the folder you want to create inside the project root
			String folderName = "uploads";

			// Combine the project root and folder name to get the full path
			String fullPath = projectRoot + File.separator + folderName;

			// Create the folder if it doesn't exist
			File uploadRootDir = new File(fullPath);
			if (!uploadRootDir.exists()) {
			    uploadRootDir.mkdirs();
			}
			try {
				int extensionSTT=noidunghinhanh.getOriginalFilename().indexOf(".");
				String extension=noidunghinhanh.getOriginalFilename().substring(extensionSTT);
		        // Generate random integers in range 0 to 999
		        String stt = Integer.toString(LocalDateTime.now().getYear()) +Integer.toString(LocalDateTime.now().getDayOfYear())+Integer.toString(LocalDateTime.now().getHour())+Integer.toString(LocalDateTime.now().getMinute())+Integer.toString(LocalDateTime.now().getSecond());
				filename = stt+extension;
				
				File serverFile = new File(uploadRootDir.getAbsoluteFile() + File.separator + filename);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(noidunghinhanh.getBytes());
				stream.close();

			} catch (Exception e) {	
			}
			
			
			
			String linkanh= filename;
			binhLuan.setNoiDungHinhAnh(linkanh);

			commentService.save(binhLuan);
		}
		return "redirect:{baiVietId}";
	}
	
	@GetMapping("/comment/{baiVietId}/delete/{commentId}")
	public String deleteComment(@PathVariable("commentId") int commentId, @PathVariable("baiVietId") int baiVietId, Model model) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		commentService.deleteById(commentId);
        return "redirect:/user/comment/{baiVietId}";
    }
	
	@PostMapping("/comment/{baiVietId}/update")
	public String updateComment(@Valid BinhLuan binhLuan, BindingResult result, ModelMap model,  @PathVariable("baiVietId") int baiVietId) {
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		BinhLuan binhLuanOld = commentService.getById(binhLuan.getMaBinhLuan());
		if (binhLuan.getNoiDungChu() != binhLuanOld.getNoiDungChu()) {
			binhLuanOld.setNoiDungChu(binhLuan.getNoiDungChu());
			binhLuanOld.setNgay(java.time.LocalDate.now());
			binhLuanOld.setThoiGian(java.time.LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
			commentService.save(binhLuanOld);
		}
        return "redirect:/user/comment/{baiVietId}";
    }
	
}