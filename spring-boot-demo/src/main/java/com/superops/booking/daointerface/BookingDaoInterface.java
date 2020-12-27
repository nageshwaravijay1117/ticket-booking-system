package com.superops.booking.daointerface;

import com.superops.booking.model.BookTicketDB;
import com.superops.booking.model.BookingDetailsDB;

public interface BookingDaoInterface {

	Boolean insertBookingDetails(BookTicketDB bookTicketDB);

	void deleteBookingDetails(String bookingID);

	BookingDetailsDB getBookingStatus(String bookingID);

	Boolean updateBookingStatus(String bookingID, String status);
}
