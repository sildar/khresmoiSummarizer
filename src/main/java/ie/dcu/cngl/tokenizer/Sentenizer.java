package ie.dcu.cngl.tokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * Separates the tokenized content into sentences.
 * @author Johannes Levelling
 *
 */
public class Sentenizer implements ISentenizer {
	
	private static Sentenizer instance;
    private Tokenizer tokenizer = null;
    private SentenceDetectorME sentenceDetector;
    
    private Sentenizer(Tokenizer tokenizer) {
    	try{
    	this.tokenizer = tokenizer;

    	InputStream modelIn = new FileInputStream("./en-sent.bin");
    	SentenceModel model = new SentenceModel(modelIn);
    	sentenceDetector = new SentenceDetectorME(model);

    	
    	}
    	catch(IOException e){
    		System.err.println("Impossible to read the model");
    		e.printStackTrace();
    	}
    }

    /**
     * Initializing a sentenizer is computationally expensive, so it exists as a singleton.
     * @return Sentenizer singleton.
     */
    public static Sentenizer getInstance() {
    	if(instance == null) {
    		synchronized(Sentenizer.class) {
	    		Tokenizer tokenizer = Tokenizer.getInstance();
	    		instance = new Sentenizer(tokenizer);
    		}
    	}
    	return instance;
    }


    /**
     * Tokenize the content, and divide the tokens by sentence.
     * @return A 2-dimensional array of each sentence and its tokens.
     */  
    public synchronized ArrayList<ArrayList<TokenInfo>> sentenize(String s) {
    	
    	ArrayList<String> sentences = new ArrayList<String>(Arrays.asList(sentenceDetector.sentDetect(s)));
    	ArrayList<ArrayList<TokenInfo>> result = new ArrayList<ArrayList<TokenInfo>>();
    	
    	for (String sentence : sentences){
    		result.add(tokenizer.tokenize(sentence));
    	}
    	
    	return result;
    }

}

