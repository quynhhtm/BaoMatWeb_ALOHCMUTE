package hcmute.alohcmute.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.BaoCaoBaiViet;

@Repository
public interface BaoCaoBaiVietRepository extends JpaRepository<BaoCaoBaiViet, Integer>{

}
