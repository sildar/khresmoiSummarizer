package ie.dcu.cngl.summarizer;

import ie.dcu.cngl.tokenizer.SectionInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Combines scores from 2 dimensional array into one and ranks sentences accordingly.
 * Simple linear addition is used. Any sentences receiving individual negative
 * scores are discounted completely.
 * @author Shane
 *
 */
public class Aggregator implements IAggregator {

	private ArrayList<SectionInfo> sentences; 

	/**
	 * Combines provided weights.
	 * @param allWeights 2-dimensional array of all weights computed for each sentence
	 * @return Array of sentences with their ranking. Sentences are ordered by score.
	 */
	public ArrayList<SentenceScore> aggregate(HashMap<String, Double[]> allWeights) {
		final int numSentences = sentences.size();
		double[] totalWeights = new double[numSentences];
		ArrayList<SentenceScore> scores = new ArrayList<SentenceScore>();
		
		for (String feature : allWeights.keySet())
		{
			Double[] featureWeights = allWeights.get(feature); 
			for(int i = 0; i < featureWeights.length; i++) {

				if(featureWeights[i] >= 0) {
					totalWeights[i]+=featureWeights[i];
				} else {
					totalWeights[i] = Double.NEGATIVE_INFINITY;
				}
			}
		}

		//Pairing weights with sentences for ranking
		for(int i = 0; i < numSentences; i++) {
			scores.add(new SentenceScore(sentences.get(i).getValue(), totalWeights[i]));
		}

		Collections.sort(scores);

		return scores;
	}

	/**
	 * Sets sentences to be ranked.
	 * @param sentences Array of sentences being considered for summarization
	 */
	@Override
	public void setSentences(ArrayList<SectionInfo> sentences) {
		this.sentences = sentences;
	}
}
