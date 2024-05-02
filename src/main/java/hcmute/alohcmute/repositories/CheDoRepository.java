package hcmute.alohcmute.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.CheDo;

@Repository
public interface CheDoRepository extends JpaRepository<CheDo, Integer>{

	CheDo findOneBytenCheDo(String username);

}
