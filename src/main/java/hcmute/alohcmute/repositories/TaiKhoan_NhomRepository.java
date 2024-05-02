package hcmute.alohcmute.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import hcmute.alohcmute.entities.TaiKhoan_Nhom;


public interface TaiKhoan_NhomRepository extends JpaRepository<TaiKhoan_Nhom, Integer>{
	@Query("SELECT tn FROM TaiKhoan_Nhom tn WHERE tn.id.taiKhoan = :taiKhoan")
    List<TaiKhoan_Nhom> findNhomTaiKhoan(String taiKhoan);
    @Query("SELECT tn FROM TaiKhoan_Nhom tn WHERE tn.id.taiKhoan = :taiKhoan and tn.id.maNhom=:maNhom")
    TaiKhoan_Nhom findOne(String taiKhoan, int maNhom);
    @Query("SELECT tn FROM TaiKhoan_Nhom tn WHERE tn.id.maNhom=:maNhom")
    List<TaiKhoan_Nhom> findTaiKhoanByNhom(int maNhom);
    
}
