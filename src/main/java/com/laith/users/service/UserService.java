package com.laith.users.service;

import java.util.List;
import java.util.Optional;

import com.laith.users.entities.Role;
import com.laith.users.entities.User;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
	List<User> findAllUsers();
	User registerUser(User user);
	User findByEmail(String email);
}
