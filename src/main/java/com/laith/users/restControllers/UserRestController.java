package com.laith.users.restControllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.laith.users.entities.Token;
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
	/*
	@PostMapping("/register")
    public User registerUser(@RequestBody User user) {
          return userService.registerUser(user);
    }*/
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
	    try {
	        User registeredUser = userService.registerUser(user);
	        Map<String, String> response = new HashMap<>();
	        response.put("status", "success");
	        response.put("message", "Registration successful");
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    } catch (RuntimeException e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}

	
	
	
	@PostMapping("/checkcode")
	public ResponseEntity<Map<String, String>> verifyUser(@RequestBody Map<String, Object> requestBody) {
	    String email = (String) requestBody.get("email");
	    int code = (int) requestBody.get("code");

	    User user = userService.verifyCode(email, code);

	    if (user != null && !user.getToken().isExpired()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("status", "success");
	        response.put("message", "Verification successful. Account is now activated.");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        Map<String, String> response = new HashMap<>();
	        response.put("status", "error");
	        response.put("message", "Token is either invalid or expired");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	}


}


