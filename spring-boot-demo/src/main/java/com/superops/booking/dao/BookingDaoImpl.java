package com.superops.booking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.superops.booking.model.BookTicketDB;

import com.superops.booking.daointerface.BookingDaoInterface;

@Repository
public class BookingDaoImpl implements BookingDaoInterface {

	@Autowired
	DataSource dataSource;

	@Value("${update.seat.status}")
	private String updateSeatStatusQuery;

	@Value("${insert.booking}")
	private String insertBookingQuery;

	@Value("${delete.booking}")
	private String deleteBookingQuery;

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
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;

	}

	@Override
	public Boolean insertBookingDetails(BookTicketDB bookTicketDB) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.prepareStatement(insertBookingQuery);
			stmt.setString(1, bookTicketDB.getBookingID());
			stmt.setString(2, bookTicketDB.getCinemaHallID());
			stmt.setString(3, bookTicketDB.getMovieID());
			stmt.setString(4, bookTicketDB.getCustomerID());
			stmt.setString(5, bookTicketDB.getSeatsReserved());
			stmt.setString(6, bookTicketDB.getShowDetailID());
			stmt.setString(7, bookTicketDB.getBookingStatus());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected == 0) {
				return false;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;

	}

	@Override
	public void deleteBookingDetails(String bookingID) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.prepareStatement(deleteBookingQuery);
			stmt.setString(1, bookingID);

			stmt.executeUpdate();
	

//			Closing the resources

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
