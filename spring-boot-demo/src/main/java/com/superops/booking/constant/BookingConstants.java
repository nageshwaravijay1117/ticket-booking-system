package com.superops.booking.constant;

public class BookingConstants {
	
	public static final String SEAT_PENDING_STATUS="PENDING";
	public static final String SEAT_ALLOTED_STATUS="ALLOTED";
	public static final String SEAT_AVAILABLE_STATUS="AVAILABLE";
	public static final String BOOKING_PENDING_STATUS="PENDING";
	public static final String BOOKING_CONFIRMED_STATUS="CONFRMED";
	public static final String BOOKING_FAILED_STATUS="FAILED";
	
	public static final String ERROR_RESPONSE="Something Went Wrong! Please Try Again Later";
	public static final String MAX_SEAT_LIMIT_ERROR_RESPONSE="You Book Only 6 Tickets at a Time";
	public static final String SEAT_NOT_AVAILABLE_ERROR_RESPONSE="Seats are Not Available";
	public static final String PAYMENT_FAILURE_RESPONSE="Payment Failed";
	public static final String PAYMENT_SUCCESS_RESPONSE="Payment Successful";
	public static final String BOOKING_SUCCESS_RESPONSE="Tickets Booked Successfully";
	public static final String INVALID_BOOKING_ID_ERROR_RESPONSE="Invalid Booking ID";
	
	public static final int SUCCESS_RESPONSE_CODE=200;
	public static final int FAILURE_RESPONSE_CODE=400;
	public static final int INTERNAL_SERVER_ERROR=500;
//	Unable to allocate new task for index.
	public static final int SEAT_NOT_AVAILABLE_ERROR_CODE=5020;
	
}
