package tz.co.nezatech.dev.surveysubmission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	@RequestMapping("/")
	public String home(ModelAndView mv) {
		return "index";
	}

	@RequestMapping("/login")
	public String login(ModelAndView mv) {
		return "login";
	}
}
