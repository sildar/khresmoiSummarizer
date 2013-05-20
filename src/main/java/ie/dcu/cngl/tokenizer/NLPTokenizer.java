package ie.dcu.cngl.tokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * Breaks content into basic units.
 * @author Johannes Levelling
 */
public class NLPTokenizer implements ITokenizer {
	
	private static NLPTokenizer instance;
    private TokenizerME tokenizer;


    private NLPTokenizer() {
    	
    	InputStream modelIn;
		try {
			modelIn = new FileInputStream(this.getClass().getResource(TokenizerUtils.tokenModelFile).getFile());
			TokenizerModel model = new TokenizerModel(modelIn);
			tokenizer = new TokenizerME(model);
	    	
			
		} catch (IOException e) {
			System.err.println("Couldn't load the tokenizer model");
			e.printStackTrace();
		}

    }

    /**
     * Initializing a tokenizer is computationally expensive, so it exists as a singleton.
     * @return Tokenizer singleton.
     */
    public static NLPTokenizer getInstance() {
    	if(instance == null) {
    		synchronized(Tokenizer.class) {
    			instance = new NLPTokenizer();
    		}
		}
    	return instance;
    }


    public Sentence tokenize(String s) {
        
        
        ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(tokenizer.tokenize(s)));
        
        Sentence tokenInfos = new Sentence();
        
        for (String token : tokens){
        	tokenInfos.add(new TokenInfo(token));
        }
        return tokenInfos;
        
    }

}
