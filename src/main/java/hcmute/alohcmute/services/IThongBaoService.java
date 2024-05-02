package hcmute.alohcmute.services;

import java.util.List;


import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;


public interface IThongBaoService {
	List<ThongBao>findAll();
	List<ThongBao> findByTaiKhoan(TaiKhoan taiKhoan);
	<S extends ThongBao> S save(S entity);
}
