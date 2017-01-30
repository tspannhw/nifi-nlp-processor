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
	/**
	 * sentence to people
	 * @param model Directory
	 * @param sentence
	 * @return JSON
	 */
	public String getPeople(String modelDirectory, String sentence) {
		if ( sentence == null ) {
			return "";
		}
		String outputJSON = "";
		TokenNameFinderModel model = null;
		InputStream tokenStream = null;
		Tokenizer tokenizer = null;
		
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

				List<PersonName> people = new ArrayList<PersonName>();
				String[] spanns = Span.spansToStrings(nameSpans, tokens);
				for (int i = 0; i < spanns.length; i++) {
					people.add(new PersonName(spanns[i]));
				}

				outputJSON = new Gson().toJson(people);
				finder.clearAdaptiveData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "{\"names\":" + outputJSON + "}";
	}
	
	/**
	 * 
	 * @param modelDirectory
	 * @param sentence
	 * @return locations as JSON
	 */
	public String getLocations(String modelDirectory, String sentence) {
		if ( sentence == null ) {
			return "";
		}
		String outputJSON = "";
		TokenNameFinderModel model = null;
		InputStream tokenStream = null;
		Tokenizer tokenizer = null;
		
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

				List<Location> locations = new ArrayList<Location>();
				String[] spanns = Span.spansToStrings(nameSpans, tokens);
				for (int i = 0; i < spanns.length; i++) {
					locations.add(new Location(spanns[i]));
				}

				outputJSON = new Gson().toJson(locations);
				finder.clearAdaptiveData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "{\"locations\":" + outputJSON + "}";
	}
	

	/**
	 * tester
	 * @param args
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InvalidFormatException, IOException {

		if (args == null || args.length <= 0) {
			System.out.println("No Data");
			return;
		}
		
		OpenNLPService nameFinder = new OpenNLPService();
		
		for (int j = 0; j < args.length; j++) {
			System.out.println("Input:  " + args[j]);
			System.out.println(nameFinder.getPeople(CURRENT_DIR, args[j]));
			System.out.println(nameFinder.getLocations(CURRENT_DIR, args[j]));
			
		}
	}

}
