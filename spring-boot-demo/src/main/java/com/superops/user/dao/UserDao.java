package com.superops.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.superops.user.daointerface.UserDaoInterface;

@Repository
public class UserDao implements UserDaoInterface {

	@Autowired
	DataSource dataSource;

	@Value("${select.user}")
	private String loginQuery;

	@Override
	public String validateUser(String userID, String password) {
		String uuid = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet=null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.prepareStatement(loginQuery);
			stmt.setString(1, userID);
			stmt.setString(2, password);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				uuid = resultSet.getString("CUSTOMER_ID");
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
		return uuid;
	}

}
