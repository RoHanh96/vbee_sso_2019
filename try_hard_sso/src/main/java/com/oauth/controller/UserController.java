package com.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oauth.entity.User;
import com.oauth.services.RoleServiceImpl;
import com.oauth.services.UserServiceImpl;
import com.oauth.util.CookieUtil;
import com.oauth.util.UserValidator;
import com.oauth.util.UserValidatorEdit;

@Controller
public class UserController {
	private static final String roleAdmin = "ROLE_ADMIN";
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	RoleServiceImpl roleService;
	
	@Autowired
	UserValidatorEdit userValidatorEdit;
	
	@Autowired
	UserValidator userValidator;
	
	@RequestMapping(value = "/edit-user-{userId}", method = RequestMethod.GET)
	public String editUser(@PathVariable String userId, ModelMap model, HttpServletRequest request) {
		try {
			if(!(LoginController.getUserRole(request).equals(roleAdmin))) {
				return "access_denied";
			}
		} catch (Exception e) {
			return "access_denied";
		}
		
		User user = new User();
		if(!model.containsAttribute("userInfo")) {
			user = userService.getUserById(Integer.valueOf(userId));
			model.addAttribute("userInfo", user);
		}
		model.addAttribute("roles", roleService.getAllRole());
		return "edit";
	}
	
	@RequestMapping(value = "/edit-user-{userId}", method = RequestMethod.POST)
	public String editUser(@ModelAttribute("userInfo") @Valid User userInfo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		userValidatorEdit.validate(userInfo, bindingResult);
		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userInfo", bindingResult);
			redirectAttributes.addFlashAttribute("userInfo", userInfo);
			return "redirect:/edit-user-{userId}";
		}
		userService.update(userInfo);
		return "redirect:/list_user";
	}
	
	@RequestMapping(value = "/delete-user-{userId}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable String userId, HttpServletRequest request) {
		try {
			if(!(LoginController.getUserRole(request).equals(roleAdmin))) {
				return "access_denied";
			}
		} catch (Exception e) {
			return "access_denied";
		}
		User user = userService.getUserById(Integer.valueOf(userId));
		userService.deleteById(user);
		return "redirect:/list_user";
		
	}
	
	@RequestMapping(value = "/logout")
	public String logOut(HttpServletRequest request, HttpServletResponse response) {
		CookieUtil.clear(response, jwtTokenCookieName);
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/newuser", method=RequestMethod.GET)
	public String createUser(Model model, HttpServletRequest request) {
		model.addAttribute("roles", roleService.getAllRole());
		if(!model.containsAttribute("userRegistrationAdmin")) {
			model.addAttribute("userRegistrationAdmin", new User());
		}
		return "registration_admin";
	}
	
	@RequestMapping(value = "/newuser", method=RequestMethod.POST)
	public String createUser(@ModelAttribute("userRegistrationAdmin") @Valid User userRegistrationAdmin, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		userValidator.validate(userRegistrationAdmin, bindingResult);
		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationAdmin", bindingResult);
			redirectAttributes.addFlashAttribute("userRegistrationAdmin", userRegistrationAdmin);
			return "redirect:/newuser";
		}
		userService.save(userRegistrationAdmin);
		return "redirect:/list_user";
	}
}
