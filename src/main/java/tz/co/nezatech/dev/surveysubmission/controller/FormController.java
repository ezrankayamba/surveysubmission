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

import tz.co.nezatech.dev.surveysubmission.model.Form;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.repository.FormRepository;

@RestController
@RequestMapping("/data/forms")
@PreAuthorize("hasRole('Administrator')")
public class FormController {
	@Autowired
	FormRepository formRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Form> get(ModelAndView mv) {
		return this.formRepository.getAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Form getById(@PathVariable Integer id, ModelAndView mv) {
		return this.formRepository.findById(id);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Status usersPost(@RequestBody Form entity, ModelAndView mv) {
		return this.formRepository.create(entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Status put(@PathVariable Integer id, @RequestBody Form entity, ModelAndView mv) {
		return this.formRepository.update(id, entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Status delete(@PathVariable Integer id, ModelAndView mv) {
		return this.formRepository.delete(id);
	}
}
