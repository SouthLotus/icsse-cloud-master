package com.cloud.service;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.dao.AdminRepository;
import com.cloud.model.Admin;



@Service
@Transactional
public class AdminService {
	
	@Autowired
	private final AdminRepository adminRepostiory;
	
	public AdminService(AdminRepository adminRepostiory) {
		this.adminRepostiory=adminRepostiory;
	}
	
	public Admin findadmin(String username) {
		return adminRepostiory.findByUsername(username);
	}

}
