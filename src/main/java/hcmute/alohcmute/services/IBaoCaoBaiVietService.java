package hcmute.alohcmute.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import hcmute.alohcmute.entities.BaoCaoBaiViet;

public interface IBaoCaoBaiVietService {

	void deleteAll();

	void deleteById(Integer id);

	long count();

	Optional<BaoCaoBaiViet> findById(Integer id);

	<S extends BaoCaoBaiViet> Page<S> findAll(Example<S> example, Pageable pageable);

	List<BaoCaoBaiViet> findAll();

	<S extends BaoCaoBaiViet> S save(S entity);
	
	void deleteAllBaoCaoBaiVietByMaBaiViet(int maBaiViet);
	
	List<BaoCaoBaiViet> findBaoCaoBaiVietByMaBaiViet(int maBV);

	BaoCaoBaiViet reportPost(int postId, String reason);
}
