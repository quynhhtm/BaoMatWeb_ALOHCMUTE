package hcmute.alohcmute.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.CuocHoiThoai;
import hcmute.alohcmute.entities.TaiKhoan;

@Repository
public interface CuocHoiThoaiRepository extends JpaRepository<CuocHoiThoai, Integer> {

	@Query("SELECT c "
			+ "FROM CuocHoiThoai c "
			+ "WHERE c.taiKhoans = :taiKhoans")
	Optional<TaiKhoan> findCuocHoiThoaiByTaiKhoan(Set<TaiKhoan> taiKhoans);
}
