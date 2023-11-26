package com.laith.users.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.laith.users.entities.Role;
import com.laith.users.entities.Token;
import com.laith.users.entities.User;
import com.laith.users.repos.RoleRepository;
import com.laith.users.repos.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRep;
	@Autowired
	RoleRepository roleRep;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRep.save(user);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRep.findByUsername(username);
	}

	@Override
	public Role addRole(Role role) {
		return roleRep.save(role);
	}

	@Override
	public User addRoleToUser(String username, String rolename) {
		User usr = userRep.findByUsername(username);
		Role r = roleRep.findByRole(rolename);
		usr.getRoles().add(r);
		return usr;
	}

	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}

	@Override
	public User registerUser(User user) {
		// Check if email is registered
		if (userRep.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Email already registered");
		}
		user.setEnabled(false);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		// set role
		List<Role> roles = new ArrayList<>();
		roles.add(roleRep.findRoleById(2L));
		user.setRoles(roles);

		// create a token
		createVerificationToken(user, generateRandomVerificationCode());

		sendMail(user);

		return userRep.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRep.findByEmail(email);
	}

	private int generateRandomVerificationCode() {
		return new Random().nextInt((999999 - 100000) + 1) + 100000;
	}

	private void sendMail(User user) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(user.getEmail());
		msg.setText("Hello " + user.getUsername() + "\nInsert your verification code : \n" + user.getToken().getCode()
				+ "\nto activate your account.");
		msg.setSubject("Verification code");
		mailSender.send(msg);
		System.out.println(user.getEmail());
	}

	@Override
	public User verifyCode(String email, int code) {
	    User user = findByEmail(email);

	    if (user != null && user.getToken() != null) {
	        Token token = user.getToken();
	        if (token.getCode() == code && !token.isExpired()) {
	            user.setEnabled(true);
	        } else {
	            throw new RuntimeException("Incorrect or expired verification code");
	        }
	    } else {
	        throw new RuntimeException("Invalid verification code");
	    }

	    return userRep.save(user);
	}


	private Token createVerificationToken(User user, int code) {
		Token token = new Token();
		token.setUser(user);
		token.setCode(code);
		token.setCreatedAt(LocalDateTime.now());
		token.setExpiredAt(LocalDateTime.now().plusMinutes(1));

		user.setToken(token);
		return token;
	}

}
