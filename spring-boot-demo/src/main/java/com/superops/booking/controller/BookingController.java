package com.superops.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superops.booking.model.BookTicketRequest;
import com.superops.booking.model.BookTicketResponse;
import com.superops.booking.model.ConfirmBookingRequest;
import com.superops.booking.model.ServerResponse;
import com.superops.booking.serviceinterface.BookingServiceInterface;

@RestController
@RequestMapping("booking")
public class BookingController {

	@Autowired
	BookingServiceInterface bookingServiceInterface;

//	The Initial Booking Request Reaches here

	@PostMapping("book-ticket")
	public ResponseEntity<BookTicketResponse> bookTicket(@RequestBody BookTicketRequest bookTicketRequest) {

		BookTicketResponse response = null;
		try {
			response = bookingServiceInterface.bookTicket(bookTicketRequest);

		} catch (Exception e) {
			return new ResponseEntity<BookTicketResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<BookTicketResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("make-payment")
	public ResponseEntity<ServerResponse> makePayment() {

		ServerResponse response = null;
		try {
			response = bookingServiceInterface.makePayment();

		} catch (Exception e) {
			return new ResponseEntity<ServerResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("confirm-booking")
	public ResponseEntity<ServerResponse> confirmBooking(@RequestBody ConfirmBookingRequest confirmBookingRequest) {

		ServerResponse response = null;
		try {
			response = bookingServiceInterface.makePayment();

		} catch (Exception e) {
			return new ResponseEntity<ServerResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);
	}
	

	
}
