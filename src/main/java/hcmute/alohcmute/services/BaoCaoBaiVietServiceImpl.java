package hcmute.alohcmute.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.BaoCaoBaiViet;
import hcmute.alohcmute.repositories.BaiVietRepository;
import hcmute.alohcmute.repositories.BaoCaoBaiVietRepository;

@Service
public class BaoCaoBaiVietServiceImpl implements IBaoCaoBaiVietService{

	@Autowired
	BaoCaoBaiVietRepository baoCaoBaiVietRepository;
	
	@Autowired
	BaiVietRepository baiVietRepository;

	@Override
	public <S extends BaoCaoBaiViet> S save(S entity) {
		return baoCaoBaiVietRepository.save(entity);
	}

	@Override
	public List<BaoCaoBaiViet> findAll() {
		return baoCaoBaiVietRepository.findAll();
	}

	@Override
	public <S extends BaoCaoBaiViet> Page<S> findAll(Example<S> example, Pageable pageable) {
		return baoCaoBaiVietRepository.findAll(example, pageable);
	}

	@Override
	public Optional<BaoCaoBaiViet> findById(Integer id) {
		return baoCaoBaiVietRepository.findById(id);
	}

	@Override
	public long count() {
		return baoCaoBaiVietRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		baoCaoBaiVietRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		baoCaoBaiVietRepository.deleteAll();
	}
	
	@Override
	public void deleteAllBaoCaoBaiVietByMaBaiViet(int maBaiViet) {
		BaiViet baiViet = baiVietRepository.getById(maBaiViet);
		for(BaoCaoBaiViet baoCao: baiViet.getBaoCaoBaiViets()) {
			baoCaoBaiVietRepository.deleteById(baoCao.getMaBaoCao());
		}
	}
	
	@Override
	public List<BaoCaoBaiViet> findBaoCaoBaiVietByMaBaiViet(int maBV) {
		List<BaoCaoBaiViet> listAll = findAll();
		List<BaoCaoBaiViet> list = new ArrayList<>();
		for (BaoCaoBaiViet baoCao : listAll) {
			if (baoCao.getBaiViet() != null && baoCao.getBaiViet().getMaBaiViet() == maBV)
				list.add(baoCao);
		}
		return list;
	}
	
	@Override
    public BaoCaoBaiViet reportPost(int postId, String reason) {
        BaiViet baiViet = baiVietRepository.findById(postId).orElse(null);
        if (baiViet == null) {
            // Handle the case where the post doesn't exist, maybe throw an exception
            return null;
        }

        BaoCaoBaiViet report = new BaoCaoBaiViet();
        report.setBaiViet(baiViet);
        report.setNoiDungBaoCao(reason);
        report.setNgay(LocalDate.now());
        report.setThoiGian(LocalTime.now());
        return baoCaoBaiVietRepository.save(report);
    }
	
}
