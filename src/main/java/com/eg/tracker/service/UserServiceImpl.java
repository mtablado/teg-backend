package com.eg.tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.tracker.domain.User;
import com.eg.tracker.exception.NotFoundException;
import com.eg.tracker.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Override
	public User getUser(String id) {
		return this.repository.findById(id).orElseThrow(() -> new NotFoundException());
	}

	@Override
	public User findUserByUsername(String username) {
		return this.repository.findOneByUsername(username).orElseThrow(() -> new NotFoundException());
	}


	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public User addUser(User user) {
		return this.repository.save(user);
	}

	@Override
	public List<User> getUsers() {
		return this.repository.findAll();
	}

}
