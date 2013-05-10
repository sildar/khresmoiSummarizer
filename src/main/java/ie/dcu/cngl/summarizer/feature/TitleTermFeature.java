package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Paragraph;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.TokenInfo;

import java.io.IOException;

/**
 * The title of an article often reveals the major subject of that document. Sentences
 * containing terms from the title are likely to be good summarization candidates.</br>
 * sentence score = number of title terms found in sentence/total number of title terms
 * @author Shane
 *
 */
public class TitleTermFeature extends Feature {

	private Sentence titleTokens;

	public TitleTermFeature(Sentence titleTokens) throws IOException {
		this.titleTokens = titleTokens;
	}

	@Override
	public Double[] calculateRawWeights(Double[] weights) {
		final double numTitleTerms = numberOfTerms(titleTokens);
		int sentenceNumber = 0;
		Sentence tokenHolder;
		for(Paragraph paragraph : structure.getStructure()) {
			for(Sentence sentence : paragraph) {
				double numOccurences = 0;
				for(TokenInfo titleToken : titleTokens) {
					tokenHolder = new Sentence();
					tokenHolder.add(titleToken);
					numOccurences+=getNumOccurrences(tokenHolder, sentence);
				}
				weights[sentenceNumber++] = numOccurences/numTitleTerms;
			}
		}
		return weights;
	}

	@Override
	public double getMultiplier() {
		return SummarizerUtils.titleTermMultiplier;
	}

}
