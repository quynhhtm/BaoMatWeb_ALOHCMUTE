package hcmute.alohcmute.models;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import hcmute.alohcmute.entities.BaiViet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel {
	
	private int maBinhLuan;
	private String noiDungChu;
	private String noiDungHinhAnh;
	private LocalTime ThoiGian;
	private LocalDate Ngay;
	private BaiViet baiViet;

	private MultipartFile imageFile;
	private Boolean isEdit= false;

}
