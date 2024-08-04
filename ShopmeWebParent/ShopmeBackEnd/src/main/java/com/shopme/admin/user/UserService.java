package com.shopme.admin.user;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	
	public List<User> listAll() {
		List<User> users = (List<User>) userRepo.findAll();
		return users;
	}
	
	public List<Role> listRoles() {
		List<Role> roles = (List<Role>) roleRepo.findAll();
		return roles;
	}

	public void save(User user) {
		userRepo.save(user);
	}
	
}
