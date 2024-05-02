package hcmute.alohcmute.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.TinNhan;

@Repository
public interface TinNhanRepository extends JpaRepository<TinNhan, Integer>{

	@Query("SELECT t "
			+ "FROM TinNhan t "
			+ "JOIN t.cuocHoiThoai c "
			+ "WHERE c.maCuocHoiThoai = :maCuocHoiThoai "
			+ "ORDER BY t.thoiGianGuiTinNhan")
	List<TinNhan> findByMaCuocHoiThoai(int maCuocHoiThoai);
	
}
