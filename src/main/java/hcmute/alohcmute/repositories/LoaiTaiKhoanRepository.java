package hcmute.alohcmute.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.LoaiTaiKhoan;

@Repository
public interface LoaiTaiKhoanRepository extends JpaRepository<LoaiTaiKhoan, Integer>{
	@Query("SELECT l "
			+ "FROM LoaiTaiKhoan l "
			+ "WHERE l.tenLoai = :tenLoai")
	LoaiTaiKhoan getByTenLoai(@Param("tenLoai") String tenLoai);
	
	@Query("SELECT CASE "
			+ "WHEN COUNT(e) > 0 "
			+ "THEN true ELSE false END "
			+ "FROM LoaiTaiKhoan e "
			+ "WHERE e.tenLoai = :tenLoai")
	LoaiTaiKhoan existLoaiTaiKhoan(@Param("tenLoai") String tenLoai);
}