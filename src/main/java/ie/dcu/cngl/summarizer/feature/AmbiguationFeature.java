/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.TokenInfo;

import java.io.IOException;
import java.util.ArrayList;

import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * Calculate the number of synsets of all term in sentences
 * sentence score = number of synset/number of terms in document
 * @author Dat Tien Nguyen
 */
public class AmbiguationFeature extends Feature {

    private int numberTermsOfDocument;

    public AmbiguationFeature() throws IOException {
        super();
        numberTermsOfDocument = 0;
        for (ArrayList<ArrayList<TokenInfo>> paragraph : structure.getStructure()) {
            for (ArrayList<TokenInfo> sentence : paragraph) {
                numberTermsOfDocument += numberOfTerms(sentence);
            }
        }
    }

    @Override
    public Double[] calculateRawWeights(Double[] weights) {
        int sentenceNumber = 0;
        for (ArrayList<ArrayList<TokenInfo>> paragraph : structure.getStructure()) {
            for (ArrayList<TokenInfo> sentence : paragraph) {
                int numSynset = 0;
                for (int i = 0; i < sentence.size(); i++) {
                    String token = sentence.get(i).getValue();
                    System.setProperty("wordnet.database.dir", "C:\\Program Files\\WordNet\\3.0\\dict\\");
                    WordNetDatabase database = WordNetDatabase.getFileInstance();
                    numSynset += database.getSynsets(token.toLowerCase()).length;
                }
                weights[sentenceNumber++] = 2 * (double) numSynset / numberTermsOfDocument; //number or synset in document
            }
        }
        return weights;
    }

    @Override
    public double getMultiplier() {
        return SummarizerUtils.ambiguationMultiplier;
    }
}
