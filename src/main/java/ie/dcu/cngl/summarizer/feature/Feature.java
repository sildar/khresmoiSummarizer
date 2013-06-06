package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.tokenizer.PageStructure;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.TokenInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenStream;

/**
 * A feature is used to score sentences based on certain criteria.
 * @author Shane
 *
 */
public abstract class Feature {

	protected SummaryAnalyzer analyzer;
	protected PageStructure structure;

	public Feature() throws IOException {
		this.analyzer = new SummaryAnalyzer();
	}

	/**
	 * Sets structure to be used during scoring
	 * @param structure Structure of content
	 */
	public void setStructure(PageStructure structure) {
		this.structure = structure;
	}

	/**
	 * Multiplier used on feature scores
	 * @return feature multiplier
	 */
	public abstract double getMultiplier();

	/**
	 * Calculates weights without normalisation or applying multiplier.
	 * @param weights
	 * @return
	 */
	protected abstract Double[] calculateRawWeights(Double[] weights);

	/**
	 * Calculates sentence weights, normalises scores and applies multiplier.
	 * @return sentence scores
	 */
	public FeatureScore getWeights() {
		Double[] weights = new Double[structure.getNumSentences()];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 0.0;
		}
		weights = calculateRawWeights(weights);
		normalise(weights);
		applyMultiplier(weights);
		return new FeatureScore(this.getClass().getName(), weights);
	}

	/**
	 * Counts the number of occurrences of searchStr in longerStr
	 * @param searchStr String occurrences being counted
	 * @param longerStr String being searched
	 * @return the number of occurrences
	 */
	protected int getNumOccurrences(Sentence searchStr, Sentence longerStr) {
		return StringUtils.countMatches(longerStr.getTokens(), searchStr.getTokens());
	}

	/**
	 * Counts the number of search terms that appear in an array of tokens
	 * @param searchTerms A map containing all search terms
	 * @param longerStr An array of tokens being checked
	 * @return The crossover count
	 */
	protected int getCrossoverCount(HashMap<String, ArrayList<ArrayList<TokenInfo>>> searchTerms, ArrayList<TokenInfo> longerStr) {
		int numOccurences = 0, tokenIndex = 0;


		for (TokenInfo token : longerStr) {
			ArrayList<ArrayList<TokenInfo>> possibleMatches = searchTerms.get(token.getValue().toLowerCase());
			if (possibleMatches != null) {
				for (ArrayList<TokenInfo> searchTokens : possibleMatches) {
					int numSearchTokens = searchTokens.size();
					boolean match = numSearchTokens <= longerStr.size() - tokenIndex;	//Is there enough tokens left for a match?
					for (int i = 1; i < numSearchTokens && match; i++) {	//First already matches => j = 1
						String nextSearchToken = searchTokens.get(i).getValue();
						String nextLongStrToken = longerStr.get(tokenIndex + i).getValue();
						match = nextSearchToken.equalsIgnoreCase(nextLongStrToken);
					}
					if (match) {
						numOccurences++;
						break;
					}
				}
			}
			tokenIndex++;
		}

		//		for (TokenInfo token : longerStr) {
		//			for (HashSet<TokenInfo> terms : searchTerms.values()){
		//				if (terms.contains(token)){
		//					numOccurences++;
		//					break;
		//				}
		//			}
		//			
		//		}

		return numOccurences;
	}

	/**
	 * Generates multi-map (key can go to numerous values) from list of token arrays. The first token of each token array is used
	 * as the key for that sentence.
	 * @param listTokens A list of token arrays (such as tokenized sentences)
	 * @return
	 */
	protected HashMap<String, ArrayList<ArrayList<TokenInfo>>> generateMultiMap(ArrayList<ArrayList<TokenInfo>> listTokens) {
		HashMap<String, ArrayList<ArrayList<TokenInfo>>> multiMap = new HashMap<String, ArrayList<ArrayList<TokenInfo>>>();
		for (ArrayList<TokenInfo> tokens : listTokens) {
			ArrayList<ArrayList<TokenInfo>> existingValues = multiMap.get(tokens.get(0).getValue());
			if (existingValues == null) {
				existingValues = new ArrayList<ArrayList<TokenInfo>>();
			}
			existingValues.add(tokens);
			multiMap.put(tokens.get(0).getValue().toLowerCase(), existingValues);
		}
		//Orders in decreasing order of size. 
		for (String key : multiMap.keySet()) {
			Collections.sort(multiMap.get(key), new Comparator<ArrayList<TokenInfo>>() {

				@Override
				public int compare(ArrayList<TokenInfo> list1, ArrayList<TokenInfo> list2) {
					if (list1.size() > list2.size()) {
						return -1;
					}
					if (list2.size() > list1.size()) {
						return 1;
					}
					return 0;
				}
			});
		}
		return multiMap;
	}

	/**
	 * Counts the number of important terms in a sentence (ignores punctuation, stopwords, numbers etc)
	 * @param query
	 * @return
	 */
	protected int numberOfTerms(ArrayList<TokenInfo> query) {
		int numTerms = 0;
		Sentence queryAsSentence = new Sentence(query);
		TokenStream tokenStream = analyzer.tokenStream(null, queryAsSentence);

		try {
			while (tokenStream.incrementToken()) {
				numTerms++;
			}
		} catch (IOException e) {
		}

		return numTerms;
	}

	private void normalise(Double[] weights) {
		double max = getMax(weights);
		if (max != 0) {
			for (int i = 0; i < weights.length; i++) {
				weights[i] /= max;
			}
		}
	}

	private void applyMultiplier(Double[] weights) {
		final double multiplier = getMultiplier();
		for (int i = 0; i < weights.length; i++) {
			weights[i] *= multiplier;
		}
	}

	private double getMax(Double[] weights) {
		double max = weights[0];
		for (int i = 1; i < weights.length; i++) {
			if (weights[i] > max) {
				max = weights[i];
			}
		}
		return max;
	}

}


