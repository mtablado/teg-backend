package com.eg.tracker.security.oauth2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import com.eg.tracker.domain.User;
import com.eg.tracker.security.oauth2.domain.AuthenticationAccessToken;
import com.eg.tracker.security.oauth2.domain.AuthenticationRefreshToken;
import com.eg.tracker.security.repository.AccessTokenRepository;
import com.eg.tracker.security.repository.RefreshTokenRepository;

@Component
public class MongoTokenStore implements TokenStore {

	@Autowired
	private AccessTokenRepository accessTokenRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String arg0) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String arg0, String arg1) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		User principal = (User) authentication.getUserAuthentication().getPrincipal();

		// Retrieve existing token if exists.
		List<AuthenticationAccessToken> tokens =
				this.accessTokenRepository.findByClientIdAndUserName(
						authentication.getOAuth2Request().getClientId() , principal.getUsername());

		// there can only exist one token. It needs to be returned unless it can be expired.
		if (!tokens.isEmpty()) {
			return tokens.get(0).getOAuth2AccessToken();
		}

		// If the token does not exist return null to create a new one.
		return null;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenId) {
		AuthenticationAccessToken token = this.accessTokenRepository.findByTokenId(tokenId);
		OAuth2AccessToken accessToken = null;
		if (null != token) {
			accessToken = token.getOAuth2AccessToken();
		}
		return accessToken;
	}

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		return this.readAuthentication(token.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String tokenId) {
		return this.accessTokenRepository.findByTokenId(tokenId).getAuthentication();
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		return this.refreshTokenRepository.findByTokenId(token.getValue()).getAuthentication();
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenId) {
		OAuth2RefreshToken rt = null;
		AuthenticationRefreshToken token = this.refreshTokenRepository.findByTokenId(tokenId);
		if (null != token) {
			rt = token.getOAuth2RefreshToken();
		}
		return rt;
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		String tokenId = token.getValue();
		AuthenticationAccessToken accessToken = this.accessTokenRepository.findByTokenId(tokenId);
		this.accessTokenRepository.deleteById(accessToken.getTokenId());
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken token) {
		String refreshTokenId = token.getValue();
		AuthenticationAccessToken accessToken = this.accessTokenRepository.findByRefreshTokenId(refreshTokenId);
		if (null != accessToken) {
			this.accessTokenRepository.deleteById(accessToken.getTokenId());
		}
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		String refreshTokenId = token.getValue();
		AuthenticationRefreshToken refreshToken = this.refreshTokenRepository.findByTokenId(refreshTokenId);
		if (null != refreshToken) {
			this.refreshTokenRepository.deleteById(refreshToken.getTokenId());
		}
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authToken) {

		AuthenticationAccessToken token = new AuthenticationAccessToken();
		token.setTokenId(accessToken.getValue());
		token.setOAuth2AccessToken(accessToken);

		OAuth2RefreshToken rt = accessToken.getRefreshToken();
		if (null != rt) {
			token.setRefreshTokenId(rt.getValue());
			token.setRefreshToken(accessToken.getRefreshToken());
		}

		token.setAuthentication(authToken);

		@SuppressWarnings("unchecked")
		Map<String, String> details = (Map<String, String>) authToken.getUserAuthentication().getDetails();
		if (null != details) {
			// The token has been generated with login.
			token.setClientId(details.get("client_id"));
			token.setUserName(details.get("username"));
		} else if (null != authToken.getOAuth2Request()) {
			// When the token is generated due to refresh token.
			token.setClientId(authToken.getOAuth2Request().getClientId());
			token.setUserName(((UserDetails) authToken.getUserAuthentication().getPrincipal()).getUsername());
		}
		this.accessTokenRepository.save(token);
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {

		AuthenticationRefreshToken token = new AuthenticationRefreshToken();

		token.setTokenId(refreshToken.getValue());
		token.setOAuth2RefreshToken(refreshToken);
		token.setAuthentication(authentication);

		this.refreshTokenRepository.save(token);
	}
}
