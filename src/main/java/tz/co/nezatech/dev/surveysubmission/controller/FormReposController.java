package tz.co.nezatech.dev.surveysubmission.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import tz.co.nezatech.dev.surveysubmission.model.FormRepos;
import tz.co.nezatech.dev.surveysubmission.model.formui.FormUI;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.repository.FormReposRepository;
import tz.co.nezatech.dev.surveysubmission.storage.StorageService;
import tz.co.nezatech.dev.surveysubmission.util.RegexUtil;

@RestController
@RequestMapping("/data/formrepos")
@PreAuthorize("hasRole('Administrator')")
@PropertySource("classpath:config.properties")
public class FormReposController {
	@Value("${survey.form.repos.version.regex}")
	private String versionRegex;
	@Autowired
	FormReposRepository reposRepository;
	@Autowired
	StorageService storageService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<FormRepos> get(ModelAndView mv) {
		return this.reposRepository.getAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public FormRepos getById(@PathVariable Integer id, ModelAndView mv) {
		return this.reposRepository.findById(id);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public Status post(@RequestPart("entity") FormRepos entity,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		Status saved = new Status(500, "Repos file invalid");

		if (file != null) {
			String filename = file.getOriginalFilename();
			String version = RegexUtil.value(filename, versionRegex);
			if (version == null || version.trim().isEmpty()) {
				saved = new Status(500, "Repos file invalid, invalid version");
			} else {
				ObjectMapper mapper = new ObjectMapper();
				try {
					FormUI ui = mapper.readValue(file.getInputStream(), FormUI.class);
					if (ui == null || !version.equals(ui.getVersion())) {
						saved = new Status(500, "Repos file invalid, version mismatch on file name vs ui json");
					} else {
						entity.setName(ui.getName());
						entity.setDescription(ui.getLabel());
						entity.setVersion(ui.getVersion());

						saved = this.reposRepository.create(entity);
						int id = saved.getGeneratedId();

						String prefix = String.format("%08d-", id);
						storageService.store(file, prefix);
						entity.setFilepath(prefix + file.getOriginalFilename());
						entity.setId(id);

						this.reposRepository.update(id, entity);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					saved = new Status(500, "Exception: " + e.getMessage());
				}
			}

		}
		return saved;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Status put(@PathVariable Integer id, @RequestPart(value = "entity") FormRepos entity,
			@RequestPart(value = "file", required = false) MultipartFile file) {

		Status saved;

		if (file != null) {
			String filename = file.getOriginalFilename();
			String version = RegexUtil.value(filename, versionRegex);
			if (version == null || version.trim().isEmpty()) {
				saved = new Status(500, "Repos file invalid, invalid version -> " + version);
			} else {
				ObjectMapper mapper = new ObjectMapper();
				try {
					FormUI ui = mapper.readValue(file.getInputStream(), FormUI.class);
					if (ui == null || !version.equals(ui.getVersion())
							|| ui.getVersion().compareToIgnoreCase(version) <= 0) {
						saved = new Status(500,
								"Repos file invalid, version mismatch on file name vs ui json or un-increased version");
					} else {
						entity.setName(ui.getName());
						entity.setDescription(ui.getLabel());
						entity.setVersion(ui.getVersion());
						
						String prefix = String.format("%08d-", id);
						storageService.store(file, prefix);
						entity.setFilepath(prefix + file.getOriginalFilename());
						entity.setId(id);

						saved = this.reposRepository.update(id, entity);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					saved = new Status(500, "Exception: " + e.getMessage());
				}
			}

		} else {
			saved = new Status(500, "Repos file invalid");
		}

		return saved;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Status delete(@PathVariable Integer id, ModelAndView mv) {
		return this.reposRepository.delete(id);
	}

}
