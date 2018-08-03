package com.eg.tracker.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.eg.tracker.domain.User;

public interface UserService {

	User getUser(String id);

	User findUserByUsername(String username);

	@PreAuthorize("hasRole('ADMIN')")
	User addUser(User user);

	List<User> getUsers();
}
