package hcmute.alohcmute.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hcmute.alohcmute.dtos.TinNhanDto;
import hcmute.alohcmute.entities.CuocHoiThoai;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.TinNhan;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.ICuocHoiThoaiService;
import hcmute.alohcmute.services.ITaiKhoanService;
import hcmute.alohcmute.services.ITinNhanService;

@Controller
public class TinNhanController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ITaiKhoanService taiKhoanService;
	@Autowired
	private ICuocHoiThoaiService cuocHoiThoaiService;
	@Autowired
	private ITinNhanService tinNhanService;
	
	@GetMapping("/user/chat")
	public String getDanhSachChat(Model model) {
		TaiKhoan taiKhoan = SecurityUtil.getMyUser();
		
		List<TaiKhoan> danhsachbanbes = taiKhoanService.findTaiKhoanFollowersByUsername(taiKhoan.getTaiKhoan());
		List<TaiKhoan> taiKhoanBiTheoDois = taiKhoanService.findTaiKhoanTheoDoisByUsername(taiKhoan.getTaiKhoan());
		danhsachbanbes.retainAll(taiKhoanBiTheoDois);
		
		model.addAttribute("taikhoan", taiKhoan);
		model.addAttribute("danhsachbanbes", danhsachbanbes);

		return "user/chat/danhsachchat";
	}

	@GetMapping("/user/chat/{username}")
	public String getCuocHoiThoai(@PathVariable(value = "username") String username, Model model) {
		TaiKhoan taiKhoan = SecurityUtil.getMyUser();
		TaiKhoan banbe = taiKhoanService.findBytaiKhoan(username);
		
		if(banbe == null) {
			return "user/chat/danhsachchat";
		}
		
		List<CuocHoiThoai> cuocHoiThoaiHienTai = new ArrayList<>();
		List<TinNhan> tinnhans = null;
		
		for (CuocHoiThoai cuocHoiThoai : taiKhoan.getCuocHoiThoais()) {
			if (cuocHoiThoai.getTaiKhoans() != null && cuocHoiThoai.getTaiKhoans().size() == 2) {
				for (TaiKhoan banBe : cuocHoiThoai.getTaiKhoans()) {
					if (banBe.getTaiKhoan().equals(username)) {
						cuocHoiThoaiHienTai.add(cuocHoiThoai);
					}
				}
			}
		}
		
		if (cuocHoiThoaiHienTai.size() == 0) {
			CuocHoiThoai cuocHoiThoai = new CuocHoiThoai();
			cuocHoiThoai
					.setTenCuocHoiThoai("Cuộc trò chuyện giữa: " + taiKhoan.getTaiKhoan() + ", " + banbe.getTaiKhoan());

			Set<TinNhan> tinNhans = new HashSet<>();
			Set<TaiKhoan> taiKhoans = new HashSet<>();
			taiKhoans.add(taiKhoan);
			taiKhoans.add(banbe);

			cuocHoiThoai.setTaiKhoans(taiKhoans);
			cuocHoiThoai.setTinNhans(tinNhans);

			cuocHoiThoaiService.save(cuocHoiThoai);
			cuocHoiThoaiHienTai.add(cuocHoiThoai);

			Set<CuocHoiThoai> cuocHoiThoaiTaiKhoans = taiKhoan.getCuocHoiThoais();
			Set<CuocHoiThoai> cuocHoiThoaiBanBes = banbe.getCuocHoiThoais();

			cuocHoiThoaiTaiKhoans.add(cuocHoiThoai);
			cuocHoiThoaiBanBes.add(cuocHoiThoai);

			taiKhoan.setCuocHoiThoais(cuocHoiThoaiTaiKhoans);
			banbe.setCuocHoiThoais(cuocHoiThoaiBanBes);

			taiKhoanService.save(taiKhoan);
			taiKhoanService.save(banbe);
		}
		else {
			int macuochoithoai = cuocHoiThoaiHienTai.get(0).getMaCuocHoiThoai();
			tinnhans = tinNhanService.findByMaCuocHoiThoai(macuochoithoai);
		}

		model.addAttribute("taikhoan", taiKhoan);
		model.addAttribute("banbe", banbe);
		model.addAttribute("cuochoithoai", cuocHoiThoaiHienTai.get(0));
		model.addAttribute("tinnhans", tinnhans);
		
		return "user/chat/cuochoithoai";
	}

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload TinNhanDto tinNhanDto, @Header("topic") String topic) {
		messagingTemplate.convertAndSend("/chat/" + topic, tinNhanDto);
		
		String noiDungChu = tinNhanDto.getNoiDungChu();
		String noiDungHinhAnh = tinNhanDto.getNoiDungChu();
		
		String thoiGianGui = tinNhanDto.getThoiGianGuiTinNhan().replace(" ", "T");
		LocalDateTime thoiGianGuiTinNhan = LocalDateTime.parse(thoiGianGui);
		
		String tenCuocHoiThoaiString = tinNhanDto.getTenCuocHoiThoai();
		int maCuocHoiThoai = tinNhanDto.getMaCuocHoiThoai();
		CuocHoiThoai cuocHoiThoai = cuocHoiThoaiService.findById(maCuocHoiThoai).get();
		
		String nickname = tinNhanDto.getNickname();
		String username = tinNhanDto.getUsername();
		TaiKhoan taiKhoan = taiKhoanService.findBytaiKhoan(username);
		
		TinNhan tinNhan = new TinNhan();
		tinNhan.setNoiDungChu(noiDungChu);
		tinNhan.setNoiDungHinhAnh(noiDungHinhAnh);
		tinNhan.setThoiGianGuiTinNhan(thoiGianGuiTinNhan);
		tinNhan.setTaiKhoan(taiKhoan);
		tinNhan.setCuocHoiThoai(cuocHoiThoai);
		tinNhanService.save(tinNhan);
	}

}
