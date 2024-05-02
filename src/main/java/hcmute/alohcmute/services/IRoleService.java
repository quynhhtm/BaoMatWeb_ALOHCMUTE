package hcmute.alohcmute.services;

import hcmute.alohcmute.entities.LoaiTaiKhoan;

public interface IRoleService {

	<S extends LoaiTaiKhoan> S save(S entity);

	LoaiTaiKhoan existLoaiTaiKhoan(String tenLoai);

	LoaiTaiKhoan getByTenLoai(String tenLoai);

}
