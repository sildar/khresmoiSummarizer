package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Paragraph;
import ie.dcu.cngl.tokenizer.Sentence;
import ie.dcu.cngl.tokenizer.TokenInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Bias factor to score sentences containing query terms more highly.</br>
 * sentence score = (number of query terms in the sentence)^2/number of terms in query
 * @author Shane
 *
 */
public class QueryBiasFeature extends Feature {

    private ArrayList<TokenInfo> query;

    public QueryBiasFeature(ArrayList<TokenInfo> query) throws IOException {
        this.query = query;
    }

    @Override
    public Double[] calculateRawWeights(Double[] weights) {
        final double numQueryTerms = numberOfTerms(query);
        int sentenceNumber = 0;
        Sentence tokenHolder;
        for (Paragraph paragraph : structure.getStructure()) {
            for (Sentence sentence : paragraph) {
                double numOccurences = 0;
                for (TokenInfo queryToken : query) {
                    tokenHolder = new Sentence();
                    tokenHolder.add(queryToken);
                    numOccurences += getNumOccurrences(tokenHolder, sentence);
                }
                weights[sentenceNumber++] = Math.pow(numOccurences, 2) / numQueryTerms;
            }
        }
        return weights;
    }

    @Override
    public double getMultiplier() {
        return SummarizerUtils.queryBiasMultiplier;
    }
}
