package com.superops.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.superops.booking.constant.BookingConstants;
import com.superops.booking.daointerface.BookingDaoInterface;
import com.superops.booking.model.BookingDetailsDB;
import com.superops.booking.serviceinterface.BookingServiceInterface;

@Service
public class BookingListener {

	@Autowired
	BookingDaoInterface bookingDaoInterface;

	@Autowired
	BookingServiceInterface bookingServiceInterface;

	@JmsListener(destination = "booking_queue")
	public void ValidateBooking(String bookingID) {
		try {
			BookingDetailsDB bookingDetailsDB = bookingDaoInterface.getBookingStatus(bookingID);
//			Validating the Booking ID 
			if (bookingDetailsDB.getSeatsReserved().length() > 0
					&& bookingDetailsDB.getBookingStatus().length() > 0) {
//				Log Invalid Booking ID with booking id
			}
//			Update Status of Booking ID as Failed if Status is not CONFIRMED
			if (!bookingDetailsDB.getBookingStatus().equals(BookingConstants.BOOKING_CONFIRMED_STATUS)) {
				Boolean updateOperationStatus = bookingDaoInterface.updateBookingStatus(bookingID,
						BookingConstants.BOOKING_FAILED_STATUS);
				if (!updateOperationStatus) {
//				Logs will be added for failed update with booking id	
				}
//				Reset The Seats Status as AVAILABLE
				Boolean updateSeatStatus = bookingServiceInterface.updateSeatStatus(bookingDetailsDB.getBookingStatus(),
						BookingConstants.SEAT_AVAILABLE_STATUS);
				if (!updateSeatStatus) {
//					Logs will be added for failed update with booking id
				}

			}

		} catch (Exception e) {
//			Logs will be added for Exception Occurred	
		}

	}

}
