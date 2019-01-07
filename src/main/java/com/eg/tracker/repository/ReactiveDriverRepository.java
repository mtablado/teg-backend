package com.eg.tracker.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.UserType;

import reactor.core.publisher.Flux;

public interface ReactiveDriverRepository extends ReactiveMongoRepository<Driver, String> {

	public Flux<Driver> findAllByType(UserType type);
}
