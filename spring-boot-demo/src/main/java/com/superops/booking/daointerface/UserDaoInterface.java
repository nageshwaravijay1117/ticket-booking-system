package com.superops.booking.daointerface;

import com.superops.booking.model.User;

public interface UserDaoInterface {
	User checkLogin(User user);
	 void addUserName(String uname);
}
