package com.warehouse.service;

import com.warehouse.entity.User;

public interface UserService {

	User findByUsername(String username);
	User findById(Integer id);
	void save(User user);
}
