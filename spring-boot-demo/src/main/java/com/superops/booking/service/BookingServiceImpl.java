package com.superops.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.superops.booking.constant.BookingConstants;
import com.superops.booking.daointerface.BookingCacheDaoInterface;
import com.superops.booking.daointerface.BookingDaoInterface;
import com.superops.booking.model.BookTicketDB;
import com.superops.booking.model.BookTicketRequest;
import com.superops.booking.model.BookTicketResponse;
import com.superops.booking.model.ServerResponse;
import com.superops.booking.serviceinterface.BookingServiceInterface;
import com.superops.booking.utils.UniqueID;

@Service
public class BookingServiceImpl implements BookingServiceInterface {

	@Autowired
	BookingCacheDaoInterface bookingCacheDaoInterface;

	@Autowired
	BookingDaoInterface bookingDaoInterface;

	@Autowired
	JmsTemplate defaultJmsTemplate;

	@Autowired
	UniqueID uniqueID;

	@Value("${queue.name}")
	private String queueName;

	@Override
	public BookTicketResponse bookTicket(BookTicketRequest bookTicketRequest) {
		BookTicketResponse bookTicketResponse = null;
		try {
			bookTicketResponse = new BookTicketResponse();

//			Adding Seats Into Redis
			String[] seats = bookTicketRequest.getSeatsReserved().split(",");
			ServerResponse serverResponse = addSeatsInRedis(seats);
			if (serverResponse.getStatusCode() != 0) {
				bookTicketResponse.setServerResponse(serverResponse);
				return bookTicketResponse;
			}

//			Creating Booking Entry in Bookings Table
			BookTicketDB bookTicketDB = convertBookTicketReqToBookTicketDB(bookTicketRequest);

			Boolean insertBookingResponse = bookingDaoInterface.insertBookingDetails(bookTicketDB);
			if (!insertBookingResponse) {
				serverResponse.setMessage("Something Went Wrong! Please Try Again Later");
				serverResponse.setStatusCode(500);
				bookTicketResponse.setServerResponse(serverResponse);
				return bookTicketResponse;
			}

//			Updating Seats Available Table
			for (String seat : seats) {
				Boolean updateSeatsResponse = bookingDaoInterface.updateSeatStatus(seat,
						BookingConstants.SEAT_PENDING_STATUS);
				if (!updateSeatsResponse) {
					bookingDaoInterface.deleteBookingDetails(bookTicketDB.getBookingID());
					serverResponse.setMessage("Something Went Wrong! Please Try Again Later");
					serverResponse.setStatusCode(500);
					bookTicketResponse.setServerResponse(serverResponse);
					return bookTicketResponse;
				}
			}

//			Adding Booking ID to Queue
			defaultJmsTemplate.convertAndSend(queueName, bookTicketDB);

//			Returning Success Response
			bookTicketResponse.setBookingID(bookTicketDB.getBookingID());

		} catch (Exception e) {

		}

		return bookTicketResponse;
	}

	private ServerResponse addSeatsInRedis(String[] seats) {
		ServerResponse serverResponse = null;
		try {
			serverResponse = new ServerResponse();
			for (String seat : seats) {
				String seatFromRedis = bookingCacheDaoInterface.getSeatIDFromRedis(seat);
				if (seat.equals(seatFromRedis)) {
					serverResponse.setMessage("Seats are Not Available");
					serverResponse.setStatusCode(200);
					return serverResponse;
				} else {
					Boolean addResponse = bookingCacheDaoInterface.addSeatIDToRedis(seat);
					if (!addResponse) {
						serverResponse.setMessage("Something Went Wrong! Please Try Again Later");
						serverResponse.setStatusCode(500);
						return serverResponse;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return serverResponse;
	}

	private BookTicketDB convertBookTicketReqToBookTicketDB(BookTicketRequest bookTicketRequest) {
		BookTicketDB bookTicketDB = null;
		try {
			bookTicketDB = new BookTicketDB();
			bookTicketDB.setBookingID(uniqueID.generateUniqueID());
			bookTicketDB.setBookingStatus(BookingConstants.BOOKING_PENDING_STATUS);
			bookTicketDB.setCinemaHallID(bookTicketRequest.getCinemaHallID());
			bookTicketDB.setCustomerID(bookTicketRequest.getCustomerID());
			bookTicketDB.setMovieID(bookTicketRequest.getMovieID());
			bookTicketDB.setSeatsReserved(bookTicketRequest.getSeatsReserved());
			bookTicketDB.setShowDetailID(bookTicketRequest.getShowDetailID());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bookTicketDB;
	}
}
