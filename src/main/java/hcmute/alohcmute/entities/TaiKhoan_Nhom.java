package hcmute.alohcmute.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "TaiKhoan_Nhom")
public class TaiKhoan_Nhom {

	@EmbeddedId
	private TaiKhoan_Nhom_Id id;
	
	@Column(name = "accept", columnDefinition = "bit")
	private boolean accept;
}