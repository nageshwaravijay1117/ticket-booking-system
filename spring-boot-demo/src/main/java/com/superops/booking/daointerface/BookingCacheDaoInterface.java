package com.superops.booking.daointerface;

public interface BookingCacheDaoInterface {

	Boolean addSeatIDToRedis(String seatID);
	String getSeatIDFromRedis(String seatID);
}
