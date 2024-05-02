package hcmute.alohcmute.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.models.FileInfo;
import hcmute.alohcmute.security.SecurityUtil;
import hcmute.alohcmute.services.FilesStorageService;
import hcmute.alohcmute.services.ITaiKhoanService;

@Controller
public class FileController {

	@Autowired
	FilesStorageService storageService;
	
	@Autowired
	ITaiKhoanService taiKhoanService;

	@PostMapping("/files/upload/{taikhoan}")
	public ModelAndView uploadFile(Model model, @RequestParam("file") MultipartFile file,
			@PathVariable("taikhoan") String taikhoan) {
		String message = "";

		try {
			Optional<TaiKhoan> optTaiKhoan = taiKhoanService.findById(taikhoan);
			TaiKhoan tk = optTaiKhoan.get();
			String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			tk.setAvatarURl(newFileName);
			taiKhoanService.save(tk);
			
			storageService.save(file, newFileName);
			
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			model.addAttribute("message", message);
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			model.addAttribute("message", message);
		}

		String url = "redirect:/trangcanhan/thongtintaikhoan/" + SecurityUtil.getMyUser().getTaiKhoan();
		return new ModelAndView(url);
	}

	@GetMapping("/files")
	public String getListFiles(Model model) {
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		model.addAttribute("files", fileInfos);

		return "files";
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/files/delete/{filename:.+}")
	public String deleteFile(@PathVariable String filename, Model model, RedirectAttributes redirectAttributes) {
		try {
			boolean existed = storageService.delete(filename);

			if (existed) {
				redirectAttributes.addFlashAttribute("message", "Delete the file successfully: " + filename);
			} else {
				redirectAttributes.addFlashAttribute("message", "The file does not exist!");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message",
					"Could not delete the file: " + filename + ". Error: " + e.getMessage());
		}

		return "redirect:/files";
	}
}
