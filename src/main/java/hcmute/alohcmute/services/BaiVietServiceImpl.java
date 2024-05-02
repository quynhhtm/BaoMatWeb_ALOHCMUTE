package hcmute.alohcmute.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.Nhom;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.ThongBao;
import hcmute.alohcmute.repositories.BaiVietRepository;
import hcmute.alohcmute.repositories.TaiKhoanRepository;
import hcmute.alohcmute.security.SecurityUtil;

@Service
public class BaiVietServiceImpl implements IBaiVietService{
	@Autowired
	BaiVietRepository baiVietRepository;
	
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	IThongBaoService iTB;
	
	@Autowired
	ITaiKhoanService taikhoanSer;

	@Override
	public List<BaiViet> findAll() {
		return baiVietRepository.findAll();
	}

	@Override
	public Optional<BaiViet> findById(Integer id) {
		return baiVietRepository.findById(id);
	}

	@Override
	public BaiViet getById(Integer id) {
		return baiVietRepository.getById(id);
	}

	@Override
	public List<BaiViet> findAllBaiVietByUsername(String taiKhoanUsername) {
		return baiVietRepository.findAllBaiVietByUsername(taiKhoanUsername);
	}

	@Override
	public void deleteById(Integer id) {
		baiVietRepository.deleteById(id);
	}
	

	
	@Override
	public Page<BaiViet> getBaiVietByPage(String taikhoan, int page, int pageSize) {
		List<BaiViet> listBaiVietOld = findAllBaiVietByUsername(taikhoan);
		List<BaiViet> listBaiViet = new ArrayList<BaiViet>();
        
		if(taikhoan.equals(SecurityUtil.getMyUser().getTaiKhoan())) {
			for(BaiViet bv : listBaiVietOld) {
	        	if(bv.isEnable()==true) {
	        		listBaiViet.add(bv);
	        	}
	        }
		} else {
			List<TaiKhoan> listTaiKhoanTheoDoi = taiKhoanRepository.findTaiKhoanFollowersByUsername(taikhoan);
			
			if(listTaiKhoanTheoDoi.contains(taiKhoanRepository.findByTaiKhoan(SecurityUtil.getMyUser().getTaiKhoan()).get())) {
				for(BaiViet bv : listBaiVietOld) {
		        	if(bv.isEnable()==true && bv.getCheDoNhom().getMaCheDo() != 3) {
		        		listBaiViet.add(bv);
		        	}
		        }
			}else {
				for(BaiViet bv : listBaiVietOld) {
		        	if(bv.isEnable()==true && bv.getCheDoNhom().getMaCheDo() != 3 && bv.getCheDoNhom().getMaCheDo() != 2) {
		        		listBaiViet.add(bv);
		        	}
		        }
			}
			
			
		}
		
        int fromIndex = page * pageSize;
        int toIndex = Math.min((page + 1) * pageSize, listBaiViet.size());

        if (fromIndex > toIndex) {
            // Trang yêu cầu không hợp lệ
            return new PageImpl<>(List.of()); // Trả về trang trống
        }
        
        List<BaiViet> baiVietOnPage = listBaiViet.subList(fromIndex, toIndex);
        
        return new PageImpl<>(baiVietOnPage, PageRequest.of(page, pageSize), listBaiViet.size());
    }

	@Override
	public long demSoTuongTac(int maBaiViet) {
		BaiViet baiViet = baiVietRepository.getById(maBaiViet);
		Set<TaiKhoan> listTaiKhoan = baiViet.getTaiKhoans();
		return listTaiKhoan.size();
	}

	@Override
	public long tangLike(int maBaiViet, String taiKhoan) {
		String sql = "INSERT INTO tuong_tac (tai_khoan, ma_bai_viet) values (?, ?)";
	    jdbcTemplate.update(sql, taiKhoan, maBaiViet);
		return demSoTuongTac(maBaiViet);
	}

	@Override
	public long giamLike(int maBaiViet, String taiKhoan) {
		String sql = "DELETE FROM tuong_tac WHERE tai_khoan = ? AND ma_bai_viet = ?";
	    jdbcTemplate.update(sql, taiKhoan, maBaiViet);
		return demSoTuongTac(maBaiViet);
	}

	@Override
	public boolean checkLiked(int maBaiViet, String taiKhoan) {
		BaiViet baiViet = baiVietRepository.getById(maBaiViet);
		if (baiViet.getTaiKhoans().contains(taiKhoanRepository.findOneBytaiKhoan(taiKhoan)))
			return true;
		return false;
	}
	@Override
	public <S extends BaiViet> S save(S entity) {
		baiVietRepository.save(entity);
		Pattern pattern = Pattern.compile("@\\w+");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(entity.getNoiDungChu());

       
        // Find and print all occurrences
        while (matcher.find()) {
            String match = matcher.group();
            ThongBao tb = new ThongBao();
            tb.setNgay(java.time.LocalDate.now());
            String NoiDung = entity.getTaiKhoan().getHoTen()+" đã nhắc đến bạn trong một bài viết";
            tb.setNoiDung(NoiDung);
            String user=match.substring(1);
            tb.setTaiKhoan(taikhoanSer.findBytaiKhoan(user));
            tb.setThoiGian(java.time.LocalTime.now());
            tb.setLinkThongBao("/user/comment/" + entity.getMaBaiViet());
            iTB.save(tb);
        }
        return entity;
	}

	@Override
	public List<BaiViet> findBymaNhom(Nhom Nhom){
		return baiVietRepository.findBynhom(Nhom);
		
	}

	@Override
	public BaiViet findBymaBaiViet(int mabv) {
		return baiVietRepository.findBymaBaiViet(mabv);
	}

}
