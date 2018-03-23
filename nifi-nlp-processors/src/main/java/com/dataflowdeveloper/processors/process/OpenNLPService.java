/**
 * 
 */
package com.dataflowdeveloper.processors.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * @author tspann
 *
 */
public class OpenNLPService {
	
	// Public Vars
	public static final String CURRENT_DIR = "src/main/resources/META-INF/input";
	// getClass().getResourceAsStream(); // System.getProperty("user.dir");
	public static final String CURRENT_FILE =  "/en-ner-person.bin"; //CURRENT_DIR + "/input/en-ner-person.bin";
	public static final String CURRENT_TOKEN_FILE =  "/en-token.bin"; //CURRENT_DIR + "/input/en-token.bin";
	public static final String CURRENT_LOCATION_FILE = "/en-ner-location.bin";
	public static final String CURRENT_DATE_FILE = "/en-ner-date.bin";
	
	/**
	 * sentence to dates
	 * @param model Directory
	 * @param sentence
	 * @return List<String>
	 */
	public List<String> getDates(String modelDirectory, String sentence) {
		if ( sentence == null ) {
			return null;
		}
		TokenNameFinderModel model = null;
		InputStream tokenStream = null;
		Tokenizer tokenizer = null;
		List<String> dates = new ArrayList<String>();
		
		if ( modelDirectory == null) {
			modelDirectory = CURRENT_DIR;
		}
		try {
		    tokenStream = new FileInputStream(new File(modelDirectory + CURRENT_TOKEN_FILE)); 			
			model = new TokenNameFinderModel( new File(modelDirectory + CURRENT_DATE_FILE) );  
			
			TokenizerModel tokenModel = new TokenizerModel(tokenStream);
			tokenizer = new TokenizerME(tokenModel);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if ( model != null && sentence != null) { 
			try {
				// Create a NameFinder using the model (Dates)
				NameFinderME finder = new NameFinderME(model);
				
				// Split the sentence into tokens
				String[] tokens = tokenizer.tokenize(sentence);

				// Find the dates in the tokens and return Span objects
				Span[] nameSpans = finder.find(tokens);
				String[] spanns = Span.spansToStrings(nameSpans, tokens);

				for (int i = 0; i < spanns.length; i++) {
					dates.add(spanns[i]);
				}

				finder.clearAdaptiveData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dates;
	}
	
	/**
	 * sentence to people
	 * @param model Directory
	 * @param sentence
	 * @return List<PersonName>
	 */
	public List<PersonName> getPeople(String modelDirectory, String sentence) {
		if ( sentence == null ) {
			return null;
		}
		TokenNameFinderModel model = null;
		InputStream tokenStream = null;
		Tokenizer tokenizer = null;
		List<PersonName> people = new ArrayList<PersonName>();
		
		if ( modelDirectory == null) {
			modelDirectory = CURRENT_DIR;
		}
		try {
		    tokenStream = new FileInputStream(new File(modelDirectory + CURRENT_TOKEN_FILE)); 			
			model = new TokenNameFinderModel( new File(modelDirectory + CURRENT_FILE) );  
			
			TokenizerModel tokenModel = new TokenizerModel(tokenStream);
			tokenizer = new TokenizerME(tokenModel);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if ( model != null && sentence != null) { 
			try {
				// Create a NameFinder using the model
				NameFinderME finder = new NameFinderME(model);

				// Split the sentence into tokens
				String[] tokens = tokenizer.tokenize(sentence);

				// Find the names in the tokens and return Span objects
				Span[] nameSpans = finder.find(tokens);


				String[] spanns = Span.spansToStrings(nameSpans, tokens);
				for (int i = 0; i < spanns.length; i++) {
					people.add(new PersonName(spanns[i]));
				}

				finder.clearAdaptiveData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return people;
	}
	
	/**
	 * 
	 * @param modelDirectory
	 * @param sentence
	 * @return locations as JSON
	 */
	public List<Location> getLocations(String modelDirectory, String sentence) {
		if ( sentence == null ) {
			return null;
		}
		TokenNameFinderModel model = null;
		InputStream tokenStream = null;
		Tokenizer tokenizer = null;
		List<Location> locations = new ArrayList<Location>();
		
		if ( modelDirectory == null) {
			modelDirectory = CURRENT_DIR;
		}
		try {
		    tokenStream = new FileInputStream(new File(modelDirectory + CURRENT_TOKEN_FILE)); 			
			model = new TokenNameFinderModel( new File(modelDirectory + CURRENT_LOCATION_FILE) );  
			
			TokenizerModel tokenModel = new TokenizerModel(tokenStream);
			tokenizer = new TokenizerME(tokenModel);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if ( model != null && sentence != null) { 
			try {
				// Create a NameFinder using the model
				NameFinderME finder = new NameFinderME(model);

				// Split the sentence into tokens
				String[] tokens = tokenizer.tokenize(sentence);

				// Find the names in the tokens and return Span objects
				Span[] nameSpans = finder.find(tokens);

				String[] spanns = Span.spansToStrings(nameSpans, tokens);
				for (int i = 0; i < spanns.length; i++) {
					locations.add(new Location(spanns[i]));
				}
				finder.clearAdaptiveData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return locations;
	}
}