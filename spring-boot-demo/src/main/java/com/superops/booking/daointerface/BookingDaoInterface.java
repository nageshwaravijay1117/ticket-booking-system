package com.superops.booking.daointerface;

import com.superops.booking.model.BookTicketDB;


public interface BookingDaoInterface {
	public Boolean updateSeatStatus(String seatID,String status);
	public Boolean insertBookingDetails(BookTicketDB bookTicketDB);
	public void deleteBookingDetails(String bookingID);

}
