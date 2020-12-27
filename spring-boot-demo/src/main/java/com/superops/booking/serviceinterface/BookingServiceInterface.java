package com.superops.booking.serviceinterface;

import com.superops.booking.model.BookTicketRequest;
import com.superops.booking.model.BookTicketResponse;
import com.superops.booking.model.ConfirmBookingRequest;
import com.superops.booking.model.ServerResponse;

public interface BookingServiceInterface {

	BookTicketResponse bookTicket(BookTicketRequest bookTicketRequest);

	ServerResponse makePayment();

	ServerResponse confirmBookingStatus(ConfirmBookingRequest confirmBookingRequest);

	boolean updateSeatStatus(String seatsReserved, String status);

}
