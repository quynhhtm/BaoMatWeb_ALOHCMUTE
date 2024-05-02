package hcmute.alohcmute.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
	List<ThongBao> findByTaiKhoan(TaiKhoan taiKhoan);
}
