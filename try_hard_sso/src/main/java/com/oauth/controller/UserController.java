package com.oauth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
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
import org.springframework.web.util.WebUtils;

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
//	private static final String domainServer = "springsso.herokuapp.com";
	private static final String domainServer = "localhost";
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	RoleServiceImpl roleService;
	
	@Autowired
	UserValidatorEdit userValidatorEdit;
	
	@Autowired
	UserValidator userValidator;
	
	/**
	 * get edit user info form
	 * @param userId
	 * @param model
	 * @param request
	 * @return access_denied form if user's role is not admin
	 * @return edit_form if user's role is admin
	 */
	@RequestMapping(value = "/edit-user-{userId}", method = RequestMethod.GET)
	public String editUser(@PathVariable String userId, ModelMap model, HttpServletRequest request) {
		String role = LoginController.getUserRole(request);
		if(role != null) {
			try {
				if(!(role.equals(roleAdmin))) {
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
		return "access_denied";
	}
	
	/**
	 * post edit user info
	 * @param userInfo
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return edit-user form if has any error
	 * @return list_user form if has not any error
	 */
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
	
	/**
	 * get delete-user info
	 * @param userId
	 * @param request
	 * @return access-denied if user's role is not admin
	 * @return list_user form if user's role is admin
	 */
	@RequestMapping(value = "/delete-user-{userId}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable String userId, HttpServletRequest request) {
		String role = LoginController.getUserRole(request);
		if(role != null) {
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
		return "access_denied";
	}
	
	/**
	 * clear value of cookie
	 * @param request
	 * @param response
	 * @return get "/login"
	 */
	@RequestMapping(value = "/logout")
	public String logOut(HttpServletRequest request, HttpServletResponse response) {
//		CookieUtil.clear(response, jwtTokenCookieName);
		CookieUtil.clear(response, jwtTokenCookieName, domainServer);
		return "redirect:/login";
	}
	
	/**
	 * add new user for admin
	 * @param model
	 * @param request
	 * @return access-denied if is not admin
	 * @return registraion_adin form if is admin
	 */
	@RequestMapping(value = "/newuser", method=RequestMethod.GET)
	public String createUser(Model model, HttpServletRequest request) {
		String role = LoginController.getUserRole(request);
		if(role != null) {
			try {
				if(!(LoginController.getUserRole(request).equals(roleAdmin))) {
					return "access_denied";
				}
			} catch (Exception e) {
				return "access_denied";
			}
			model.addAttribute("roles", roleService.getAllRole());
			if(!model.containsAttribute("userRegistrationAdmin")) {
				model.addAttribute("userRegistrationAdmin", new User());
			}
			return "registration_admin";
		}
		return "access_denied";
	}
	
	/**
	 * post user data info
	 * @param userRegistrationAdmin
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return newuser form if has any errors
	 * @return get "/list_user" if has not any errors
	 */
	@RequestMapping(value = "/newuser", method=RequestMethod.POST)
	public String createUser(@ModelAttribute("userRegistrationAdmin") @Valid User userRegistrationAdmin, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		userValidator.validate(userRegistrationAdmin, bindingResult);
		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationAdmin", bindingResult);
			redirectAttributes.addFlashAttribute("userRegistrationAdmin", userRegistrationAdmin);
			return "redirect:/newuser";
		}
		userRegistrationAdmin.setPassword(BCrypt.hashpw(userRegistrationAdmin.getPassword(), BCrypt.gensalt(7)));
		userService.save(userRegistrationAdmin);
		return "redirect:/list_user";
	}
}
