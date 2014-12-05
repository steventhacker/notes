package com.notes.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.notes.mysql.DatabaseInteraction;

@Controller
@RequestMapping(method = RequestMethod.POST)
public class NotesController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(NotesController.class);

	/**
	 * Creates initial text file and DB table for each note taking session.  
	 * 
	 * @param sessionId session that is initially named
	 * @param sessionName name that will be used for reference
	 * 
	 * @throws Exception
	 */
	
	@RequestMapping("/createSession")
	public String setUpSession(
			Model model, 
			@RequestParam(value="sessionId") String sessionId,
			@RequestParam(value="sessionName") String sessionName) 
					throws Exception {
		
		// Create text file to store questions & answers temporarily. Used to populate DB table when session is finalized.
		File file = new File(sessionId+".txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write("Session: "+sessionName+"\n");
		bufferedWriter.close();
		
		// Create mysql table based on sesionId
		DatabaseInteraction.createSessionTable(sessionId);
		DatabaseInteraction.insertSessionMetaData(sessionId, sessionName);
		
		model.addAttribute("sessionId", sessionId);
		
		return "newcard";
	}
	
	/**
	 * Retrieves all questions & answers from database
	 * 
	 * @return map of flashcards
	 */
	@RequestMapping("/flashcards")
	public String viewFlashCards(
			Model model,
			@RequestParam(value = "flashcardSession") String sessionId) {
		
		// Get flashcards from database
		HashMap<String, String> flashcards = DatabaseInteraction.getRandomFlashcard(sessionId);
		//HashMap<String, String> flashcards = DatabaseInteraction.getFlashcards(sessionId);
		String sessionName = DatabaseInteraction.getSessionName(sessionId);
		
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("sessionName", sessionName);
		model.addAttribute("flashcards", flashcards);
		
		return "flashcards";
	}

	/**
	 * Writes each question and answer combination to local text file for temporary storage. 
	 * 
	 * @param sessionId  session that is initially named
	 * @param question  Question sent from front end
	 * @param answer  Answer sent from front end
	 * 
	 */
	@RequestMapping("/question")
	public String insertQuestion(
			Model model, 
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "question") String question,
			@RequestParam(value = "answer") String answer) {
		
		// Retrieves text file that questions & answers are written to
		try {
			File file = new File(sessionId+".txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			// Write questions and answers to temporary text file
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			if (!question.isEmpty() && !answer.isEmpty()) {
				bufferedWriter.write(question + "\n");
				bufferedWriter.write(answer + "\n");
			}
			bufferedWriter.close();
			
			// Pass back question & answer with session so user has verification that info is written
			model.addAttribute("sessionId", sessionId);
			model.addAttribute("question", question);
			model.addAttribute("answer", answer);
			}
		catch (Exception e) {
			LOGGER.warn("Eception when writing file for question/answer input", e);
			model.addAttribute("error", "Could not write to file");
		}		
		
		model.addAttribute("sessionId", sessionId);
		
		return "newcard";
	}
	
	/**
	 * Copies all question & answer combinations and saves them to database for permanent storage.	
	 *
	 * @param sessionId session that is initially named
	 *
	 */
	@RequestMapping("/finalize")
	public String finalizeQuestions(
			Model model,
			@RequestParam(value = "sessionId") String sessionId) {
		
		// Insert data from current session into newly created database table
		DatabaseInteraction.insertQuestion(sessionId);
		
		// Retrieve all tables for results page
		HashMap<String, String> tables = DatabaseInteraction.getTables();
		model.addAttribute("tables", tables);
		
		return "results";
	}
	
	/**
	 * Simple way to return to the results page from a flashcard reading.
	 */
	@RequestMapping("/goToResults")
	public String goToResults(
			Model model,
			@RequestParam(value = "sessionId") String sessionId) {
		
		// Retrieve all tables for results page
		HashMap<String, String> tables = DatabaseInteraction.getTables();
		
		model.addAttribute("tables", tables);				
		model.addAttribute(sessionId);
		
		return "results";
	}
}
