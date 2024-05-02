package hcmute.alohcmute.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanModel {
	
	private String taiKhoan;
	private String matKhau;
	private String token;
	private String hoTen; 
	private String nickName;
	private String email;
	private String gioiTinh;
	private String sDT;
	private String avatarURl;
	private boolean enable;
	private long soLuongNguoiTheoDoi;
	private boolean isfollowed;

	
}
