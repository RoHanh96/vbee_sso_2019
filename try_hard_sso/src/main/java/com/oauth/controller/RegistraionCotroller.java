package com.oauth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oauth.entity.User;
import com.oauth.services.UserServiceImpl;
import com.oauth.util.UserValidator;

@Controller
public class RegistraionCotroller {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("userForm", new User());
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registration(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult) {
		userValidator.validate(userForm, bindingResult);
		if(bindingResult.hasErrors()) {
			return "registration";
		}
		userService.beforeSave();
		userService.save(userForm);
		return "redirect:/login";
	}
}
