package com.superops.booking.daointerface;

public interface BookingCacheDaoInterface {

	boolean addSeatIDToRedis(String seatID);
	String getSeatIDFromRedis(String seatID);
	boolean deleteSeatIDFromRedis(String seatID);
}
