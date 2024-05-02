package hcmute.alohcmute.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.TinNhan;
import hcmute.alohcmute.repositories.TinNhanRepository;

@Service
public class TinNhanServiceImpl implements ITinNhanService {

	@Autowired
	private TinNhanRepository tinNhanRepository;
	
	@Override
	public <S extends TinNhan> S save(S entity) {
		return tinNhanRepository.save(entity);
	}

	@Override
	public List<TinNhan> findAll() {
		return tinNhanRepository.findAll();
	}

	@Override
	public Optional<TinNhan> findById(Integer id) {
		return tinNhanRepository.findById(id);
	}

	@Override
	public long count() {
		return tinNhanRepository.count();
	}

	@Override
	public List<TinNhan> findByMaCuocHoiThoai(int maCuocHoiThoai) {
		return tinNhanRepository.findByMaCuocHoiThoai(maCuocHoiThoai);
	}
}
