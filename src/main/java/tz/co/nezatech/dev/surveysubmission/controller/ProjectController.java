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

import tz.co.nezatech.dev.surveysubmission.model.Project;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.repository.ProjectRepository;

@RestController
@RequestMapping("/data/projects")
@PreAuthorize("hasRole('Administrator')")
public class ProjectController {
	@Autowired
	ProjectRepository projectRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Project> get(ModelAndView mv) {
		return this.projectRepository.getAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Project getById(@PathVariable Integer id, ModelAndView mv) {
		return this.projectRepository.findById(id);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public Status post(@RequestBody Project entity, ModelAndView mv) {
		return this.projectRepository.create(entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Status put(@PathVariable Integer id, @RequestBody Project entity, ModelAndView mv) {
		return this.projectRepository.update(id, entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Status delete(@PathVariable Integer id, ModelAndView mv) {
		return this.projectRepository.delete(id);
	}
}
