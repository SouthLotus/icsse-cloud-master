package com.cloud.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloud.model.Admin;
import com.cloud.model.Tintuc;
import com.cloud.service.AdminService;
import com.cloud.service.AmazonService;
import com.cloud.service.TintucService;
import com.cloud.storage.FileSystemStorageService;
import com.cloud.storage.StorageFileNotFoundException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

@Controller
public class MainController {

	@Autowired
	private TintucService tintucService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private final FileSystemStorageService storageService;

	public MainController(FileSystemStorageService storageService) {
		this.storageService = storageService;
	}

	// hiển thị tin tức chinh
	@GetMapping("/xem")
	public String Xem(HttpServletRequest request) {
		request.setAttribute("tintuc", tintucService.xem());
		request.setAttribute("lienquan", tintucService.lienquan());
		request.setAttribute("mode", "MODE_tintucchinh");
		return "xem";
	}

	// xu lý xem thêm tin tức
	@GetMapping("/xemthem")
	public String xemthem(@RequestParam int id, HttpServletRequest request) {
		request.setAttribute("tintuc", tintucService.findTintuc(id));
		request.setAttribute("lienquan", tintucService.lienquan());
		request.setAttribute("mode", "MODE_tintucchinh");
		return "xem";
	}

	@GetMapping({"/"})
	public String index(HttpServletRequest request) {
		List<Tintuc> tintucs = tintucService.layMoiNhat();
		request.setAttribute("tintucs", tintucs);
		request.setAttribute("mode", "MODE_INDEX");
		return "index";
	}

	@GetMapping({"/admin"})
	public String home(HttpServletRequest request) {
		if(getCurrentAdmin(request) == null) {
			return "redirect:/";
		}
		request.setAttribute("mode", "MODE_HOME");
		return "admin";
	}

	// show ra tất cả tin tức
	@GetMapping("/all-tasks")
	public String allTasks(HttpServletRequest request) {
		if(getCurrentAdmin(request) == null) {
			return "redirect:/";
		}
		request.setAttribute("tintucs", tintucService.findAll());
		request.setAttribute("mode", "MODE_TINTUC");
		return "admin";
	}

	// điều hướng tới trang thêm tin tức
	@GetMapping("/new-task")
	public String newTask(HttpServletRequest request) {
		request.setAttribute("mode", "MODE_NEW");
		return "admin";
	}

	// chọn tin tức để updae
	@GetMapping("/update-task")
	public String updateTask(@RequestParam int id, HttpServletRequest request) {
		request.setAttribute("tintuc", tintucService.findTintuc(id));
		request.setAttribute("mode", "MODE_UPDATE");
		return "admin";
	}

	// xóa tin tức
	@GetMapping("/delete-task")
	public String deleteTask(@RequestParam int id, HttpServletRequest request) {
		tintucService.delete(id);
		request.setAttribute("tintucs", tintucService.findAll());
		request.setAttribute("mode", "MODE_TINTUC");
		return "admin";
	}

	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		if(getCurrentAdmin(request) != null) {
			request.setAttribute("mode", "MODE_HOME");
			return "redirect:/admin";
		}
		return "loginAdmin";
	}

	// xử lý đăng nhập
	@PostMapping("/login-admin")
	public String loginadmin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest request) {
		try {
			Admin admin = adminService.findadmin(username);
			if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
				setCurrentAdmin(request, admin);
				request.setAttribute("mode", "MODE_HOME");
				return "redirect:/admin";
			}
		}catch (Exception e) {
			request.setAttribute("message", "Saitk");
		}	
		return "loginAdmin";
	}

	// thêm hoặc update tin tức
	@PostMapping("/save-task")
	public String saveTask(@ModelAttribute Tintuc tintuc, BindingResult bindingResult,
			@RequestParam("Name") String name, @RequestParam("noidung") String noidung,
			@RequestParam("hinhanh") MultipartFile file, HttpServletRequest request) throws IOException {
		if(getCurrentAdmin(request) == null) {
			return "redirect:/";
		}
		tintuc.setThoigian(new Date());
		tintuc.setName(name);
		tintuc.setNoidung(noidung);
		tintucService.save(tintuc);
		tintucService.uploadImage(tintuc, file);
		request.setAttribute("tintucs", tintucService.findAll());
		request.setAttribute("mode", "MODE_TINTUC");
		return "admin";
	}

	@GetMapping("/show")
	public String showNews(@RequestParam("id") String id, HttpServletRequest request) {
		try {
			int nid = Integer.parseInt(id);
			Tintuc tintuc = tintucService.findTintuc(nid);
			request.setAttribute("tintuc", tintuc);
			List<Tintuc> tintucs = tintucService.layMoiNhat();
			request.setAttribute("tintucs", tintucs);
			request.setAttribute("mode", "MODE_INDEX");
			return "show";
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session !=null) {
			session.invalidate();
		}
		return "redirect:/login";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}


	private Admin getCurrentAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			Admin adm = (Admin)session.getAttribute("admin");
			return adm;
		}
		return null;
	}

	private Admin setCurrentAdmin(HttpServletRequest request, Admin adm) {
		HttpSession session = request.getSession(true);
		session.setAttribute("admin", adm);
		return adm;
	}
}
