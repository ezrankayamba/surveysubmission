package tz.co.nezatech.dev.surveysubmission.controller.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import tz.co.nezatech.dev.surveysubmission.model.FormRepos;
import tz.co.nezatech.dev.surveysubmission.model.User;
import tz.co.nezatech.dev.surveysubmission.repository.FormReposRepository;
import tz.co.nezatech.dev.surveysubmission.repository.ProjectRepository;
import tz.co.nezatech.dev.surveysubmission.repository.UserRepository;
import tz.co.nezatech.dev.surveysubmission.storage.StorageService;

@Controller
@RestController
@RequestMapping("/survey")
@PreAuthorize("hasRole('API')")
@PropertySource("classpath:config.properties")
public class SurveyApiController {
	@Value("${survey.form.files.upload.path}")
	private String uploadPath;
	@Value("${survey.form.files.upload.folder.video}")
	private String videos;
	@Value("${survey.form.files.upload.folder.picture}")
	private String pictures;
	@Value("${survey.form.files.upload.folder.other}")
	private String others;
	@Autowired
	StorageService storageService;

	@Autowired
	FormReposRepository reposRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	UserRepository userRepository;

	private final Logger LOG = LoggerFactory.getLogger(SurveyApiController.class);

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public @ResponseBody ApiResponse post(MultipartHttpServletRequest request) {
		long contentLength = request.getContentLength();
		LOG.debug("Content-length: " + contentLength);

		try {

			LOG.debug(request.getParameter("name"));
			LOG.debug(request.getCharacterEncoding());
			LOG.debug(request.getAttributeNames().nextElement());

			Collection<Part> parts = request.getParts();
			for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();) {
				Part part = (Part) iterator.next();
				String contentType = part.getContentType();
				String name = part.getName();
				long size = part.getSize();
				LOG.debug(String.format("Type: %s, Name: %s, Size: %d", contentType, name, size));

			}

			Iterator<String> files = request.getFileNames();
			while (files.hasNext()) {
				String fn = files.next();

				MultipartFile file = request.getFile(fn);
				// Get the file and save it somewhere
				byte[] bytes = file.getBytes();
				LOG.debug(String.format("File: %s, Size: %d, Filename: %s", fn, bytes.length,
						file.getOriginalFilename()));

				String filePath;
				if (file.getContentType().contains("video")) {
					filePath = videos + file.getOriginalFilename();
				} else if (file.getContentType().contains("image")) {
					filePath = pictures + file.getOriginalFilename();
				} else {
					filePath = others + file.getOriginalFilename();
				}

				Path path = Paths.get(uploadPath + filePath);
				Files.write(path, bytes);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ApiResponse response = new ApiResponse();
		response.setMessage("Successfully received");
		response.setStatus("200");

		return response;
	}

	@RequestMapping(value = "/repos", method = RequestMethod.GET)
	public @ResponseBody ApiResponse getMyRepos(@AuthenticationPrincipal UserDetails user) {
		String username = user.getUsername();
		System.out.println(String.format("Username: %s", username));
		User u = userRepository.getAll("username", username).get(0);

		ApiResponse response = new ApiResponse();
		response.setMessage("Successfully received");
		response.setStatus("200");
		response.setData(u.getProjects());

		return response;
	}

	@RequestMapping(value = "/repos/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getReposFile(@AuthenticationPrincipal UserDetails user, @PathVariable("id") int id,
			HttpServletResponse response) {
		FormRepos formRepos = reposRepository.findById(id);
		Resource file = storageService.loadAsResource(formRepos.getFilepath());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
