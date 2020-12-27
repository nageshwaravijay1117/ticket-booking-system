package com.superops.booking.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UniqueID {

//	Generates an Unique UUID
	public String generateUniqueID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

}
