package hcmute.alohcmute.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.Nhom;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Integer>{
	@Query("SELECT b FROM BaiViet b WHERE b.taiKhoan.taiKhoan = :taiKhoanUsername")
    List<BaiViet> findAllBaiVietByUsername(String taiKhoanUsername);
	@Query(value = "SELECT COUNT(*) FROM tuong_tac WHERE ma_bai_viet = :maBaiViet", nativeQuery = true)
    int countLikesByBaiVietId(@Param("maBaiViet") int maBaiViet);
	
	@Query(value = "SELECT COUNT(*) FROM binh_luan WHERE ma_bai_viet = :maBaiViet", nativeQuery = true)
    int countCommentsByBaiVietId(@Param("maBaiViet") int maBaiViet);
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END FROM tai_khoan_nhom tkn WHERE tkn.tai_khoan = :username AND tkn.ma_nhom = :maNhom", nativeQuery = true)
    boolean isUserInGroup(@Param("username") String username, @Param("maNhom") Integer maNhom);
	List<BaiViet> findBynhom(Nhom Nhom);
	BaiViet findBymaBaiViet(int maBaiViet);
}
