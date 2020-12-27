package com.superops.user.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superops.user.dao.UserDao;

@Component
public class UserService {

	@Autowired
	private UserDao userDao;

//	Extract the encoded password from Authorization Header
	public String getEncodedPassword(String token) {
		String[] tokenArr = token.split(" ");
		String encodedPassword = tokenArr[1];
		return encodedPassword;
	}

//	Validate the User Login from Customer Table
	public Boolean validateUser(String token) {
		Boolean validUser = false;
		try {
			String decodedString = new String(Base64.getDecoder().decode(token.getBytes()));
			String[] credentials = decodedString.split(",");
			String uuid = userDao.validateUser(credentials[0], credentials[1]);
			if (uuid.length() > 0) {
				validUser = true;
			}
		} catch (Exception e) {

		}
		return validUser;
	}
}
