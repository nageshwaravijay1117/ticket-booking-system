package com.superops.booking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.superops.booking.daointerface.SeatStatusDaoInterface;

@Repository
public class SeatStatusDaoImpl implements SeatStatusDaoInterface {

	@Autowired
	private DataSource dataSource;

	@Value("${update.seat.status}")
	private String updateSeatStatusQuery;

//	Updates the seat status in the CINEMA_HALL_SEAT_STATUS table
	@Override
	public Boolean updateSeatStatus(String seatID, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.prepareStatement(updateSeatStatusQuery);
			stmt.setString(1, status);
			stmt.setString(2, seatID);

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected == 0) {
				return false;
			}

//			Closing the resources

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

}
