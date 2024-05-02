package hcmute.alohcmute.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.BinhLuan;


@Repository
public interface CommentRepositories extends JpaRepository<BinhLuan, Integer>{
	long countByBaiViet_MaBaiViet(int maBaiViet);
}
