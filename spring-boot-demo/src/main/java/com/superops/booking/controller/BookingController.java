package com.superops.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superops.booking.model.BookTicketRequest;
import com.superops.booking.model.BookTicketResponse;

import com.superops.booking.serviceinterface.BookingServiceInterface;

@RestController
@RequestMapping("booking")
public class BookingController {
	
	@Autowired
	BookingServiceInterface bookingServiceInterface;
	
	
//	The Initail Booking Request Reaches here
	
	@PostMapping("book-ticket")
public ResponseEntity<BookTicketResponse> userLogin(@RequestBody BookTicketRequest bookTicketRequest) {
		
		BookTicketResponse response=null;
		try {
			response=bookingServiceInterface.bookTicket(bookTicketRequest);
							
		}catch(Exception e){
			return new ResponseEntity<BookTicketResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<BookTicketResponse>(response, HttpStatus.OK);
	}

}
