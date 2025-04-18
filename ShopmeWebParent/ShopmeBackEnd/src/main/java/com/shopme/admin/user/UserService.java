package com.shopme.admin.user;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE = 4;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public List<User> listAll() {
		List<User> users = (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
		return users;
	}
	
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
		
		if(keyword != null) return userRepo.findAll(keyword, pageable);
		
		return userRepo.findAll(pageable);
	}
	
	
	public List<Role> listRoles() {
		List<Role> roles = (List<Role>) roleRepo.findAll();
		return roles;
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		
		if(isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		
		return userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if (userByEmail == null) return true; 
		
		boolean isCreatingNew = (id == null);
		
		if (isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}
	
	public User get(Integer id) throws UserNotFoundException {
		
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with Id");
		}
		
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
		
		userRepo.deleteById(id);
	}
	
	public void updateUserEnableStatus(Integer id, boolean enable) {
		userRepo.updateEnabledStatus(id, enable);
	}
}
