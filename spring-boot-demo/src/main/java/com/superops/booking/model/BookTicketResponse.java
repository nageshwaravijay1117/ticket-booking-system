package com.superops.booking.model;

public class BookTicketResponse {

	private String bookingID;
	private ServerResponse serverResponse;

	

	public String getBookingID() {
		return bookingID;
	}

	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}

	public ServerResponse getServerResponse() {
		return serverResponse;
	}

	public void setServerResponse(ServerResponse serverResponse) {
		this.serverResponse = serverResponse;
	}

	@Override
	public String toString() {
		return "BookTicketResponse [bookingID=" + bookingID + ", serverResponse=" + serverResponse.toString() + "]";
	}

}
