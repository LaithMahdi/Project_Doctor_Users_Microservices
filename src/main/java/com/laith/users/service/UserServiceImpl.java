package com.laith.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.laith.users.entities.Role;
import com.laith.users.entities.User;
import com.laith.users.repos.RoleRepository;
import com.laith.users.repos.UserRepository;

import jakarta.transaction.Transactional;


@Transactional
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRep;
	@Autowired
	RoleRepository roleRep;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;


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
		System.out.println(r);
		if (usr.getRoles() == null) {
		    usr.setRoles(new ArrayList<>());
		}
		if (!usr.getRoles().contains(r)) {
			usr.getRoles().add(r);
		}
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
	    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    user.setEnabled(false);
	    List<Role> roles=new ArrayList<>();
        roles.add(roleRep.findRoleById(2L));
        user.setRoles(roles);
	    return userRep.save(user);
	}

	@Override
	public User findByEmail(String email) {
	    return userRep.findByEmail(email);
	}

}
