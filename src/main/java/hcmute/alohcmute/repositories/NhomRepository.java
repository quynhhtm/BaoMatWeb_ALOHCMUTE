package hcmute.alohcmute.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.Nhom;

@Repository
public interface NhomRepository extends JpaRepository<Nhom, Integer>{
	public Nhom findBymaNhom(int maNhom);
    List<Nhom> findByTenNhomContainingIgnoreCase(String searchString);
}
