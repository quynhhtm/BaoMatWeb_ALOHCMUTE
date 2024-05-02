package hcmute.alohcmute.services;

import java.util.List;

import hcmute.alohcmute.entities.Nhom;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.entities.TaiKhoan_Nhom;

public interface INhomService {

	Nhom findBymaNhom (int maNhom);

	List<Nhom> findByTenNhomContainingIgnoreCase(String searchString);
	
	void sendRequestToGroup(TaiKhoan tk, Nhom gr);

	List<TaiKhoan_Nhom> findNhomTaiKhoan(String taiKhoan);

	List<TaiKhoan_Nhom> findAllTK_Nhom();

	void leaveGroup(String tk, int nhom);

	List<TaiKhoan_Nhom> findTaiKhoanByNhom(int maNhom);

	void addMember(String tk, int nhom);

	void Save(Nhom nhom);

	boolean createGroup(String username, String groupName, String CheDo);
}
