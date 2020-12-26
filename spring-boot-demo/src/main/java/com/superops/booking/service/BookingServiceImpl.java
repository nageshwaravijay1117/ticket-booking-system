package com.superops.booking.service;

import java.util.Random;

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
import com.superops.booking.model.BookingDetailsDB;
import com.superops.booking.model.ConfirmBookingRequest;
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
		ServerResponse serverResponse = null;
		try {
			bookTicketResponse = new BookTicketResponse();
			serverResponse = new ServerResponse("Something Went Wrong! Please Try Again Later", 500);

//			Adding Seats Into Redis
			String[] seats = bookTicketRequest.getSeatsReserved().split(",");
			ServerResponse addSeatsServerResponse = addSeatsInRedis(seats);
			if (addSeatsServerResponse.getStatusCode() != 200) {
				bookTicketResponse.setServerResponse(addSeatsServerResponse);
				return bookTicketResponse;
			}

//			Creating Booking Entry in Bookings Table
			BookTicketDB bookTicketDB = convertBookTicketReqToBookTicketDB(bookTicketRequest);

			Boolean insertBookingResponse = bookingDaoInterface.insertBookingDetails(bookTicketDB);
			if (!insertBookingResponse) {
				bookTicketResponse.setServerResponse(serverResponse);
				return bookTicketResponse;
			}
//			Updating Seats Available Table
			Boolean updateSeatsResponse = updateSeatStatus(bookTicketDB.getSeatsReserved(),
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

	private ServerResponse addSeatsInRedis(String[] seats) {
		ServerResponse serverResponse = null;
		try {
			serverResponse = new ServerResponse("", 200);
			for (String seat : seats) {
				String seatFromRedis = bookingCacheDaoInterface.getSeatIDFromRedis(seat);
				if (seat.equals(seatFromRedis)) {
					serverResponse.setMessage("Seats are Not Available");
					serverResponse.setStatusCode(204);
					return serverResponse;
				} else {
					Boolean addResponse = bookingCacheDaoInterface.addSeatIDToRedis(seat);
					if (!addResponse) {
						serverResponse.setMessage("Something Went Wrong! Please Try Again Later");
						return serverResponse;
					}
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
			// TODO: handle exception

		}
		return bookTicketDB;
	}

	@Override
	public ServerResponse makePayment() {
		ServerResponse response = new ServerResponse("Payment Failed", 400);
		Random random = new Random();
		if (random.nextBoolean()) {
			response.setStatusCode(200);
			response.setMessage("Payment Successful");
		}
		return response;
	}

	@Override
	public Boolean updateSeatStatus(String seatsReserved, String status) {
		try {
			String[] seats = seatsReserved.split(",");
			for (String seatID : seats) {
				Boolean updateSeatStatus = bookingDaoInterface.updateSeatStatus(seatID, status);
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

	@Override
	public ServerResponse confirmBookingStatus(ConfirmBookingRequest confirmBookingRequest) {
		ServerResponse serverResponse = null;
		try {
			serverResponse = new ServerResponse("Something Went Wrong! Please Try Again Later", 500);
			if (confirmBookingRequest.getBookingStatus()) {
				Boolean updateBookingStatus = bookingDaoInterface.updateBookingStatus(
						confirmBookingRequest.getBookingID(), BookingConstants.BOOKING_CONFIRMED_STATUS);
				if (!updateBookingStatus) {
					return serverResponse;
				}
			} else {
				BookingDetailsDB bookingDetailsDB = bookingDaoInterface
						.getBookingStatus(confirmBookingRequest.getBookingID());
				if (bookingDetailsDB.getSeatsReserved().length() > 0
						&& bookingDetailsDB.getBookingStatus().length() > 0) {
					Boolean updateSeatsResponse = updateSeatStatus(bookingDetailsDB.getSeatsReserved(),
							BookingConstants.SEAT_AVAILABLE_STATUS);
					if (!updateSeatsResponse) {
						return serverResponse;
					}
				} else {
					serverResponse.setMessage("Invalid Booking ID");
					serverResponse.setStatusCode(200);
					return serverResponse;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			return serverResponse;
		}
		serverResponse.setMessage("Booked Successfully");
		serverResponse.setStatusCode(200);
		return serverResponse;
	}
}
