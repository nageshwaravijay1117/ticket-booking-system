package com.superops.booking.serviceinterface;

import com.superops.booking.model.User;

public interface UserServiceInterface {
	
User checkLogin(User user);
 void addUserName(String uname);
}
