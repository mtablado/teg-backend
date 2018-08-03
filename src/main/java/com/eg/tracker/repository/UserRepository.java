package com.eg.tracker.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eg.tracker.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

	public Optional<User> findOneByUsername(String username);
}
