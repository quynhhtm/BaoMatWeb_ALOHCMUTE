package hcmute.alohcmute.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table
public class TinNhan implements Serializable{
	private static final long serialVersionUID = -7595339127598956518L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaTinNhan")
	private int maTinNhan;
	
	@Column(name = "NoiDungChu", columnDefinition = "nvarchar(MAX)")
	private String noiDungChu;
	
	@Column(name = "NoiDungHinhAnh")
	private String noiDungHinhAnh;
	
	@Column(name = "ThoiGianGuiTinNhan")
	private LocalDateTime thoiGianGuiTinNhan;
	
	@ManyToOne
	@JoinColumn(name="TaiKhoan")
	private TaiKhoan taiKhoan;
	
	@ManyToOne
	@JoinColumn(name="MaCuocHoiThoai")
	@EqualsAndHashCode.Exclude private CuocHoiThoai cuocHoiThoai;

}
