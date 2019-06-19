package com.oauth.controller;

import java.util.List;

import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oauth.entity.User;
import com.oauth.services.UserServiceImpl;
import com.oauth.util.UserValidator;

@Controller
public class RegistraionCotroller {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private UserValidator userValidator;
	
	/**
	 * get registration form
	 * @param model
	 * @param redirectUrl
	 * @return registration form
	 */
	@GetMapping("/registration")
	public String registration(Model model, @RequestParam(value = "callbackUrl") String redirectUrl) {
		model.addAttribute("userForm", new User());
		return "registration";
	}
	
	/**
	 * post registration form
	 * @param userForm
	 * @param bindingResult
	 * @param redirectUrl
	 * @return registration form if not success
	 * @return login form if success
	 */
	@PostMapping("/registration")
	public String registration(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult,  @RequestParam(value = "callbackUrl") String redirectUrl) {
		userValidator.validate(userForm, bindingResult);
		if(bindingResult.hasErrors()) {
			return "registration";
		}
		userService.beforeSave();
		userForm.setPassword(BCrypt.hashpw(userForm.getPassword(), BCrypt.gensalt(7)));
		userService.save(userForm);
		return "redirect:/login" + "?callbackUrl=" + redirectUrl;
	}
}
