package com.superops.booking.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.superops.booking.constant.BookingConstants;
import com.superops.booking.daointerface.BookingCacheDaoInterface;
import com.superops.booking.daointerface.BookingDaoInterface;
import com.superops.booking.daointerface.SeatStatusDaoInterface;
import com.superops.booking.model.BookTicketDB;
import com.superops.booking.model.BookTicketRequest;
import com.superops.booking.model.BookTicketResponse;
import com.superops.booking.model.BookingDetailsDB;
import com.superops.booking.model.ConfirmBookingRequest;
import com.superops.booking.model.ServerResponse;
import com.superops.booking.serviceinterface.BookingServiceInterface;
import com.superops.booking.utils.UniqueID;

@Service
public class BookingServiceImpl implements BookingServiceInterface {

	@Autowired
	private BookingCacheDaoInterface bookingCacheDaoInterface;

	@Autowired
	private BookingDaoInterface bookingDaoInterface;

	@Autowired
	private SeatStatusDaoInterface seatStatusDaoInterface;

	@Autowired
	private JmsTemplate defaultJmsTemplate;

	@Autowired
	private UniqueID uniqueID;

	@Value("${queue.name}")
	private String queueName;

	@Override
	public BookTicketResponse bookTicket(BookTicketRequest bookTicketRequest) {
		BookTicketResponse bookTicketResponse = null;
		ServerResponse serverResponse = null;
		try {
			bookTicketResponse = new BookTicketResponse();
			serverResponse = new ServerResponse(BookingConstants.ERROR_RESPONSE,
					BookingConstants.INTERNAL_SERVER_ERROR);

			String[] seats = bookTicketRequest.getSeatsReserved().split(",");
//			Validating Maximum Number of Tickets to Be Booked
			if (seats.length > 6) {
				serverResponse.setMessage(BookingConstants.MAX_SEAT_LIMIT_ERROR_RESPONSE);
				serverResponse.setStatusCode(BookingConstants.SUCCESS_RESPONSE_CODE);
				bookTicketResponse.setServerResponse(serverResponse);
				return bookTicketResponse;
			}

//			Adding Seats Into Redis

			ServerResponse addSeatsServerResponse = addSeatsInRedis(seats);
			if (addSeatsServerResponse.getStatusCode() != BookingConstants.SUCCESS_RESPONSE_CODE) {
				bookTicketResponse.setServerResponse(addSeatsServerResponse);
				return bookTicketResponse;
			}

//			Creating Booking Entry in Bookings Table
			BookTicketDB bookTicketDB = convertBookTicketReqToBookTicketDB(bookTicketRequest);

			boolean insertBookingResponse = bookingDaoInterface.insertBookingDetails(bookTicketDB);
			if (!insertBookingResponse) {
				bookTicketResponse.setServerResponse(serverResponse);
				return bookTicketResponse;
			}
//			Updating Seats Available Table
			boolean updateSeatsResponse = updateSeatStatus(bookTicketDB.getSeatsReserved(),
					BookingConstants.SEAT_PENDING_STATUS);
			if (!updateSeatsResponse) {
				bookingDaoInterface.deleteBookingDetails(bookTicketDB.getBookingID());
				bookTicketResponse.setServerResponse(serverResponse);
				return bookTicketResponse;
			}

//			Adding Booking ID to Queue
			defaultJmsTemplate.convertAndSend(queueName, bookTicketDB.getBookingID());

//			Returning Success Response
			bookTicketResponse.setBookingID(bookTicketDB.getBookingID());
			bookTicketResponse.setServerResponse(serverResponse);

		} catch (Exception e) {
			bookTicketResponse.setServerResponse(serverResponse);
			return bookTicketResponse;
		}

		return bookTicketResponse;
	}

//	Adding Seats In To the Redis Cache

	private ServerResponse addSeatsInRedis(String[] seats) {
		ServerResponse serverResponse = null;
		try {
			serverResponse = new ServerResponse("", BookingConstants.SUCCESS_RESPONSE_CODE);
			for (String seat : seats) {
//				If seats already exist in cache return false
				boolean addResponse = bookingCacheDaoInterface.addSeatIDToRedis(seat);
				if (!addResponse) {
					serverResponse.setMessage(BookingConstants.SEAT_NOT_AVAILABLE_ERROR_RESPONSE);
					serverResponse.setStatusCode(BookingConstants.SEAT_NOT_AVAILABLE_ERROR_CODE);
					break;
				}
			}
			if (serverResponse.getStatusCode() == BookingConstants.SEAT_NOT_AVAILABLE_ERROR_CODE) {
//				Removing the previously added keys
				for (String seat : seats) {
					bookingCacheDaoInterface.deleteSeatIDFromRedis(seat);
				}
			}
		} catch (Exception e) {
//			Log the Exception
			return serverResponse;

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
//			Execpetion will be logged with request

		}
		return bookTicketDB;
	}

//	Mock Function for Payment
	@Override
	public ServerResponse makePayment() {
		ServerResponse response = new ServerResponse(BookingConstants.PAYMENT_FAILURE_RESPONSE,
				BookingConstants.FAILURE_RESPONSE_CODE);
		Random random = new Random();
		if (random.nextBoolean()) {
			response.setStatusCode(BookingConstants.SUCCESS_RESPONSE_CODE);
			response.setMessage(BookingConstants.PAYMENT_SUCCESS_RESPONSE);
		}
		return response;
	}

//	Updates the seat status in the My sql CINEMA_HALL_SEAT_STATUS table
	@Override
	public boolean updateSeatStatus(String seatsReserved, String status) {
		try {
			String[] seats = seatsReserved.split(",");
			for (String seatID : seats) {
				boolean updateSeatStatus = seatStatusDaoInterface.updateSeatStatus(seatID, status);
				if (!updateSeatStatus) {
					// Logs will be added for failed update with seat id
					return false;
				}
			}
		} catch (Exception e) {
			// Logs will be added for Exception
			return false;
		}

		return true;
	}

//	Update the Booking Status on the final booking stage
	@Override
	public ServerResponse confirmBookingStatus(ConfirmBookingRequest confirmBookingRequest) {
		ServerResponse serverResponse = null;
		try {
			serverResponse = new ServerResponse(BookingConstants.ERROR_RESPONSE,
					BookingConstants.INTERNAL_SERVER_ERROR);
			if (confirmBookingRequest.getBookingStatus()) {

//				Update the Booking Status if payment is true
				boolean updateBookingStatus = bookingDaoInterface.updateBookingStatus(
						confirmBookingRequest.getBookingID(), BookingConstants.BOOKING_CONFIRMED_STATUS);
				if (!updateBookingStatus) {
					return serverResponse;
				}
			} else {

//				Change the status of the seats in pending state
				BookingDetailsDB bookingDetailsDB = bookingDaoInterface
						.getBookingStatus(confirmBookingRequest.getBookingID());
				if (bookingDetailsDB.getSeatsReserved().length() > 0
						&& bookingDetailsDB.getBookingStatus().length() > 0) {
					boolean updateSeatsResponse = updateSeatStatus(bookingDetailsDB.getSeatsReserved(),
							BookingConstants.SEAT_AVAILABLE_STATUS);
					if (!updateSeatsResponse) {
						return serverResponse;
					}
				} else {
					serverResponse.setMessage(BookingConstants.INVALID_BOOKING_ID_ERROR_RESPONSE);
					serverResponse.setStatusCode(BookingConstants.SUCCESS_RESPONSE_CODE);
					return serverResponse;
				}
//				Update the status of Booking id as failed
				boolean updateBookingStatus = bookingDaoInterface.updateBookingStatus(
						confirmBookingRequest.getBookingID(), BookingConstants.BOOKING_FAILED_STATUS);
				if (!updateBookingStatus) {
					return serverResponse;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			return serverResponse;
		}
		serverResponse.setMessage(BookingConstants.BOOKING_SUCCESS_RESPONSE);
		serverResponse.setStatusCode(BookingConstants.SUCCESS_RESPONSE_CODE);
		return serverResponse;
	}
}
