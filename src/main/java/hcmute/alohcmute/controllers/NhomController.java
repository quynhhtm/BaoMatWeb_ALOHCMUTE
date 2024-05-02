package hcmute.alohcmute.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.Nhom;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.TaiKhoan_Nhom;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.IBaiVietService;
import hcmute.alohcmute.services.ICheDoService;
import hcmute.alohcmute.services.INewFeedService;
import hcmute.alohcmute.services.INhomService;
import hcmute.alohcmute.services.ITaiKhoanService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user/group")
public class NhomController {

	@Autowired(required = true)
	ITaiKhoanService tkSer;
	@Autowired(required = true)
	INhomService NhomSer;
	@Autowired
	ICheDoService cheDoSer;
	@Autowired
	IBaiVietService baiVietSer;
	@Autowired
	INewFeedService iNewFeed;
	@Autowired
	ServletContext app;
	
	String username;
	@GetMapping("")
	public String NhomCuaBan(ModelMap model) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		// TaiKhoan tk = tkSer.findBytaiKhoan(username);
		// Set<Nhom> setnhom =tk.getNhom();
		// List<Nhom> listnhom = new ArrayList<>(setnhom);
		Map<Nhom, Boolean> List_Nhom = new HashMap<>();
		List<TaiKhoan_Nhom> tkNhom = NhomSer.findNhomTaiKhoan(username);
		for (TaiKhoan_Nhom taiKhoan_Nhom : tkNhom) {
			Nhom nhom = NhomSer.findBymaNhom(taiKhoan_Nhom.getId().getMaNhom());
			List_Nhom.put(nhom, taiKhoan_Nhom.isAccept());
		}
		// model.addAttribute("listnhom",listnhom);
		model.addAttribute("List_Nhom", List_Nhom);
		model.addAttribute("username", username);
		return "/user/nhom/nhomcuaban.html";
	}

	@GetMapping("viewgroup")
	public String TheoDoi(ModelMap model, @RequestParam("groupID") String groupID) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		
		
		int groupid = Integer.parseInt(groupID);
		Nhom nhom = NhomSer.findBymaNhom(groupid);
		model.addAttribute("Nhom", nhom);
		model.addAttribute("username", username);
//		List<BaiViet> listBaiViet = new ArrayList<>(nhom.getBaiViets());
		List<BaiViet> listBaiViet = baiVietSer.findBymaNhom(nhom);
		Collections.reverse(listBaiViet);
		List<TaiKhoan> thanhvien = new ArrayList<>();
		TaiKhoan taikhoanTemp = new TaiKhoan();
		List<TaiKhoan_Nhom> tkNhom = NhomSer.findTaiKhoanByNhom(groupid);
		Map<Integer, Boolean> likedPosts = new HashMap<>();
		Map<Integer, Integer> postLikesCount = new HashMap<>();
		Map<Integer, Integer> postCommentsCount = new HashMap<>();
		for (TaiKhoan_Nhom taikhoanNhom : tkNhom) {
			taikhoanTemp = tkSer.findBytaiKhoan(taikhoanNhom.getId().getTaiKhoan());
			if (taikhoanNhom.isAccept()
					&& !taikhoanNhom.getId().getTaiKhoan().equals(nhom.getTaiKhoanTruongNhom().getTaiKhoan()))
				thanhvien.add(taikhoanTemp);
		}
		for (BaiViet post : listBaiViet) {
			likedPosts.put(post.getMaBaiViet(), iNewFeed.checkIfLiked(post.getMaBaiViet(), username));
			postLikesCount.put(post.getMaBaiViet(), iNewFeed.getLikeCount(post.getMaBaiViet()));
			postCommentsCount.put(post.getMaBaiViet(), iNewFeed.getCommentCount(post.getMaBaiViet()));
		}
		TaiKhoan taikhoanuser = tkSer.findBytaiKhoan(username);
		if (thanhvien.contains(taikhoanuser) || nhom.getTaiKhoanTruongNhom().getTaiKhoan().equals(username))
			model.addAttribute("check", false);
		else
			model.addAttribute("check", true);
		model.addAttribute("thanhvien", thanhvien);
		model.addAttribute("listBaiViet", listBaiViet);
		model.addAttribute("likedPosts", likedPosts);
		model.addAttribute("postLikesCount", postLikesCount);
		model.addAttribute("postCommentsCount", postCommentsCount);
		
		TaiKhoan tk = tkSer.findBytaiKhoan(username);
		if(!thanhvien.contains(tk)&&nhom.getCheDoNhom().getMaCheDo()==3&&!nhom.getTaiKhoanTruongNhom().getTaiKhoan().equals(username))
		{
			return "redirect:/user/group/error";
		}
		
		return "user/nhom/nhom.html";

	}

	@GetMapping("searchgroup")
	public String TimKiemNhom(ModelMap model, @RequestParam("groupName") String groupName) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		List<Nhom> listnhomTimKiem = NhomSer.findByTenNhomContainingIgnoreCase(groupName);
		boolean empty = false;
		if (listnhomTimKiem.isEmpty())
			empty = true;
		Map<Nhom, Integer> List_Nhom_TimKiem = new HashMap<>();
		List<TaiKhoan_Nhom> tkNhom = NhomSer.findNhomTaiKhoan(username);
		for (Nhom nhom : listnhomTimKiem) {
			List_Nhom_TimKiem.put(nhom, 0);
		}
		for (TaiKhoan_Nhom taiKhoan_Nhom : tkNhom) {
			Nhom nhom = NhomSer.findBymaNhom(taiKhoan_Nhom.getId().getMaNhom());
			if (taiKhoan_Nhom.getId().getTaiKhoan().equals(username) && List_Nhom_TimKiem.containsKey(nhom))
				if (taiKhoan_Nhom.isAccept())
					List_Nhom_TimKiem.put(nhom, 2);
				else
					List_Nhom_TimKiem.put(nhom, 1);
		}
		model.addAttribute("List_Nhom_TimKiem", List_Nhom_TimKiem);
		NhomCuaBan(model);
		model.addAttribute("tenGroup", groupName);
		model.addAttribute("empty", empty);
		model.addAttribute("username", username);
		return "user/nhom/nhomcuaban.html";
	}

	@GetMapping("joingroup")
	public String VaoNhom(ModelMap model, @RequestParam("groupID") String groupID) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		TaiKhoan tk = tkSer.findBytaiKhoan(username);
		int GrID = Integer.parseInt(groupID);
		Nhom nhom = NhomSer.findBymaNhom(GrID);
		NhomSer.sendRequestToGroup(tk, nhom);
		model.addAttribute("mes", "Thành công");
		return NhomCuaBan(model);
	}

	@GetMapping("outgroup")
	public String RoiNhom(ModelMap model, @RequestParam("groupID") String groupID,
			@RequestParam("ThanhVien") String ThanhVien) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		int GrID = Integer.parseInt(groupID);
		if (ThanhVien != "") {
			Nhom nhom = NhomSer.findBymaNhom(GrID);
			nhom.setTaiKhoanTruongNhom(tkSer.findBytaiKhoan(ThanhVien));
			NhomSer.Save(nhom);
		}
		NhomSer.leaveGroup(username, GrID);
		NhomCuaBan(model);
		return "user/nhom/nhomcuaban.html";
	}

	@GetMapping("editgroup")
	public String ChinhSuaNhom(ModelMap model, @RequestParam("groupID") String groupID) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		int GrID = Integer.parseInt(groupID);
		Nhom nhom = NhomSer.findBymaNhom(GrID);
		if (!username.equals(nhom.getTaiKhoanTruongNhom().getTaiKhoan()))
			return "redirect:/user/group/errorPage";
		model.addAttribute("nhom", nhom);
		List<TaiKhoan_Nhom> tkNhom = NhomSer.findTaiKhoanByNhom(GrID);
		List<TaiKhoan> thanhvien = new ArrayList<>();
		List<TaiKhoan> yeucau = new ArrayList<>();
		TaiKhoan taikhoanTemp = new TaiKhoan();
		for (TaiKhoan_Nhom taikhoanNhom : tkNhom) {
			taikhoanTemp = tkSer.findBytaiKhoan(taikhoanNhom.getId().getTaiKhoan());
			if (taikhoanNhom.isAccept() && !taikhoanNhom.getId().getTaiKhoan().equals(username))
				thanhvien.add(taikhoanTemp);
			else if (!taikhoanNhom.isAccept() && !taikhoanNhom.getId().getTaiKhoan().equals(username))
				yeucau.add(taikhoanTemp);
		}

		model.addAttribute("thanhvien", thanhvien);
		model.addAttribute("yeucau", yeucau);
		return "user/nhom/quantrinhom.html";
	}

	@GetMapping("removeMember")
	public String XoaThanhVien(ModelMap model, @RequestParam("groupID") String groupID,
			@RequestParam("username") String usernameRemove, HttpServletRequest request) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		int grID = Integer.parseInt(groupID);
		String usernameConvert = usernameRemove.substring(1, usernameRemove.length() - 1);
		NhomSer.leaveGroup(usernameConvert, grID);
		String referer = request.getHeader("referer");
		return "redirect:" + (referer != null ? referer : "/defaultPath");

	}

	@GetMapping("/errorPage")
	@ResponseBody
	public String errorPage() {
		return "<html><body><h1>Bạn không là nhóm trưởng không thể truy cập trang này</h1></body></html>";
	}
	
	@GetMapping("/error")
	@ResponseBody
	public String errorPage1() {
		return "<html><body><h1>Bạn không có quyền truy cập nhóm này</h1></body></html>";
	}

	@GetMapping("addMember")
	public String ThemThanhVien(ModelMap model, @RequestParam("groupID") String groupID,
			@RequestParam("username") String usernameAdd, HttpServletRequest request) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		int grID = Integer.parseInt(groupID);
		String usernameConvert = usernameAdd.substring(1, usernameAdd.length() - 1);
		NhomSer.addMember(usernameConvert, grID);
		String referer = request.getHeader("referer");
		return "redirect:" + (referer != null ? referer : "/defaultPath");

	}

	@PostMapping("saveEdit")
	public String LuuThayDoi(ModelMap model, @RequestParam("groupName") String groupName,
			@RequestParam("CheDo") String CheDo, @RequestParam("groupID") String groupID, HttpServletRequest request,
			@RequestParam("file") MultipartFile noidunghinhanh) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		int grID = Integer.parseInt(groupID);
		Nhom nhom = NhomSer.findBymaNhom(grID);
		int CheDoID;
		if (CheDo.equals("public"))
			CheDoID = 1;
		else
			CheDoID = 3;
		nhom.setCheDoNhom(cheDoSer.findByID(CheDoID).get());
		nhom.setTenNhom(groupName);

//		String usernameConvert = usernameAdd.substring(1,usernameAdd.length()-1);
//		NhomSer.addMember(usernameConvert, grID);
		String referer = request.getHeader("referer");
		String filename = "";

		if (!noidunghinhanh.isEmpty()) {
			filename="";
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
				int extensionSTT = noidunghinhanh.getOriginalFilename().indexOf(".");
				String extension = noidunghinhanh.getOriginalFilename().substring(extensionSTT);
				// Generate random integers in range 0 to 999
				String stt = Integer.toString(LocalDateTime.now().getYear())
						+ Integer.toString(LocalDateTime.now().getDayOfYear())
						+ Integer.toString(LocalDateTime.now().getHour())
						+ Integer.toString(LocalDateTime.now().getMinute())
						+ Integer.toString(LocalDateTime.now().getSecond());
				filename = stt + extension;

				File serverFile = new File(uploadRootDir.getAbsoluteFile() + File.separator + filename);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(noidunghinhanh.getBytes());
				stream.close();

			} catch (Exception e) {
			}
			String linkanh = filename;
			nhom.setNhomURL(linkanh);
		}
		
		NhomSer.Save(nhom);
		return "redirect:" + (referer != null ? referer : "/defaultPath");
	}

	@GetMapping("post")
	public String DangBai(ModelMap model, @RequestParam("groupID") String groupID) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		model.addAttribute("nhom", groupID);
		TaiKhoan taikhoan = tkSer.findBytaiKhoan(username);
		model.addAttribute("taikhoan", taikhoan);
		List<TaiKhoan> aa = tkSer.findTaiKhoanFollowersByUsername(username);
		List<String> kq = new ArrayList<>();
		for (TaiKhoan ds : aa) {
			kq.add(ds.getTaiKhoan());
		}
		model.addAttribute("listbanbe", kq);
		return "user/dangbai/dangbai.html";
	}

	@PostMapping("creategroup")
	public String TaoNhom(ModelMap model, @RequestParam("groupName") String groupName,
			@RequestParam("CheDo") String CheDo) {
		String username = SecurityUtil.getMyUser().getTaiKhoan();
		boolean Success = NhomSer.createGroup(username, groupName, CheDo);
		if (Success)
			model.addAttribute("success", "Thành công");
		else
			model.addAttribute("success", "Thất bại");
		return NhomCuaBan(model);
	}
}
