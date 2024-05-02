package hcmute.alohcmute.services;

import java.util.List;
import java.util.Optional;

import hcmute.alohcmute.entities.TinNhan;

public interface ITinNhanService  {

	long count();
	
	<S extends TinNhan> S save(S entity);

	Optional<TinNhan> findById(Integer id);

	List<TinNhan> findAll();

	List<TinNhan> findByMaCuocHoiThoai(int maCuocHoiThoai);
}
