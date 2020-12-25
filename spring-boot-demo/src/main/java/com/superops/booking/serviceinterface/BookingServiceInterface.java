package com.superops.booking.serviceinterface;

import com.superops.booking.model.BookTicketRequest;
import com.superops.booking.model.BookTicketResponse;

public interface BookingServiceInterface {
	
	public BookTicketResponse bookTicket(BookTicketRequest bookTicketRequest);
	
}
