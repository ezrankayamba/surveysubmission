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

import tz.co.nezatech.dev.surveysubmission.model.Role;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.repository.RoleRepository;

@RestController
@RequestMapping("/data")
@PreAuthorize("hasRole('Administrator')")
public class RoleController {
	@Autowired
	RoleRepository roleRepository;

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public List<Role> rolesGet(ModelAndView mv) {
		return this.roleRepository.getAll();
	}

	@RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
	public Role rolesGetById(@PathVariable Integer id, ModelAndView mv) {
		return this.roleRepository.findById(id);
	}

	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	public Status rolesPost(@RequestBody Role role, ModelAndView mv) {
		return this.roleRepository.create(role);
	}

	@RequestMapping(value = "/roles/{id}", method = RequestMethod.PUT)
	public Status rolesPut(@PathVariable Integer id, @RequestBody Role role, ModelAndView mv) {
		return this.roleRepository.update(id, role);
	}

	@RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
	public Status rolesDelete(@PathVariable Integer id, ModelAndView mv) {
		return this.roleRepository.delete(id);
	}
}
