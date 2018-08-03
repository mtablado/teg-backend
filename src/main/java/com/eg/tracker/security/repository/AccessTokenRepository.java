package com.eg.tracker.security.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eg.tracker.security.oauth2.domain.AuthenticationAccessToken;

@Repository
public interface AccessTokenRepository extends MongoRepository<AuthenticationAccessToken, String> {

    public AuthenticationAccessToken findByTokenId(String tokenId);

    public AuthenticationAccessToken findByRefreshTokenId(String refreshTokenId);

    public AuthenticationAccessToken findByAuthenticationId(String authenticationId);

    public List<AuthenticationAccessToken> findByClientIdAndUserName(String clientId, String userName);

    public List<AuthenticationAccessToken> findByClientId(String clientId);

}
