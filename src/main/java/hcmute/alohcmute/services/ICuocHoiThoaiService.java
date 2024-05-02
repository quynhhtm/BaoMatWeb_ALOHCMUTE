package hcmute.alohcmute.services;

import java.util.Optional;

import hcmute.alohcmute.entities.CuocHoiThoai;

public interface ICuocHoiThoaiService {

	<S extends CuocHoiThoai> S save(S entity);

	Optional<CuocHoiThoai> findById(Integer id);

}
