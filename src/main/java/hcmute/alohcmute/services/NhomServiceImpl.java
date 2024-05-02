package hcmute.alohcmute.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.Nhom;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.TaiKhoan_Nhom;
import hcmute.alohcmute.entities.TaiKhoan_Nhom_Id;
import hcmute.alohcmute.entities.ThongBao;
import hcmute.alohcmute.repositories.CheDoRepository;
import hcmute.alohcmute.repositories.NhomRepository;
import hcmute.alohcmute.repositories.TaiKhoanRepository;
import hcmute.alohcmute.repositories.TaiKhoan_NhomRepository;

@Service
public class NhomServiceImpl implements INhomService {

	@Autowired
	NhomRepository nhomRepo;
	@Autowired
	TaiKhoan_NhomRepository tkNhomRepo;
	@Autowired
	CheDoRepository cheDoRepo;
	@Autowired
	TaiKhoanRepository tkRepo;
	@Autowired
	IThongBaoService tbSer;

	public NhomServiceImpl(NhomRepository nhomRepo) {
		this.nhomRepo = nhomRepo;
	}

	@Override
	public Nhom findBymaNhom(int maNhom) {
		return nhomRepo.findBymaNhom(maNhom);
	}

	@Override
	public List<Nhom> findByTenNhomContainingIgnoreCase(String searchString) {
		return nhomRepo.findByTenNhomContainingIgnoreCase(searchString);
	}

	@Override
	public void sendRequestToGroup(TaiKhoan tk, Nhom gr) {
		TaiKhoan_Nhom taikhoanNhom = new TaiKhoan_Nhom();
		taikhoanNhom.setId(new TaiKhoan_Nhom_Id(gr.getMaNhom(), tk.getTaiKhoan()));
		taikhoanNhom.setAccept(false);
		tkNhomRepo.save(taikhoanNhom);
		ThongBao tb = new ThongBao();
		tb.setNgay(LocalDate.now());
		tb.setNoiDung(tk.getTaiKhoan() + " vừa gửi yêu cầu vào nhóm " + gr.getTenNhom());
		tb.setTaiKhoan(gr.getTaiKhoanTruongNhom());
		tb.setThoiGian(LocalTime.now());
		tb.setLinkThongBao("/user/group/editgroup?groupID=" + gr.getMaNhom());
		tbSer.save(tb);
	}

	@Override
	public void leaveGroup(String tk, int nhom) {
		TaiKhoan_Nhom taikhoanNhom = tkNhomRepo.findOne(tk, nhom);
		ThongBao tb = new ThongBao();
		tb.setNgay(LocalDate.now());
		tb.setNoiDung("Bạn không còn là thành viên của nhóm " + this.findBymaNhom(nhom).getTenNhom());
		tb.setTaiKhoan(tkRepo.findOneBytaiKhoan(tk));
		tb.setThoiGian(LocalTime.now());
		tbSer.save(tb);
		tkNhomRepo.delete(taikhoanNhom);
	}

	@Override
	public void addMember(String tk, int nhom) {
		TaiKhoan_Nhom taikhoanNhom = tkNhomRepo.findOne(tk, nhom);
		taikhoanNhom.setAccept(true);
		tkNhomRepo.save(taikhoanNhom);
		ThongBao tb = new ThongBao();
		tb.setNgay(LocalDate.now());
		tb.setNoiDung("Bạn đã được chấp nhận vào nhóm " + this.findBymaNhom(nhom).getTenNhom());
		tb.setTaiKhoan(tkRepo.findOneBytaiKhoan(tk));
		tb.setThoiGian(LocalTime.now());
		tb.setLinkThongBao("/user/group/viewgroup?groupID=" + nhom);
		tbSer.save(tb);
	}

	@Override
	public List<TaiKhoan_Nhom> findNhomTaiKhoan(String taiKhoan) {
		return tkNhomRepo.findNhomTaiKhoan(taiKhoan);
	}

	@Override
	public List<TaiKhoan_Nhom> findAllTK_Nhom() {
		return tkNhomRepo.findAll();
	}

	@Override
	public List<TaiKhoan_Nhom> findTaiKhoanByNhom(int maNhom) {
		return tkNhomRepo.findTaiKhoanByNhom(maNhom);
	}

	@Override
	public void Save(Nhom nhom) {
		nhomRepo.save(nhom);
	}

	@Override
	public boolean createGroup(String username, String groupName, String CheDo) {
		List<Nhom> listNhom = nhomRepo.findByTenNhomContainingIgnoreCase(groupName);
		for (Nhom nhom : listNhom) {
			if (nhom.getTenNhom().equals(groupName))
				return false;
		}
		Nhom nhomMoi = new Nhom();
		hcmute.alohcmute.entities.CheDo chedo = new hcmute.alohcmute.entities.CheDo();
		if (CheDo.equals("public"))
			chedo = cheDoRepo.findById(1).get();
		else
			chedo = cheDoRepo.findById(3).get();
		nhomMoi.setCheDoNhom(chedo);
		nhomMoi.setTenNhom(groupName);
		nhomMoi.setTaiKhoanTruongNhom(tkRepo.findOneBytaiKhoan(username));
		nhomMoi.setNgayThanhLap(LocalDateTime.now());
		nhomMoi.setNhomURL("https://png.pngtree.com/element_our/20200610/ourlarge/pngtree-social-networking-image_2239654.jpg");
		nhomRepo.save(nhomMoi);
		this.sendRequestToGroup(tkRepo.findOneBytaiKhoan(username), nhomMoi);
		this.addMember(username, nhomMoi.getMaNhom());
		return true;
	}

}
