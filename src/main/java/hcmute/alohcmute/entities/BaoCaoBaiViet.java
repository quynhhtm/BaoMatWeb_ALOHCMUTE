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
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor

@Entity
@Table
public class BaoCaoBaiViet  implements Serializable{
	private static final long serialVersionUID = -894093677681477663L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaBaoCao")
	private int maBaoCao;
	
	@Column(name = "NoiDungBaoCao", columnDefinition = "nvarchar(2000)")
	private String noiDungBaoCao;
	
	@Column(name = "ThoiGian", columnDefinition = "Time")
	private LocalTime ThoiGian;
	
	@Column(name = "Ngay", columnDefinition = "Date")
	private LocalDate Ngay;
	
	@ManyToOne
	@JoinColumn(name="MaBaiViet")
	private BaiViet baiViet;

	@Override
	public String toString() {
		return "BaoCaoBaiViet [maBaoCao=" + maBaoCao + ", noiDungBaoCao=" + noiDungBaoCao + ", ThoiGian=" + ThoiGian
				+ ", Ngay=" + Ngay + ", baiViet=" + baiViet + "]";
	}
	
	
}
