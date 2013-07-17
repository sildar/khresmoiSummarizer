package ie.dcu.cngl.summarizer;

import ie.dcu.cngl.summarizer.feature.TitleTermFeature;
import ie.dcu.cngl.tokenizer.IStructurer;
import ie.dcu.cngl.tokenizer.PageStructure;
import ie.dcu.cngl.tokenizer.Paragraph;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 * Provides a sentence extracted summary of provided content.
 * There are three stages in the summarization pipeline - </br>
 * Tokenization: 
 * The summarizer uses a Structurer to not only tokenize the content,
 * but to structure the content by sentences and paragraphs.</br>
 * Weighting: 
 * Using the content structure and chosen features the weighter assigns
 * weights to each sentence.</br>
 * Aggregation: The aggregator combines the weights from each feature.
 * It may decide to completely discount those negatively weighted, combine
 * them linearly, logarithmically, or in any other fashion chosen. The sentences are
 * then ranked. </br>
 * The summarizer finishes by outputting the number of sentences desired
 * according to their score.
 * 
 * @author Shane
 *
 */
public class Summarizer {

	private Tokenizer tokenizer;
	private IStructurer structurer;	
	private IWeighter weighter;
	private IAggregator aggregator;

	private int numSentences;
	private String title;
	private String query;

	private HashMap<String, Double[]> weights;

	private PrintWriter pw;

	/**
	 * Creates new summarizer with provided components.
	 * @param structurer Extracts content structure
	 * @param weighter Weights sentences and paragraphs
	 * @param aggregator Combines scores and ranks sentences
	 */
	public Summarizer(IStructurer structurer, IWeighter weighter, IAggregator aggregator) {
		this.tokenizer = Tokenizer.getInstance();
		this.weighter = weighter;
		this.aggregator = aggregator;
		this.structurer = structurer;
		this.numSentences = 2;	//Default number of sentences
		this.weights = new HashMap<String, Double[]>();
		try{
			pw = new PrintWriter(new File("./results.txt"));
		}catch (IOException e){
			System.out.println();
		}
	}

	/**
	 * Sets the number sentences to be returned by the summarizer.
	 * @param numSentences
	 */
	public void setNumSentences(int numSentences) {
		this.numSentences = numSentences;
	}

	/**
	 * Provides sentence extracted summary of provided content.
	 * @param content to be summarized
	 * @return summary of provided content
	 */
	public String summarize(String content) {
		if(StringUtils.isEmpty(content)) {
			return StringUtils.EMPTY;
		}

		PageStructure structure = structurer.getStructure(content);	
		weighter.setStructure(structure);
		weighter.setTitle(structure.getSentenceTokens(0));

		//weighter.setTitle(StringUtils.isNotEmpty(title) ? tokenizer.tokenize(title) : null);
		weighter.setQuery(StringUtils.isNotEmpty(query) ? tokenizer.tokenize(query) : null);
		aggregator.setSentences(structure.getSentences());

		weights.clear();
		try{
		weighter.addFeature(new TitleTermFeature(structure.getSentenceTokens(0)));
		}catch(IOException e){
			System.err.println("No title found");
		}
		weighter.calculateWeights(weights);



		pw.append(structure.getSentenceTokens(0).getTokens() + "\n");
		
		for (int i = 0; i<structure.getNumSentences(); i++){

			pw.append(structure.getSentenceTokens(i).getTokens() + "\n");
			double total = 0;

			for (String featureName : weights.keySet()){
				double score = weights.get(featureName)[i];
				total += score;
				pw.append("\t" + featureName + "\t" + score + "\n");
			}
			pw.append("\t Total \t" + total + "\n\n");
		}

		ArrayList<SentenceScore> scores = aggregator.aggregate(weights);

		String summary = StringUtils.EMPTY;
		//best ranked extraction
		
		for(int i = 0; i < numSentences; i++) {
			summary+=(scores.get(i).getSentence() + "\n");
		}
		
		
		
		//random extraction
		/*
		int maxi = scores.size();
		Random rand = new Random();
		
		for (int i = 0; i< numSentences; i++) {
			int randomi = rand.nextInt(maxi);
			
			while (scores.get(randomi).getScore() <= 0){
				randomi = rand.nextInt(maxi);
			}
			
			summary+= (scores.get(randomi).getSentence() + "\n");
		}
		*/
		
		//intro extraction
		/*
		int total = 0;
		int numParag = 0;
		int numSent = 0;
		while (total < numSentences){
			//go to next paragraph is there's no more sentence in the previous one
			if (numSent >= structure.get(numParag).getSentences().size()){
				numParag++;
				numSent = 0;
			}
			//take a sentence only if
			if (structure.get(numParag).getSentences().get(numSent).length() > 30){
				summary += structure.get(numParag).getSentences().get(numSent) + "\n";
				total++;
			}
			numSent++;			
		}
*/
		summary = beautifulString(summary);
		
		return summary;
	}

	/**
	 * Delete the whitespace before some punctuation marks (,?.)
	 * @param summary the string to process
	 * @return the processed string, without spaces before punctuation mark
	 */
	private String beautifulString(String summary) {
		String result = summary.replaceAll("\\s([,?.;)])", "$1");
		result = result.replaceAll("([(])\\s", "$1");
		return result;
	}

	/**
	 * Set content title
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set query related to the content
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/** 
	 * Provide pre-calculated weights that will be combined with
	 * weights calculated on new run.
	 * @param weights Pre-calculated weights
	 */
	public void setWeights(HashMap<String, Double[]> weights) {	
		this.weights = weights;
	}

}