package com.superops.booking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.superops.booking.model.BookTicketDB;
import com.superops.booking.model.BookingDetailsDB;
import com.superops.booking.daointerface.BookingDaoInterface;

@Repository
public class BookingDaoImpl implements BookingDaoInterface {

	@Autowired
	private DataSource dataSource;

	@Value("${update.seat.status}")
	private String updateSeatStatusQuery;

	@Value("${insert.booking}")
	private String insertBookingQuery;

	@Value("${delete.booking}")
	private String deleteBookingQuery;

	@Value("${select.booking.details}")
	private String selectBookingStatusQuery;

	@Value("${update.booking.status}")
	private String updateBookingStatusQuery;

	
//	Create a new Booking entry in Bookings Table
	@Override
	public boolean insertBookingDetails(BookTicketDB bookTicketDB) {
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
	
//	Delete a Booking entry from Bookings Table
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

//	Get the Booking Status and Seats Reserved from Bookings Table
	@Override
	public BookingDetailsDB getBookingStatus(String bookingID) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		BookingDetailsDB bookingDetailsDB = null;

		try {
			bookingDetailsDB = new BookingDetailsDB();
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.prepareStatement(selectBookingStatusQuery);
			stmt.setString(1, bookingID);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				bookingDetailsDB.setBookingStatus(resultSet.getString("BOOKING_STATUS"));
				bookingDetailsDB.setSeatsReserved(resultSet.getString("SEATS_RESERVED"));
			}

//			Closing the resources

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				resultSet.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
		return bookingDetailsDB;

	}

//	Update the Status if the Booking ID in Booking Table
	@Override
	public boolean updateBookingStatus(String bookingID, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.prepareStatement(updateBookingStatusQuery);
			stmt.setString(1, status);
			stmt.setString(2, bookingID);

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
