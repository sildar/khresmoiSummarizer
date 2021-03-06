package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Paragraph;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.Tokenizer;

import java.io.IOException;
import java.util.HashMap;

/**
 * CuePhraseFeature checks for specified indicator phrases in sentences. These phrases may indicate that the sentence
 * is good for summarization, or that it is not. </br>
 * sentence score = number of occurrences of cue phrase * cue phrase score
 * @author Shane
 *
 */
public class CuePhraseFeature extends TermCheckingFeature {
	
	private HashMap<Sentence, Integer> cuePhrases;

	public CuePhraseFeature() throws IOException {
		this.cuePhrases = new HashMap<Sentence, Integer>();
        Tokenizer tokenizer = Tokenizer.getInstance();
		for(String line : terms) {
		    line = line.toLowerCase();
		    String [] phraseAndWeight = line.split(",");
		    cuePhrases.put(tokenizer.tokenize(phraseAndWeight[0].trim()), Integer.parseInt(phraseAndWeight[1].trim()));
        }
	}

	@Override
	public Double[] calculateRawWeights(Double[] weights) {
		int sentenceNumber = 0;
		for(Paragraph paragraph : structure) {
			for(Sentence sentence : paragraph) {
				for(Sentence cuePhrase : cuePhrases.keySet()) {
					weights[sentenceNumber]+=(getNumOccurrences(cuePhrase, sentence)*cuePhrases.get(cuePhrase));
				}
				sentenceNumber++;
			}
		}
		return weights;
	}
	
	@Override
	public double getMultiplier() {
		return SummarizerUtils.cuePhraseMultiplier;
	}

	@Override
	public String getTermsFileName() {
		return SummarizerUtils.cuePhrasesFile;
	}

}
