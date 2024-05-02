package hcmute.alohcmute.entities;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor

@Entity
@Table
public class LoaiTaiKhoan implements Serializable{
	private static final long serialVersionUID = 3808802474750908577L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaLoai")
	private int maLoai;
	
	@Column(name = "TenLoai", columnDefinition = "nvarchar(2000)")
	private String tenLoai;
	
	@ManyToMany(mappedBy = "loaiTaiKhoans") 
	private Set<TaiKhoan> taiKhoans; 
}
