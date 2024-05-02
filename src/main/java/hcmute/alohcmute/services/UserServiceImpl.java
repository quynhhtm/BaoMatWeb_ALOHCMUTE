package hcmute.alohcmute.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hcmute.alohcmute.dtos.TaiKhoanDto;
import hcmute.alohcmute.entities.LoaiTaiKhoan;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.exceptions.UserAlreadyExistsException;
import hcmute.alohcmute.repositories.LoaiTaiKhoanRepository;
import hcmute.alohcmute.repositories.TaiKhoanRepository;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
    private TaiKhoanRepository taiKhoanRepository;
	@Autowired
	private LoaiTaiKhoanRepository loaiTaiKhoanRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<TaiKhoan> getTaiKhoans() {
		return taiKhoanRepository.findAll();
	}
	
    @Override
    public Optional<TaiKhoan> findByTaiKhoanOrEmail(String taiKhoan, String email) {
        return taiKhoanRepository.findByTaiKhoanOrEmail(taiKhoan, email);
    }
    
	@Override
	public Optional<TaiKhoan> findByTokenAndEmail(String email, String token) {
		return taiKhoanRepository.findByTokenAndEmail(email, token);
	}

	@Override
	public Optional<TaiKhoan> findByTaiKhoan(String taiKhoan) {
		return taiKhoanRepository.findByTaiKhoan(taiKhoan);
	}

	@Override
	public Optional<TaiKhoan> findByEmail(String email) {
		return taiKhoanRepository.findByEmail(email);
	}
    
    @Override
    public Optional<TaiKhoan> findByToken(String token) {
        return taiKhoanRepository.findByToken(token);
    }
    
    @Override
    public TaiKhoan saveTaiKhoan(TaiKhoanDto taiKhoanDto) {
        Optional<TaiKhoan> existUser = this.findByTaiKhoanOrEmail(taiKhoanDto.getUsername(), taiKhoanDto.getEmail());
        if(existUser.isPresent()) {
        	throw new UserAlreadyExistsException("User with username or email already");
        }
        
        TaiKhoan user = new TaiKhoan();
        user.setTaiKhoan(taiKhoanDto.getUsername());
        user.setHoTen(taiKhoanDto.getFirstName().trim() + " " + taiKhoanDto.getLastName().trim());
        user.setMatKhau(passwordEncoder.encode(taiKhoanDto.getPassword()));
        user.setNickName(taiKhoanDto.getNickName());
        user.setEmail(taiKhoanDto.getEmail());
        user.setGioiTinh(taiKhoanDto.getSex());
        user.setSDT(taiKhoanDto.getPhone());
        
        LoaiTaiKhoan role = loaiTaiKhoanRepository.getByTenLoai("USER");
        if (role == null) {
            role = new LoaiTaiKhoan();
            role.setTenLoai("USER");
            loaiTaiKhoanRepository.save(role);
        }
        
        user.setLoaiTaiKhoans(Set.of(role));
        return taiKhoanRepository.save(user);
    }

	@Override
	public void saveToken(TaiKhoan user, String token) {
		user.setToken(token);
		taiKhoanRepository.save(user);
	}

	@Override
	public void saveEnable(TaiKhoan user) {
		user.setToken("no");
		user.setEnable(true);
		taiKhoanRepository.save(user);
	}
	
	@Override
	public void saveResetPassword(TaiKhoan user, String password) {
		user.setToken("no");
		user.setMatKhau(passwordEncoder.encode(password));
		taiKhoanRepository.save(user);
	}
}
