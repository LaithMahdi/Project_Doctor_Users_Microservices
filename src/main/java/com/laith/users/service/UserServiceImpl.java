package com.laith.users.service;

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
		User usr =userRep.findByUsername(username);
		Role role=roleRep.findByRole(rolename);
		usr.getRoles().add(role);
		//userRep.save(usr);
		return usr;
	}

	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}

}
