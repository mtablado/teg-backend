package com.eg.tracker.security.oauth2.converter;

import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.eg.tracker.domain.User;

@ReadingConverter
public class PreAuthenticatedAuthenticationTokenReadConverter
		implements Converter<Document, PreAuthenticatedAuthenticationToken> {

	@Override
	public PreAuthenticatedAuthenticationToken convert(Document source) {
		Document p = (Document) source.get("principal");
		User principal = new User();
		principal.setId(((ObjectId) p.get("_id")).toString());
		principal.setUsername((String) p.get("username"));
		principal.setPassword((String) p.get("password"));

		// TODO fill up credentials for tokens that use them.
		Object credentials = null;

		@SuppressWarnings("unchecked")
		List<? extends GrantedAuthority> authoritiesList = (List<? extends GrantedAuthority>) source.get("authorities");

		List<? extends GrantedAuthority> authorities = Collections.unmodifiableList(authoritiesList);
		PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(principal, credentials, authorities);
		return token;
	}

}
