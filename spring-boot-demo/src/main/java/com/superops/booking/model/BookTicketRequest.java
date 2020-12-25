package com.superops.booking.model;

//import com.fasterxml.jackson.annotation.JsonAlias;

public class BookTicketRequest {
//	@JsonAlias({"CinemaHallID"})
	
	private String cinemaHallID;
	private String movieID;
	private String customerID;
	private String showDetailID;
	private String seatsReserved;

	
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

	@Override
	public String toString() {
		return "BookTicketRequest [cinemaHallID=" + cinemaHallID + ", movieID=" + movieID + ", customerID=" + customerID
				+ ", showDetailID=" + showDetailID + ", seatsReserved=" + seatsReserved + "]";
	}


}
