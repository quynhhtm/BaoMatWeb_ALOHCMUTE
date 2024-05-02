package hcmute.alohcmute.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;
import hcmute.alohcmute.repositories.ThongBaoRepository;

@Service
public class ThongBaoServiceImpl implements IThongBaoService{

	@Autowired
	ThongBaoRepository thongbaoRepository;
	
	@Override
	public List<ThongBao> findAll() {
		return thongbaoRepository.findAll();
	}

	@Override
	public List<ThongBao> findByTaiKhoan(TaiKhoan taiKhoan) {
		return thongbaoRepository.findByTaiKhoan(taiKhoan);
	}

	@Override
	public <S extends ThongBao> S save(S entity) {
		return thongbaoRepository.save(entity);
	}

}
