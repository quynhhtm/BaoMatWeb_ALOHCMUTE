package hcmute.alohcmute.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.alohcmute.entities.TaiKhoan;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String>{
	@Query("SELECT t "
			+ "FROM TaiKhoan t "
			+ "WHERE t.taiKhoan = :taiKhoan")
	Optional<TaiKhoan> findByTaiKhoan(String taiKhoan);
	
	@Query("SELECT t "
			+ "FROM TaiKhoan t "
			+ "WHERE t.email = :email")
	Optional<TaiKhoan> findByEmail(String email);
	
	@Query("SELECT t "
			+ "FROM TaiKhoan t "
			+ "WHERE t.email = :email "
			+ "or t.taiKhoan = :taiKhoan")
	Optional<TaiKhoan> findByTaiKhoanOrEmail(String taiKhoan, String email);
	
	@Query("SELECT t "
			+ "FROM TaiKhoan t "
			+ "WHERE t.token = :token")
	Optional<TaiKhoan> findByToken(String token);
	
	@Query("SELECT t "
			+ "FROM TaiKhoan t "
			+ "WHERE t.email = :email "
			+ "and t.token = :token")
	Optional<TaiKhoan> findByTokenAndEmail(String email, String token);
	
	@Query("SELECT CASE "
			+ "WHEN COUNT(e) > 0 "
			+ "THEN true ELSE false END "
			+ "FROM TaiKhoan e "
			+ "WHERE e.email = :email")
	boolean existByEmail(String email);
	
	@Query("SELECT CASE "
			+ "WHEN COUNT(e) > 0 "
			+ "THEN true ELSE false END "
			+ "FROM TaiKhoan e "
			+ "WHERE e.taiKhoan = :taiKhoan")
	boolean existByTaiKhoan(String taiKhoan);
	
	@Query("SELECT t FROM TaiKhoan t JOIN t.taiKhoanTheoDois u WHERE u.taiKhoan = :taiKhoanUsername")
	List<TaiKhoan> findTaiKhoanFollowersByUsername(String taiKhoanUsername);
	
	@Query("SELECT t.taiKhoanTheoDois FROM TaiKhoan t WHERE t.taiKhoan = :taiKhoanUsername")
    List<TaiKhoan> findTaiKhoanTheoDoisByUsername(String taiKhoanUsername);
	
	TaiKhoan findOneBytaiKhoan(String username);
	
	@Query("SELECT t FROM TaiKhoan t JOIN t.taiKhoanTheoDois u WHERE u.taiKhoan = :taiKhoanUsername ORDER BY t.id DESC LIMIT 6")
	List<TaiKhoan> findTop5TaiKhoanFollowersByUsername(String taiKhoanUsername);
	
	@Query("SELECT count(t) FROM TaiKhoan t JOIN t.taiKhoanTheoDois u WHERE u.taiKhoan = :taiKhoanUsername")
	int countTaiKhoanFollowersByUsername(String taiKhoanUsername);
	
	@Query("SELECT t.taiKhoanTheoDois FROM TaiKhoan t WHERE t.taiKhoan = :taiKhoanUsername ORDER BY t.id DESC LIMIT 4")
    List<TaiKhoan> findTop5TaiKhoanTheoDoisByUsername(String taiKhoanUsername);
	
	@Query("SELECT t FROM TaiKhoan t WHERE t.taiKhoan LIKE %:keyword% or t.hoTen LIKE %:keyword%")
	List<TaiKhoan> findTaiKhoanByKeyword(String keyword);
}
