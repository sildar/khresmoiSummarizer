package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Paragraph;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.TokenInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Calculates the number of named entities that occur in each sentence. As NE taggers
 * are quite slow a more naive approach is taken here. Any tokens (except the first), that
 * start with a capital letter are assumed to be named entities.</br>
 * sentence boost = (number of NEs present)^2/number of terms
 * @author Shane
 *
 */
public class NamedEntityFeature extends Feature {

	public NamedEntityFeature() throws IOException {
		super();
	}

	private int calculateNumNamedEntities(Sentence sentence) {
		int numNamedEntities = 0;
		for(int i = 1; i < sentence.size(); i++) {	//Ignore first token
			String token = sentence.get(i).getValue();
			if(Character.isUpperCase(token.charAt(0))) {
				numNamedEntities++;
			}
		}
		return numNamedEntities;
	}

	@Override
	public double getMultiplier() {
		return SummarizerUtils.namedEntityMultiplier;
	}

	@Override
	protected Double[] calculateRawWeights(Double[] weights) {
		//this is used to fill the result structure (keeping track of where we are in the text)
		int sentenceInText = 0;
		
		for(Paragraph paragraph : structure) {
			for (Sentence sentence : paragraph){
				//default value if there is no namedEntites OR if there is 0 nonEmptyWords in the sentence
				double namedEntitiesRatio = 0;
				
				int nonEmptyWords = numberOfTerms(sentence);
				if (nonEmptyWords != 0)
					namedEntitiesRatio = calculateNumNamedEntities(sentence)/nonEmptyWords;

				weights[sentenceInText] = namedEntitiesRatio ;
				sentenceInText++;						
			}
		}
		return weights;
	}

}