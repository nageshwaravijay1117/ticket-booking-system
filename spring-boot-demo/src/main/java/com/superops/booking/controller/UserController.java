package com.superops.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superops.booking.model.ErrorResponse;
import com.superops.booking.model.ServerResponse;
import com.superops.booking.model.User;
import com.superops.booking.serviceinterface.UserServiceInterface;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	UserServiceInterface userServiceInterface;
	
	@GetMapping("test/{user}")
	public ResponseEntity<ServerResponse> userLogin(@PathVariable String user) {
		
		
		System.out.println(user);
	
		ServerResponse response=null;
		try {
			
			userServiceInterface.addUserName(user);
					
		}catch(Exception e){
			return new ResponseEntity<ServerResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);
	}



}
