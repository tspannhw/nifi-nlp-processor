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
//	private static final String CURRENT_DIR = System.getProperty("user.dir");
	private static final String CURRENT_DIR = "/Volumes/Transcend/projects/nifi-nlp-processor";
	private static final String CURRENT_FILE = CURRENT_DIR + "/input/en-ner-person.bin";
	private static final String CURRENT_TOKEN_FILE = CURRENT_DIR + "/input/en-token.bin";
	
	/**
	 * sentence to people
	 * @param sentence
	 * @return JSON
	 */
	public String getPeople(String sentence) {
		// 
		String outputJSON = "";
		TokenNameFinderModel model = null;
		InputStream tokenStream = null;
		Tokenizer tokenizer = null;
		try {
		     tokenStream = new FileInputStream( new File(CURRENT_TOKEN_FILE));
			
			model = new TokenNameFinderModel(
					new File(CURRENT_FILE));
			 TokenizerModel tokenModel = new TokenizerModel(tokenStream);
			tokenizer = new TokenizerME(tokenModel);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		return "{\"names\":" + outputJSON + "}";
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
			System.out.println(nameFinder.getPeople(args[j]));
		}
	}

}
