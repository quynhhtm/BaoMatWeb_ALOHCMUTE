package hcmute.alohcmute.controllers;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.CheDo;
import hcmute.alohcmute.entities.Nhom;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.IBaiVietService;
import hcmute.alohcmute.services.ICheDoService;
import hcmute.alohcmute.services.INhomService;
import hcmute.alohcmute.services.ITaiKhoanService;
import hcmute.alohcmute.services.IThongBaoService;
import jakarta.servlet.ServletContext;

@Controller
@RequestMapping("user/dangbai")
public class DangBaiController{

	@Autowired
	IBaiVietService baivietSer;
	@Autowired
	ITaiKhoanService taikhoanSer;
	@Autowired
	ICheDoService chedoSer;
	@Autowired
	IThongBaoService tbSer;
	@Autowired
	INhomService nhomSer;
	@Autowired
	ServletContext app;
	String username ;
	@RequestMapping("")
	public String list(ModelMap model)
	{
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		TaiKhoan taikhoan=taikhoanSer.findBytaiKhoan(username);
		model.addAttribute("taikhoan",taikhoan);
		List<TaiKhoan> aa = taikhoanSer.findTaiKhoanFollowersByUsername(username);
		List<String> kq = new ArrayList<>();
		for (TaiKhoan ds : aa) {
			kq.add(ds.getTaiKhoan());
		}
		model.addAttribute("listbanbe",kq);
		return "user/dangbai/dangbai.html";
	}
	@PostMapping("add")
	public String add(ModelMap model,@RequestParam("noidungchu") String noidungchu,@RequestParam("manhom") String manhom,@RequestParam("privacy") String cdo,@RequestParam("file") MultipartFile noidunghinhanh,@RequestParam("color") String color)
	{
		this.username = SecurityUtil.getMyUser().getTaiKhoan();
		if(color.equals(""))
			color="#000000";
		
		BaiViet baiviet=new BaiViet();
		if (noidungchu.equals("")&&noidunghinhanh.isEmpty()) {
			return "redirect:/user/dangbai";
		}
		else
		{
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
			
			String tenchedo="";
			TaiKhoan taikhoan=taikhoanSer.findBytaiKhoan(username);
			baiviet.setNoiDungChu(noidungchu+color);
			String linkanh= filename;
			baiviet.setNoiDungHinhAnh(linkanh);
			baiviet.setTaiKhoan(taikhoan);
			System.out.println(taikhoan.getHoTen());
			System.out.println(cdo);
			if(cdo.equals("Public"))
			{
				tenchedo="Công Khai";
			}
			else if(cdo.equals("Follower"))
			{
				tenchedo="Người Theo Dõi";
			}
			else if(cdo.equals("Private"))
			{
				tenchedo="Riêng Tư";
			}
			if (!manhom.equals(""))
			{
				int grID = Integer.parseInt(manhom);
				Nhom nhom = nhomSer.findBymaNhom(grID);
				baiviet.setNhom(nhom);
				CheDo chedo=chedoSer.findByID(4).get();
				baiviet.setCheDoNhom(chedo);
			}
			else {
				CheDo chedo=chedoSer.findByCheDo(tenchedo);
				baiviet.setCheDoNhom(chedo);
			}
			baiviet.setNgay(LocalDate.now());
			baiviet.setEnable(true);
			baiviet.setThoiGian(LocalTime.now());
			baivietSer.save(baiviet);
		}
		if (!manhom.equals("")) {
			return "redirect:/user/group/viewgroup?groupID="+manhom;
		}
		else return "redirect:/user/newfeed";
	}
	
}
