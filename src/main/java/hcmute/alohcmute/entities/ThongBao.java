package hcmute.alohcmute.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(exclude="taiKhoan")

@Entity
@Table
public class ThongBao implements Serializable{
	
	private static final long serialVersionUID = -5888631492023904983L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaThongBao")
	private int maThongBao;
	
	@Column(name = "NoiDung", columnDefinition = "nvarchar(MAX)")
	private String NoiDung;
	
	@Column(name = "ThoiGian", columnDefinition = "Time")
	private LocalTime ThoiGian;
	
	@Column(name = "Ngay", columnDefinition = "Date")
	private LocalDate Ngay;
	
	@Column(name = "LinkThongBao", columnDefinition = "nvarchar(1000)")
	private String linkThongBao;
	
	@ManyToOne
	@JoinColumn(name="TaiKhoan")
	private TaiKhoan taiKhoan;
}
