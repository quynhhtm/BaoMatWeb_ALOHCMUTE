package hcmute.alohcmute.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.LoaiTaiKhoan;
import hcmute.alohcmute.repositories.LoaiTaiKhoanRepository;

@Service
public class RoleServiceImpl implements IRoleService {
	@Autowired
	private LoaiTaiKhoanRepository loaiTaiKhoanRepository;

	@Override
	public LoaiTaiKhoan getByTenLoai(String tenLoai) {
		return loaiTaiKhoanRepository.getByTenLoai(tenLoai);
	}

	@Override
	public LoaiTaiKhoan existLoaiTaiKhoan(String tenLoai) {
		return loaiTaiKhoanRepository.existLoaiTaiKhoan(tenLoai);
	}

	@Override
	public <S extends LoaiTaiKhoan> S save(S entity) {
		return loaiTaiKhoanRepository.save(entity);
	}
	
}
