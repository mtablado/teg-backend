package com.eg.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eg.tracker.domain.User;
import com.eg.tracker.service.UserService;

@RestController
@RequestMapping("/private/api/v1")
public class UserController {

	@Autowired
	private UserService service;

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<User> getUsers() throws Exception {
		return this.service.getUsers();
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User getUser(@PathVariable("id") String id) throws Exception {
		return this.service.getUser(id);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/users/username/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User getUserByUsername(@PathVariable("username") String username) throws Exception {
		return this.service.findUserByUsername(username);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/users/current-user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User getCurrentUser() throws Exception {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
