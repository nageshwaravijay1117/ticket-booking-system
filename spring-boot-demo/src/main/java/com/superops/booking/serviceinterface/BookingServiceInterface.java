package com.superops.booking.serviceinterface;

import com.superops.booking.model.BookTicketRequest;
import com.superops.booking.model.BookTicketResponse;
import com.superops.booking.model.ConfirmBookingRequest;
import com.superops.booking.model.ServerResponse;

public interface BookingServiceInterface {
	
	public BookTicketResponse bookTicket(BookTicketRequest bookTicketRequest);
	public ServerResponse makePayment();
	public ServerResponse confirmBookingStatus(ConfirmBookingRequest confirmBookingRequest);
	public Boolean updateSeatStatus(String seatsReserved, String status);
	
	
}
