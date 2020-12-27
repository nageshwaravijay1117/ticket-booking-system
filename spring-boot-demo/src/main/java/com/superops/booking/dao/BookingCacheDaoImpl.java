package com.superops.booking.dao;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.superops.booking.daointerface.BookingCacheDaoInterface;

@Repository
public class BookingCacheDaoImpl implements BookingCacheDaoInterface {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

//	Add seat to Redis if SeatID is not already present
	@Override
	public boolean addSeatIDToRedis(String seatID) {
		boolean setStatus = false;
		setStatus = stringRedisTemplate.opsForValue().setIfAbsent(seatID, seatID, 120, TimeUnit.SECONDS);
		return setStatus;
	}

//	Get value of the provided key
	@Override
	public String getSeatIDFromRedis(String seatID) {
		return stringRedisTemplate.opsForValue().get(seatID);
	}

//	Delete key from Redis
	@Override
	public boolean deleteSeatIDFromRedis(String seatID) {
		return stringRedisTemplate.delete(seatID);
	}

}
