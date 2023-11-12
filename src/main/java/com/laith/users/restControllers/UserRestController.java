package com.laith.users.restControllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.laith.users.entities.User;
import com.laith.users.service.UserService;

import lombok.Data;

@RestController
@CrossOrigin(origins = "*")
public class UserRestController {
	@Autowired
	UserService userService;
	
	
	
	
	
	@RequestMapping(path = "all", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userService.findAllUsers();
	}
	
	@PostMapping("/register")
    public User registerUser(@RequestBody User user) {
          return userService.registerUser(user);
    }
	
	
	
	@PostMapping("/checkcode")
    public String verifyUser(@RequestBody User request ) {
		String email = request.getEmail();
		int verificationCode= request.getVerificationCode();
        System.out.println("user activated: " + verificationCode);
        User user =userService.verifyCode(email, verificationCode);
        if (user!=null)
            return " account has been verified ";
        else
           return "account has not been verified";
    }

}


