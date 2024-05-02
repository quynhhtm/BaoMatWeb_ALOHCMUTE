package hcmute.alohcmute.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.entities.CheDo;
import hcmute.alohcmute.repositories.CheDoRepository;

@Service
public class CheDoServiceImpl implements ICheDoService{
	@Autowired
	CheDoRepository cheDoRepo;
	
	@Override
	public Optional<CheDo> findByID(int id) {
		return cheDoRepo.findById(id);
	}
	
	@Override
	public CheDo findByCheDo(String username) {
		return cheDoRepo.findOneBytenCheDo(username);
	}
	@Override
	public Optional<CheDo> findById(Integer id) {
		return cheDoRepo.findById(id);
	}
}