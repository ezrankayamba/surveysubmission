package tz.co.nezatech.dev.surveysubmission.controller.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


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

	private final Logger LOG = LoggerFactory.getLogger(SurveyApiController.class);

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public @ResponseBody ApiResponse post(MultipartHttpServletRequest request) {
		long contentLength = request.getContentLength();
		LOG.debug("Content-length: " + contentLength);

		try {
			
			LOG.debug(request.getParameter("name"));
			LOG.debug(request.getCharacterEncoding());
			LOG.debug(request.getAttributeNames().nextElement());
			
			Collection<Part> parts=request.getParts();
			for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();) {
				Part part = (Part) iterator.next();
				String contentType=part.getContentType();
				String name=part.getName();
				long size=part.getSize();
				LOG.debug(String.format("Type: %s, Name: %s, Size: %d", contentType,name, size));
				
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
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public @ResponseBody ApiResponse testPost(HttpServletRequest request) {
		int contentLength = request.getContentLength();
		LOG.debug("Content-length: " + contentLength);

		try {
			IOUtils.copy(request.getInputStream(), System.out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ApiResponse response = new ApiResponse();
		response.setMessage("Successfully received");
		response.setStatus("200");

		return response;
	}

}
