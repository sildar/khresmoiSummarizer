package ie.dcu.cngl.tokenizer;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a tokenized sentence.
 * @author Rémi Bois
 *
 */
public class Sentence extends ArrayList<TokenInfo>{
	
	public Sentence(){
	}

	/**
	 * Creates a Sentence with an ArrayList of TokenInfo
	 * @param sentence the sentence to create
	 */
	public Sentence(ArrayList<TokenInfo> sentence) {
		this.addAll(sentence);
	}

	/**
	 * String representation of a sentence
	 * @return the sentence as a String
	 */
	public String getTokens() {

		return StringUtils.join(this," ");
	}

}
