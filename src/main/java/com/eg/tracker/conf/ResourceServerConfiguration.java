package com.eg.tracker.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "ElGarabatoAppResId";

    @Autowired
    TokenStore tokenStore;

    @Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources
			.resourceId(RESOURCE_ID)
			.tokenStore(this.tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
        http
    	.antMatcher("/private/api/**")
        .authorizeRequests()
        	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        	.antMatchers("/private/api/admin/**").hasRole("ADMIN")
        	.anyRequest().authenticated();
	}

}
