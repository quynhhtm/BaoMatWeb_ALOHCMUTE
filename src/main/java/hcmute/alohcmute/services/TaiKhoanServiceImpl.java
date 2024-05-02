package hcmute.alohcmute.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;
import hcmute.alohcmute.models.TaiKhoanModel;
import hcmute.alohcmute.repositories.TaiKhoanRepository;

@Service
public class TaiKhoanServiceImpl implements ITaiKhoanService{
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	
	@Autowired
	IThongBaoService tbSer;

	@Override
	public <S extends TaiKhoan> S save(S entity) {
		return taiKhoanRepository.save(entity);
	}
	
	@Override
	public List<TaiKhoan> findAll() {
		return taiKhoanRepository.findAll();
	}

	@Override
	public Optional<TaiKhoan> findById(String id) {
		return taiKhoanRepository.findById(id);
	}
	
	@Override
	public boolean existsById(String id) {
		return taiKhoanRepository.existsById(id);
	}

	@Override
	public long count() {
		return taiKhoanRepository.count();
	}
	
	@Override
	public void deleteById(String id) {
		taiKhoanRepository.deleteById(id);
	}

	@Override
	public List<TaiKhoan> findTaiKhoanTheoDoisByUsername(String taiKhoanUsername) {
		return taiKhoanRepository.findTaiKhoanTheoDoisByUsername(taiKhoanUsername);
	}
	
	@Override
	public List<TaiKhoan> findTaiKhoanFollowersByUsername(String taiKhoanUsername) {
		return taiKhoanRepository.findTaiKhoanFollowersByUsername(taiKhoanUsername);
	}
	
	@Override
	@Transactional
	public void unfollow(TaiKhoan taiKhoanTheoDoi, TaiKhoan taiKhoanBiTheoDoi) {
		taiKhoanTheoDoi.getTaiKhoanTheoDois().remove(taiKhoanBiTheoDoi);
		ThongBao tb = new ThongBao();
		tb.setNgay(LocalDate.now());
		tb.setNoiDung(taiKhoanTheoDoi.getTaiKhoan() + " vừa hủy theo dõi bạn trong ALOHCMUTE");
		tb.setTaiKhoan(taiKhoanBiTheoDoi);
		tb.setThoiGian(LocalTime.now());
		tb.setLinkThongBao("/user/follow");
		tbSer.save(tb);
		taiKhoanRepository.save(taiKhoanTheoDoi);
	}
	@Override
	public void follow(TaiKhoan taiKhoan, TaiKhoan taiKhoanTheoDoi) {
		taiKhoan.getTaiKhoanTheoDois().add(taiKhoanTheoDoi);
		ThongBao tb = new ThongBao();
		tb.setNgay(LocalDate.now());
		tb.setNoiDung(taiKhoan.getTaiKhoan() + " vừa theo dõi bạn trong ALOHCMUTE");
		tb.setTaiKhoan(taiKhoanTheoDoi);
		tb.setThoiGian(LocalTime.now());
		tb.setLinkThongBao("/user/follow");
		tbSer.save(tb);
		taiKhoanRepository.save(taiKhoan);
	}
	
	@Override
	public <S extends TaiKhoan> Optional<S> findOne(Example<S> example) {
		return taiKhoanRepository.findOne(example);
	}
	
	@Override
	public TaiKhoan findBytaiKhoan(String username) {
		return taiKhoanRepository.findOneBytaiKhoan(username);
	}
	
	@Override
	public Map<TaiKhoan, Integer> NguoiTheoDoiChung(String username)
	{
		TaiKhoan ChinhChu = taiKhoanRepository.findOneBytaiKhoan(username);
		Map<TaiKhoan, Integer> result = new HashMap<>();
		List<TaiKhoan> NguoiTheoDoi = taiKhoanRepository.findTaiKhoanTheoDoisByUsername(username);
		for (TaiKhoan taiKhoan : NguoiTheoDoi) {
			List<TaiKhoan> NguoiTheoDoiChung = taiKhoanRepository.findTaiKhoanFollowersByUsername(taiKhoan.getTaiKhoan());
			for (TaiKhoan taiKhoan2 : NguoiTheoDoiChung) {
				if (!NguoiTheoDoi.contains(taiKhoan2) && !taiKhoan2.equals(ChinhChu))
				if (result.containsKey(taiKhoan2)) {
					int a = result.get(taiKhoan2);
					a=a+1;
					result.put(taiKhoan2, a);
				}
				else {
					result.put(taiKhoan2, 1);
				}	
			}
		}
		
		return result;
	}

	@Override
	public List<TaiKhoan> findTop5TaiKhoanFollowersByUsername(String taiKhoanUsername) {
		return taiKhoanRepository.findTop5TaiKhoanFollowersByUsername(taiKhoanUsername);
	}

	@Override
	public int countTaiKhoanFollowersByUsername(String taiKhoanUsername) {
		return taiKhoanRepository.countTaiKhoanFollowersByUsername(taiKhoanUsername);
	}

	@Override
	public List<TaiKhoan> findTop5TaiKhoanTheoDoisByUsername(String taiKhoanUsername) {
		return taiKhoanRepository.findTop5TaiKhoanTheoDoisByUsername(taiKhoanUsername);
	}

	@Override
	public Page<TaiKhoan> getTaiKhoanTheoDoiByPage(String taikhoan, int page, int pageSize) {
		List<TaiKhoan> listTaiKhoanTheoDoi = findTaiKhoanFollowersByUsername(taikhoan);
		int fromIndex = page * pageSize;
        int toIndex = Math.min((page + 1) * pageSize, listTaiKhoanTheoDoi.size());

        if (fromIndex > toIndex) {
            // Trang yêu cầu không hợp lệ
            return new PageImpl<>(List.of()); // Trả về trang trống
        }

        List<TaiKhoan> taiKhooanOnPage = listTaiKhoanTheoDoi.subList(fromIndex, toIndex);
        return new PageImpl<>(taiKhooanOnPage, PageRequest.of(page, pageSize), listTaiKhoanTheoDoi.size());
    }

	@Override
	public List<TaiKhoanModel> findTaiKhoanByKeyword(String keyword, String username) {
		List<TaiKhoan> list = taiKhoanRepository.findTaiKhoanByKeyword(keyword);
		List<TaiKhoanModel> listModel = new ArrayList<>();
		for (TaiKhoan tk : list) {
			TaiKhoanModel model = new TaiKhoanModel();
			model.setTaiKhoan(tk.getTaiKhoan()); 
			model.setMatKhau(tk.getMatKhau()); 
			model.setEnable(tk.isEnable()); 
			model.setHoTen(tk.getHoTen()); 
			model.setNickName(tk.getNickName());
			model.setEmail(tk.getEmail()); 
			model.setGioiTinh(tk.getGioiTinh()); 
			model.setSDT(tk.getSDT()); 
			model.setAvatarURl(tk.getAvatarURl());
			model.setSoLuongNguoiTheoDoi(demTaiKhoanTheoDoi(tk.getTaiKhoan()));
			model.setIsfollowed(checkFollowed(username, tk.getTaiKhoan()));
			listModel.add(model);
		}
		return listModel;
	}

	@Override
	public long demTaiKhoanTheoDoi(String username) {
        return taiKhoanRepository.findTaiKhoanFollowersByUsername(username).size();
	}

	@Override
	public boolean checkFollowed(String taiKhoanTheoDoi, String taiKhoanDuocTheoDoi) {
		TaiKhoan taiKhoan = taiKhoanRepository.getById(taiKhoanTheoDoi);
		if (taiKhoan.getTaiKhoanTheoDois().contains(taiKhoanRepository.getById(taiKhoanDuocTheoDoi)))
			return true;
		return false;
	}
}