package hcmute.alohcmute.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString (exclude = {"binhLuans", "thongBaos", "baiViets", "nhoms"})

@Entity
@Table
public class TaiKhoan implements Serializable{
	private static final long serialVersionUID = -8430672957164998050L;
	
	@Id
	@Column(name = "TaiKhoan")
	private String taiKhoan;
	
	@Column(name = "MatKhau", columnDefinition = "varchar(2000)")
	private String matKhau;
	
	@Column(name = "Token")
	private String token = "no";
	
	@Column(name = "Enable", columnDefinition = "bit")
	private boolean enable = false;
	
	@Column(name = "HoTen", columnDefinition = "nvarchar(2000)")
	private String hoTen; 

	@Column(name = "NickName", columnDefinition = "nvarchar(2000)")
	private String nickName;
	
	@Column(name = "Email")
	private String email;
	
	@Column(name = "GioiTinh", columnDefinition = "nvarchar(3)")
	private String gioiTinh;
	
	@Column(name = "SDT")
	private String sDT;
	
	@Column(name = "AvatarURL", columnDefinition = "nvarchar(2000)")
	private String avatarURl = "account.png";
	
	@OneToMany(mappedBy = "taiKhoan", fetch = FetchType.LAZY)
	private List<BaiViet> baiViets;
	
	@OneToMany(mappedBy = "taiKhoan")
	private Set<TinNhan> tinNhans;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "TaiKhoan_LoaiTaiKhoan",
			joinColumns = {@JoinColumn(name = "taiKhoan")},
			inverseJoinColumns = {@JoinColumn(name = "maLoai")})
	private Set<LoaiTaiKhoan> loaiTaiKhoans;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "TaiKhoan_CuocHoiThoai",
		joinColumns = {@JoinColumn(name = "TaiKhoan") },
		inverseJoinColumns = {@JoinColumn(name = "MaCuocHoiThoai")})
	private Set<CuocHoiThoai> cuocHoiThoais;
	
	@ManyToMany
	@JoinTable(name = "TaiKhoan_Nhom",
		joinColumns = {@JoinColumn(name = "TaiKhoan") },
		inverseJoinColumns = {@JoinColumn(name = "MaNhom")})
	private Set<Nhom> nhom;
	
	@ManyToMany
	@JoinTable(name = "TaiKhoan_TheoDoi_TaiKhoan",
		joinColumns = {@JoinColumn(name = "TaiKhoanTheoDoi") },
		inverseJoinColumns = {@JoinColumn(name = "TaiKhoanBiTheoDoi")})
	private Set<TaiKhoan> taiKhoanTheoDois;
	
	@ManyToMany
	@JoinTable(name = "TaiKhoan_Chan_TaiKhoan",
		joinColumns = {@JoinColumn(name = "TaiKhoanChan") },
		inverseJoinColumns = {@JoinColumn(name = "TaiKhoanBiChan")})
	private Set<TaiKhoan> taiKhoanChans;
	
	@OneToMany(mappedBy = "taiKhoan", fetch = FetchType.LAZY)
	private Set<ThongBao> thongBaos;
	
	@OneToMany(mappedBy = "taiKhoan", fetch = FetchType.LAZY)
	private Set<BinhLuan> binhLuans;
	
	@ManyToMany
	@JoinTable(name = "TuongTac",
		joinColumns = {@JoinColumn(name = "TaiKhoan") },
		inverseJoinColumns = {@JoinColumn(name = "MaBaiViet")})
	private Set<BaiViet> baiVietTuongTacs;
	
	@OneToMany(mappedBy = "taiKhoanTruongNhom", fetch = FetchType.LAZY)
	private Set<Nhom> nhoms;

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		
		TaiKhoan other = (TaiKhoan) obj;
		return this.taiKhoan==other.getTaiKhoan();
	}

	@Override
	public int hashCode() {
		return Objects.hash(taiKhoan);
	}
}
