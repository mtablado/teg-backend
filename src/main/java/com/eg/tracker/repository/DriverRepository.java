package com.eg.tracker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.UserType;

public interface DriverRepository extends MongoRepository<Driver, String> {

    public Driver findByName(String name);
    public List<Driver> findByLastname(String lastname);
    public List<Driver> findByType(UserType type);
    public List<Driver> findAllByTypeAndLastPositionNotNullAndEnabled(UserType type, boolean enabled);

}
