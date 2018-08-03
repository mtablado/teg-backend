package com.eg.tracker.domain;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Document(collection="user")
@Data
public class User implements UserDetails {

	private static final long serialVersionUID = -5154242719127281754L;

	@Id
	private String id;
	private String username;
	private String password;
	private String name;
	private String lastName;
	protected UserType type;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean enabled = true;
	private boolean locked = true;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
