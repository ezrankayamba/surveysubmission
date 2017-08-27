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

import tz.co.nezatech.dev.surveysubmission.model.FormData;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.repository.FormDataRepository;

@RestController
@RequestMapping("/data/formdata")
@PreAuthorize("hasRole('Administrator')")
public class FormDataController {
	@Autowired
	FormDataRepository formDataRepository;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<FormData> get(ModelAndView mv) {
		return this.formDataRepository.getAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public FormData getById(@PathVariable Integer id, ModelAndView mv) {
		return this.formDataRepository.findById(id);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Status post(@RequestBody FormData entity, ModelAndView mv) {
		return this.formDataRepository.create(entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Status put(@PathVariable Integer id, @RequestBody FormData entity, ModelAndView mv) {
		return this.formDataRepository.update(id, entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Status delete(@PathVariable Integer id, ModelAndView mv) {
		return this.formDataRepository.delete(id);
	}
}
