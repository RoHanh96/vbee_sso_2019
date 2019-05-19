package com.oauth.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.oauth.entity.User;
import com.oauth.services.UserServiceImpl;

@Component
public class UserValidatorEdit implements Validator {
@Autowired UserServiceImpl userServiceImpl;
	
	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}
	
	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        User userInDB = userServiceImpl.getUserById(user.getId());
//        System.out.println(userInDB.getUsername() + user.getUsername() + (userInDB.getUsername().equalsIgnoreCase(user.getUsername())));
        if (userInDB != null && !(userInDB.getUsername().equalsIgnoreCase(user.getUsername()))) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        User userInDB1 = userServiceImpl.getUserByEmail(user.getEmail());
        System.out.println(userInDB.getEmail() + " " + user.getEmail() );
        if (userInDB1 != null && !(userInDB1.getEmail().equalsIgnoreCase(userInDB.getEmail()))) {
            errors.rejectValue("email", "Duplicate.userForm.email");
        }
	}
}
