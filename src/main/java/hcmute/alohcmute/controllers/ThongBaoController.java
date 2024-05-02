package hcmute.alohcmute.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.ITaiKhoanService;
import hcmute.alohcmute.services.IThongBaoService;

@ControllerAdvice
public class ThongBaoController {

	@Autowired
	IThongBaoService thongbaoService;
	@Autowired
	ITaiKhoanService taiKhoanService;
	@ModelAttribute("thongbaos")
    public List<ThongBao> getThongBaos() {
		List<ThongBao> newlist = new ArrayList<>();
		try {
			
			TaiKhoan taiKhoan = taiKhoanService.findBytaiKhoan(SecurityUtil.getMyUser().getTaiKhoan());
			List<ThongBao> listtb = thongbaoService.findByTaiKhoan(taiKhoan);
			 Collections.reverse(listtb);
			 return listtb;
		} catch (Exception e) {
			return newlist; 
		}
      
       
		
    }
	@ModelAttribute("taiKhoan")
    public TaiKhoan getTaiKhoan() {
		
		try {
			 return taiKhoanService.findBytaiKhoan(SecurityUtil.getMyUser().getTaiKhoan()); 
			
		} catch (Exception e) {
			return new TaiKhoan();
		}
       
    }
}

