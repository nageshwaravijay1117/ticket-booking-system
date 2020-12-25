package com.superops.booking.model;

public class BookTicketDB {

	private String bookingID;
	private String cinemaHallID;
	private String movieID;
	private String customerID;
	private String showDetailID;
	private String seatsReserved;
	private String bookingStatus;


	@Override
	public String toString() {
		return "BookTicketDB [bookingID=" + bookingID + ", cinemaHallID=" + cinemaHallID + ", movieID=" + movieID
				+ ", customerID=" + customerID + ", showDetailID=" + showDetailID + ", seatsReserved=" + seatsReserved
				+ ", bookingStatus=" + bookingStatus + "]";
	}

	public String getBookingID() {
		return bookingID;
	}

	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}

	public String getCinemaHallID() {
		return cinemaHallID;
	}

	public void setCinemaHallID(String cinemaHallID) {
		this.cinemaHallID = cinemaHallID;
	}

	public String getMovieID() {
		return movieID;
	}

	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getShowDetailID() {
		return showDetailID;
	}

	public void setShowDetailID(String showDetailID) {
		this.showDetailID = showDetailID;
	}

	public String getSeatsReserved() {
		return seatsReserved;
	}

	public void setSeatsReserved(String seatsReserved) {
		this.seatsReserved = seatsReserved;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}


}
