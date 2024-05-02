package hcmute.alohcmute.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TinNhanDto {
	private String noiDungChu;
	private String noiDungHinhAnh;
	private String thoiGianGuiTinNhan;
	
	private int maCuocHoiThoai;
	private String tenCuocHoiThoai;
	
	private String username;
	private String nickname;
	
}
