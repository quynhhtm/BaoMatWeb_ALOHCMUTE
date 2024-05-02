package hcmute.alohcmute.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TaiKhoan_Nhom_Id implements Serializable{

	private static final long serialVersionUID = 812322399864657479L;
	private int maNhom;
	private String taiKhoan;
}