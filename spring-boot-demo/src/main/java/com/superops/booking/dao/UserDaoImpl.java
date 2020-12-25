package com.superops.booking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.superops.booking.daointerface.UserDaoInterface;
import com.superops.booking.model.User;


import org.springframework.data.redis.core.StringRedisTemplate;

@Repository
public class UserDaoImpl implements UserDaoInterface {

	@Autowired
	DataSource dataSource;
	
	 @Autowired
	  private StringRedisTemplate stringRedisTemplate;

	@Value("${select.user}")
	private String loginQuery;

	@Override
	public User checkLogin(User user) {
		User response = null;
		response = new User();
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.prepareStatement(loginQuery);
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());

			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {

				response.setUid(resultSet.getString("UID"));
				response.setUserName(resultSet.getString("UserName"));
				response.setPassword(resultSet.getString("Password"));
				response.setIsAdmin(resultSet.getInt("IsAdmin"));
			}

//			Closing the resources
			resultSet.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return response;
	}

	@Override
	 public void addUserName(String uname) {
		  stringRedisTemplate.opsForValue().setIfAbsent(uname, uname);
		  stringRedisTemplate.expire(uname, 60, TimeUnit.SECONDS);
	  }
}
