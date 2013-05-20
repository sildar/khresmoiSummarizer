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
 * @author Remi Bois
 *
 */
public class NLPSentenizer implements ISentenizer {
	
	private static NLPSentenizer instance;
    private NLPTokenizer tokenizer = null;
    private SentenceDetectorME sentenceDetector;
    
    private NLPSentenizer(NLPTokenizer tokenizer) {
    	try{
    	this.tokenizer = tokenizer;
    	InputStream modelIn = new FileInputStream(this.getClass().getResource(TokenizerUtils.sentenceModelFile).getFile());
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
    public static NLPSentenizer getInstance() {
    	if(instance == null) {
    		synchronized(NLPSentenizer.class) {
    			NLPTokenizer tokenizer = NLPTokenizer.getInstance();
	    		instance = new NLPSentenizer(tokenizer);
    		}
    	}
    	return instance;
    }


    /**
     * Tokenize the content, and divide the tokens by sentence.
     * @return A 2-dimensional array of each sentence and its tokens.
     */  
    public Paragraph sentenize(String s) {
    	
    	ArrayList<String> sentences = new ArrayList<String>(Arrays.asList(sentenceDetector.sentDetect(s)));
    	Paragraph result = new Paragraph();
    	
    	for (String sentence : sentences){
    		result.add(tokenizer.tokenize(sentence));
    	}
    	
    	return result;
    }

}

