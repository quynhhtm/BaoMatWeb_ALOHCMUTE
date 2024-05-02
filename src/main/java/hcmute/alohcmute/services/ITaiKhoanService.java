package hcmute.alohcmute.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.models.TaiKhoanModel;

public interface ITaiKhoanService {

	Map<TaiKhoan, Integer> NguoiTheoDoiChung(String username);

	TaiKhoan findBytaiKhoan(String username);

	<S extends TaiKhoan> Optional<S> findOne(Example<S> example);

	void follow(TaiKhoan taiKhoan, TaiKhoan taiKhoanTheoDoi);

	void unfollow(TaiKhoan taiKhoanTheoDoi, TaiKhoan taiKhoanBiTheoDoi);

	List<TaiKhoan> findTaiKhoanFollowersByUsername(String taiKhoanUsername);

	List<TaiKhoan> findTaiKhoanTheoDoisByUsername(String taiKhoanUsername);

	void deleteById(String id);

	long count();

	boolean existsById(String id);

	Optional<TaiKhoan> findById(String id);

	List<TaiKhoan> findAll();

	<S extends TaiKhoan> S save(S entity);
	
	List<TaiKhoanModel> findTaiKhoanByKeyword(String keyword, String username);
	
	long demTaiKhoanTheoDoi(String username);
	
	boolean checkFollowed(String taiKhoanTheoDoi, String taiKhoanDuocTheoDoi);

	List<TaiKhoan> findTop5TaiKhoanFollowersByUsername(String taiKhoanUsername);

	int countTaiKhoanFollowersByUsername(String taiKhoanUsername);

	List<TaiKhoan> findTop5TaiKhoanTheoDoisByUsername(String taiKhoanUsername);
	
	Page<TaiKhoan> getTaiKhoanTheoDoiByPage(String taikhoan, int page, int pageSize);
	
}
