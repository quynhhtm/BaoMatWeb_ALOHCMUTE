package hcmute.alohcmute.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.BinhLuan;
import hcmute.alohcmute.repositories.BaiVietRepository;
import hcmute.alohcmute.repositories.CommentRepositories;

@Service
public class CommentSerrviceImpl implements ICommentService{

	@Autowired
	CommentRepositories commentRepository;
	
	@Autowired
	BaiVietRepository baiVietRepository;

	@Override
	public <S extends BinhLuan> S save(S entity) {
		return commentRepository.save(entity);
	}

	@Override
	public <S extends BinhLuan> List<S> saveAll(Iterable<S> entities) {
		return commentRepository.saveAll(entities);
	}

	@Override
	public List<BinhLuan> findAll() {
		Sort sort = Sort.by(Sort.Direction.ASC, "ngay");
        return commentRepository.findAll(sort);
	}

	@Override
	public Page<BinhLuan> findAll(Pageable pageable) {
		return commentRepository.findAll(pageable);
	}

	@Override
	public Optional<BinhLuan> findById(Integer id) {
		return commentRepository.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return commentRepository.existsById(id);
	}

	@Override
	public <S extends BinhLuan> boolean exists(Example<S> example) {
		return commentRepository.exists(example);
	}

	@Override
	public BinhLuan getOne(Integer id) {
		return commentRepository.getOne(id);
	}

	@Override
	public long count() {
		return commentRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		commentRepository.deleteById(id);
	}

	@Override
	public BinhLuan getById(Integer id) {
		return commentRepository.getById(id);
	}

	@Override
	public void delete(BinhLuan entity) {
		commentRepository.delete(entity);
	}

	@Override
	public BinhLuan getReferenceById(Integer id) {
		return commentRepository.getReferenceById(id);
	}

	@Override
	public void deleteAll() {
		commentRepository.deleteAll();
	}

	@Override
	public List<BinhLuan> findCommentByMaBaiViet(int maBV) {
		BaiViet baiViet = baiVietRepository.getById(maBV);
		List<BinhLuan> list = new ArrayList<>(baiViet.getBinhLuans());
        List<BinhLuan> listBinhLuan = list.stream().sorted((bl1, bl2) -> {
			int dateCompare = bl2.getNgay().compareTo(bl1.getNgay());
			if (dateCompare == 0) {
				return bl2.getThoiGian().compareTo(bl1.getThoiGian());
			}
			return dateCompare;
		}).collect(Collectors.toList());
        return listBinhLuan;
	}

	@Override
	public long countBinhLuanByMaBaiViet(int maBaiViet) {
		return commentRepository.countByBaiViet_MaBaiViet(maBaiViet);
	}

	@Override
	public void deleteAllBinhLuanByMaBaiViet(int maBaiViet) {
		BaiViet baiViet = baiVietRepository.getById(maBaiViet);
		for(BinhLuan binhLuan: baiViet.getBinhLuans()) {
			commentRepository.deleteById(binhLuan.getMaBinhLuan());
		}
	}
	
	
}
