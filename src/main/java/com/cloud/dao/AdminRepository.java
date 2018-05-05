package com.cloud.dao;




import org.springframework.data.repository.CrudRepository;

import com.cloud.model.Admin;


public interface AdminRepository extends CrudRepository<Admin, String> {

	Admin findByUsername(String username);
	
}
