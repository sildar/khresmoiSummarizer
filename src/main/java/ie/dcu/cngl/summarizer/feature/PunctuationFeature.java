package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.TokenInfo;

import java.io.IOException;

/**
 * Calculates what proportion of the sentence contains punctuation. If this is above
 * a specified threshold the sentence is negatively marked.
 * @author Shane
 */
public class PunctuationFeature extends Feature {
	
	private double maxPunctuationRatio;

	/**
	 * Creates PunctuationFeature with default max punctuation ratio of 0.3.
	 * @throws IOException
	 */
	public PunctuationFeature() throws IOException {
		//Default
		this.maxPunctuationRatio = SummarizerUtils.maxPunctuationRatio == -1 ? 0.3 : SummarizerUtils.maxPunctuationRatio;
	}
	
	@Override
	public FeatureScore getWeights() {
		final int numSentences = structure.getNumSentences();
		Double[] weights = new Double[numSentences];
		
		Sentence sentence;
		for(int i = 0; i < numSentences; i++) {
			sentence = structure.getSentenceTokens(i);
			double punctuationRatio = numPunctuationTokens(sentence)/sentence.size();
			weights[i] = punctuationRatio > maxPunctuationRatio ? -1.0 : 0.0;
		}
		
		return new FeatureScore(this.getClass().getName(), weights);
	}

	private double numPunctuationTokens(Sentence sentence) {
		double numPunctuationTokens = 0;
		
		for(TokenInfo token : sentence) {
			if(token.getValue().matches("\\p{P}+")){
				numPunctuationTokens++;
			}
		}
		
		return numPunctuationTokens;
	}

	@Override
	public double getMultiplier() {
		return SummarizerUtils.punctuationMultiplier != 0 ? 1 : 0;
	}

	@Override
	public Double[] calculateRawWeights(Double[] weights) {
		return weights;
	}

	/**
	 * Set max punctuation ratio. If the ratio of punctuation to terms is above
	 * this figure the sentence is negatively marked.
	 * @param maxPunctuationRatio Max punctuation ration (between 0 and 1)
	 */
	public void setMaxPunctuationRatio(double maxPunctuationRatio) {
		this.maxPunctuationRatio = maxPunctuationRatio;
	}

}
