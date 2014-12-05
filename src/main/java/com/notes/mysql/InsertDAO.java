package com.notes.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertDAO implements DataInsertDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertDAO.class);
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void insert(String question, String answer) {
		
		String sql = "INSERT INTO flashcards (question, answer) VALUES (?, ?)";
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, question);
			preparedStatement.setString(2, answer);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			LOGGER.warn("Could not insert database entry", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.warn("Could not close database connection on insert", e);
				}
			}
		}
		
	}

	
}
