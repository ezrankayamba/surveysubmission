package tz.co.nezatech.dev.surveysubmission.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tz.co.nezatech.dev.surveysubmission.model.FormRepos;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.repository.FormReposRepository;

@RestController
@RequestMapping("/data/formrepos")
@PreAuthorize("hasRole('Administrator')")
public class FormReposController {
	@Autowired
	FormReposRepository reposRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<FormRepos> get(ModelAndView mv) {
		return this.reposRepository.getAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public FormRepos getById(@PathVariable Integer id, ModelAndView mv) {
		return this.reposRepository.findById(id);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public Status post(@RequestBody FormRepos entity, ModelAndView mv) {
		return this.reposRepository.create(entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Status put(@PathVariable Integer id, @RequestBody FormRepos entity, ModelAndView mv) {
		return this.reposRepository.update(id, entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Status delete(@PathVariable Integer id, ModelAndView mv) {
		return this.reposRepository.delete(id);
	}
}
