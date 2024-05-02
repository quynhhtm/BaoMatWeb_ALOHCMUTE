package hcmute.alohcmute.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.CuocHoiThoai;
import hcmute.alohcmute.repositories.CuocHoiThoaiRepository;

@Service
public class CuocHoiThoaiServiceImpl implements ICuocHoiThoaiService{
	
	@Autowired
	private CuocHoiThoaiRepository cuocHoiThoaiRepository;

	@Override
	public <S extends CuocHoiThoai> S save(S entity) {
		return cuocHoiThoaiRepository.save(entity);
	}
	
	@Override
	public Optional<CuocHoiThoai> findById(Integer id) {
		return cuocHoiThoaiRepository.findById(id);
	}
}
