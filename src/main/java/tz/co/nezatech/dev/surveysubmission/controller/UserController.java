package tz.co.nezatech.dev.surveysubmission.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.model.User;
import tz.co.nezatech.dev.surveysubmission.repository.UserRepository;

@RestController
@RequestMapping("/data")
@PreAuthorize("hasRole('Administrator')")
public class UserController {
	@Autowired
	UserRepository userRepository;
	//@Autowired
	//BCryptPasswordEncoder passwordEncoder;
	@Value("${user.default.password}")
	String defaultPasswordScrumbled;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> usersGet(ModelAndView mv) {
		return this.userRepository.getAll();
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public User usersGetById(@PathVariable Integer id, ModelAndView mv) {
		return this.userRepository.findById(id);
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public Status usersPost(@RequestBody User entity, ModelAndView mv) {
		entity.setPassword(defaultPasswordScrumbled);
		return this.userRepository.create(entity);
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	public Status usersPut(@PathVariable Integer id, @RequestBody User entity, ModelAndView mv) {
		return this.userRepository.update(id, entity);
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public Status usersDelete(@PathVariable Integer id, ModelAndView mv) {
		return this.userRepository.delete(id);
	}
}
