package com.superops.booking.service;

import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.superops.booking.daointerface.UserDaoInterface;
import com.superops.booking.model.User;
import com.superops.booking.serviceinterface.UserServiceInterface;

@Service
public class UserService1 implements UserServiceInterface {

	@Autowired
	UserDaoInterface userDaoInterface;
	
	@Override
	public User checkLogin(User user) {
		User response=null;

		try {
			response=new User();
			response= userDaoInterface.checkLogin(user);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return response;
	}
	
	
	public String checkLogian(User user) {
		User response=null;

		try {
			response=new User();
			response= userDaoInterface.checkLogin(user);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return "";
	}


	@Override
	public void addUserName(String uname) {
		 userDaoInterface.addUserName(uname);
		}


}
