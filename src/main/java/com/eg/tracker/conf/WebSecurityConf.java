package com.eg.tracker.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
//@Order(Ordered.HIGHEST_PRECEDENCE) // Set highest precedence here to handle the authorization and login endpoints here. Otherwise, the resource server configuration will kick in for the /login endpoint and you will get “Full Authentication Required” response
public class WebSecurityConf extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.userPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
    	.requestMatchers().antMatchers("/", "/home", "/login")
    	.and()
        .authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated()
//        .and()
//        .authorizeRequests() // Turn on when roles get in place.
//        	.anyRequest().hasRole("USER") // ROLE_ is automatically inserted.
//    	.and()
//        .formLogin()
//            .loginPage("/login")
//            .permitAll()
        .and()
        .logout()
            .permitAll();
    }

}
