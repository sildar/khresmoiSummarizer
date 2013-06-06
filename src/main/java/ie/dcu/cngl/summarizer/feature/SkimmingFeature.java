package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Paragraph;

import java.io.IOException;

/**
 * Applies a linear deboost function to simulate the manual heuristic of
 * summarizing by skimming the first few sentences off a paragraph.
 */
public class SkimmingFeature extends Feature {


	public SkimmingFeature() throws IOException {
		super();
	}


	@Override
	public double getMultiplier() {
		return SummarizerUtils.skimmingMultiplier;
	}

	@Override
	protected Double[] calculateRawWeights(Double[] weights) {
		//this is used to fill the result structure (keeping track of where we are in the text)
		int sentenceInText = 0;
		
		for(Paragraph paragraph : structure) {
			
			int sentencesInParagraph = paragraph.size();
			
			for(int i=0; i< sentencesInParagraph; i++) {
				//the upper the sentence in the paragraph, the better the score.
				weights[sentenceInText] = (double) (sentencesInParagraph - i)/sentencesInParagraph;
				sentenceInText++;
			}
		}
		return weights;
	}

}
