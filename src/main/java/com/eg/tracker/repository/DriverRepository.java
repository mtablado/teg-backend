package com.eg.tracker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eg.tracker.domain.Driver;

public interface DriverRepository extends MongoRepository<Driver, String> {

    public Driver findByName(String firstName);
    public List<Driver> findByLastName(String lastName);

}
