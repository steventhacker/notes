package com.notes.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInteraction {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInteraction.class);
	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	
	public static void connectToDatabase() {
		
		// Connect to database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/flashcards","steve", "password");
		} catch (SQLException e) {
			LOGGER.warn("Could not insert into meta table", e);
		} catch (ClassNotFoundException e) {
			LOGGER.warn("Class not found for meta table", e);
		}
	}
	
	/**
	 * Creates a new database table based on current session ID
	 * 
	 * @param sessionId Session ID created when note session is started
	 */
	public static void createSessionTable(String sessionId) {
		
		try {
			// Connect to database
			connectToDatabase();
			statement = connection.createStatement();
			// Create new table in database for current Session
			String sql = "CREATE TABLE "+sessionId+" (id int auto_increment, " +
					"question varchar(250), answer varchar(250), primary key(id))";
			statement.executeUpdate(sql);			
		} catch (Exception e) {
			LOGGER.warn("Could not create table for session", e);
		}
		
	}
	
	/**
	 * Inserts sessionId and sessionName into separate DB for retrieving meta data
	 * 
	 * @param sessionId
	 * @param sessionName
	 */
	public static void insertSessionMetaData(String sessionId, String sessionName) {
		
		try {
			// Connect to database
			connectToDatabase();
			// Insert current session info into meta table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS sessionNames (id int auto_increment, " +
					"sessionId varchar(250), sessionName varchar(250), primary key(id))");
			statement.executeUpdate("INSERT INTO sessionNames (sessionId, sessionName) VALUES ( '"+sessionId+"', '"+sessionName+"' )");
			
		} catch (SQLException e) {
			LOGGER.warn("Could not insert into meta table", e);
		}
		
	}
	
	/**
	 * Inserts new question & answer combination into database
	 * 
	 * @param sessionId Session ID created when note session is started
	 */
	public static void insertQuestion(String sessionId) {
		
		// Retrieve file from local system
		File file = new File(sessionId+".txt");
		try {
			// Question & answer strings to pull data from file and insert into database
			String question;
			String answer;
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			connectToDatabase();			
			statement = connection.createStatement();
			// Write question & answer data to DB, skipping initial line that stores the session title
			while ((question = bufferedReader.readLine()) != null) {
				if (question.startsWith("Session", 0)) {
					question = bufferedReader.readLine();
				}
				answer = bufferedReader.readLine();
				PreparedStatement preparedStatement = 
						connection.prepareStatement("INSERT INTO "+sessionId+" (question, answer) VALUES (?, ?)");
				preparedStatement.setString(1, question);
				preparedStatement.setString(2, answer);
				preparedStatement.executeUpdate();
				preparedStatement.close();				
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Could not find file for database write", e);
		} catch (IOException e) {
			LOGGER.error("IO Exception on database write", e);
		} catch (SQLException e) {
			LOGGER.error("SQL Exception on database write", e);
		}
	}
	
	/**
	 * Retrieves the session name easily.
	 *
	 * @return  sessionName
	 */
	public static String getSessionName(String sessionId) {
		
		String sessionName = "";
		// Connect to database
		connectToDatabase();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
						"SELECT * FROM sessionNames WHERE sessionId='"+sessionId+"'");
			while (resultSet.next()) {
				// Set sessionName variable to retrieved sessionName column
				sessionName = resultSet.getString("sessionName");
			}
		} catch (SQLException e) {
			LOGGER.warn("Could not retrieve session name from database", e);
		}
				
		return sessionName;
	}
	
	/**
	 * Retrieves all questions & answers for flashcards based on given session
	 * 
	 * @param sessionId  specific session
	 * @return  all questions and answers for a session
	 */
	public static HashMap<String, String> getFlashcards(String sessionId) {
		
		HashMap<String, String> flashcards = new HashMap<String, String>();
		
		// Connect to database
		connectToDatabase();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM "+sessionId);
			while (resultSet.next()) {
				// Insert retrieved DB data into map
				flashcards.put(resultSet.getString("question"), resultSet.getString("answer"));
			}
		} catch (SQLException e) {
			LOGGER.warn("Could not retrieve flashcard data", e);
		}
		
		return flashcards;
	}
	
	/**
	 * Instead of getting all questions and answers, get one random pair
	 * 
	 * @param sessionId  specific session
	 * @return  one random question & answer 
	 */
	public static HashMap<String, String> getRandomFlashcard(String sessionId) {
		
		HashMap<String, String> flashcard = new HashMap<String, String>();
		
		// Connect to database
		HashMap<String, String> fullCards = getFlashcards(sessionId);
		Random rand = new Random();
		
		// Keys from the full set of questions & answers
		ArrayList<String> keys = new ArrayList<String>(fullCards.keySet());
		
		// Get question & answer based on random key
		String randomQuestion = keys.get(rand.nextInt(keys.size()));
		String randomAnswer = fullCards.get(randomQuestion);		
		flashcard.put(randomQuestion, randomAnswer);
		
		return flashcard;
	}
	
	/**
	 * Gets tables to show on results page
	 * 
	 * @return session IDs and names
	 */
	public static HashMap<String, String> getTables() {
		
		HashMap<String, String> tables = new HashMap<String, String>();
		
		try {
			// Connect to database
			connectToDatabase();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM sessionNames");
			while (resultSet.next()) {
				tables.put(resultSet.getString("sessionId"), resultSet.getString("sessionName"));
			}
			
		} catch (SQLException e) {
			LOGGER.warn("Could not get table list", e);
		}
		// This shouldn't ever happen
		if (tables.size() < 1) {
			tables.put("No data could be retrieved", "No data");
		}
		
		return tables;		
	}
}
