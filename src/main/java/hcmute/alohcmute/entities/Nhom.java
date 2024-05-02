package hcmute.alohcmute.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data 
@NoArgsConstructor 
@AllArgsConstructor
@ToString (exclude = {"taiKhoans", "baiViets", "taiKhoanTruongNhom"})


@Entity
@Table
public class Nhom  implements Serializable{
	private static final long serialVersionUID = 322144712404628458L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaNhom")
	private int maNhom;

	@Column(name = "TenNhom", columnDefinition = "nvarchar(2000)")
	private String tenNhom;

	@Column(name = "NgayThanhLap")
	private LocalDateTime ngayThanhLap;
	
	@Column(name = "HinhAnhNhom")
	private String nhomURL;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="MaCheDo")
	private CheDo cheDoNhom;
	
	@ManyToMany(mappedBy = "nhom")
	private Set<TaiKhoan> taiKhoans;
	
	@OneToMany(mappedBy = "nhom")
	private Set<BaiViet> baiViets;
	
	@ManyToOne
	@JoinColumn(name="TaiKhoanTruongNhom")
	private TaiKhoan taiKhoanTruongNhom;
}
