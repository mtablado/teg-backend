package com.eg.tracker.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eg.tracker.security.oauth2.domain.AuthenticationRefreshToken;

@Repository
public interface RefreshTokenRepository extends MongoRepository<AuthenticationRefreshToken, String> {
	AuthenticationRefreshToken findByTokenId(String tokenId);
}
