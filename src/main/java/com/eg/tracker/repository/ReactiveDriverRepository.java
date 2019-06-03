package com.eg.tracker.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.UserType;

import reactor.core.publisher.Flux;

public interface ReactiveDriverRepository extends ReactiveMongoRepository<Driver, String>/*, ReactiveQuerydslPredicateExecutor<Driver>*/ {

	public Flux<Driver> findAllByType(UserType type);

	public Flux<Driver> findAllByTypeAndLastPositionNotNullAndEnabled(UserType type, boolean enabled);
}
