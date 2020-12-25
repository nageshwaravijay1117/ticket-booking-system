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

	@Override
	public Boolean addSeatIDToRedis(String seatID) {
		Boolean result = false;
		try {
			stringRedisTemplate.opsForValue().setIfAbsent(seatID, seatID);
			stringRedisTemplate.expire(seatID, 120, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public String getSeatIDFromRedis(String seatID) {
		return stringRedisTemplate.opsForValue().get(seatID);
	}

}
