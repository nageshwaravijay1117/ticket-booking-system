package com.superops.booking.daointerface;

import com.superops.booking.model.BookTicketDB;
import com.superops.booking.model.BookingDetailsDB;


public interface BookingDaoInterface {
	public Boolean updateSeatStatus(String seatID,String status);
	public Boolean insertBookingDetails(BookTicketDB bookTicketDB);
	public void deleteBookingDetails(String bookingID);
	public BookingDetailsDB getBookingStatus(String bookingID);
	public Boolean updateBookingStatus(String bookingID,String status);
}
