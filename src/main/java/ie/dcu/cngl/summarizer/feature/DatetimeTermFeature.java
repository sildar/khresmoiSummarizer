/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Paragraph;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.TokenInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Span;

/**
 * Calculates the number of special words (date, time ..) that occur in each sentence.
 * This may be useful for a user that is not overly familiar with more technical language</br>
 * sentence score = number of occurrence/number of terms in sentence
 * @author Dat Tien Nguyen
 */
public class DatetimeTermFeature extends TermCheckingFeature {

	private HashMap<String, ArrayList<ArrayList<TokenInfo>>> datetimeTerm;

	public DatetimeTermFeature() throws IOException {
		ArrayList<ArrayList<TokenInfo>> wordsList = new ArrayList<ArrayList<TokenInfo>>();
		for (String line : terms) {
			line = line.toLowerCase().trim();
			ArrayList<TokenInfo> tokenLine = new ArrayList<TokenInfo>();
			tokenLine.add(new TokenInfo(line));
			wordsList.add(tokenLine);
		}
		this.datetimeTerm = generateMultiMap(wordsList);
		// System.out.println(datetimeTerm.size());
		// System.out.println("Datetime term feature loaded sucessfully!");
	}

	@Override
	public Double[] calculateRawWeights(Double[] weights) {

		int sentenceNumber = 0;
		for (Paragraph paragraph : structure.getStructure()) {
			for (Sentence sentence : paragraph) {
				double numOccurences = 0;
				numOccurences += getCrossoverCount(datetimeTerm, sentence);
				weights[sentenceNumber++] = numOccurences;
			}
		}

		/*
		//!!Test
		try{
			TokenNameFinderModel mod = new TokenNameFinderModel(new FileInputStream("./en-time.bin"));
			NameFinderME nameFinder = new NameFinderME(mod);

			String text = "";
			for (Paragraph paragraph : structure.getStructure()) {
				for (Sentence sentence : paragraph) {
					text = sentence.getTokens();
					Span spans[] = nameFinder.find(text.split(" "));
					if (spans.length != 0){
						System.out.println(Arrays.toString(Span.spansToStrings(spans, text.split(" "))));
						//System.out.println(text);
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		//!!end Test
		 * */

		return weights;
	}

	@Override
	public double getMultiplier() {
		return SummarizerUtils.datetimeTermMultiplier;
	}

	@Override
	public String getTermsFileName() {
		return SummarizerUtils.datetimeTermFile;
	}
}
