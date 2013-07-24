package ie.dcu.cngl.tokenizer;

import java.util.ArrayList;

/**
 * Represents a paragraph
 * @author Rémi Bois
 *
 */
public class Paragraph extends ArrayList<Sentence>{

	public Paragraph(ArrayList<Sentence> sentences) {
		this.addAll(sentences);
	}

	public Paragraph() {
	}

	/**
	 * Gives a standard representation of a paragraph
	 * @return the sentences of the paragraph in a list
	 */
	public ArrayList<String> getSentences() {
		 ArrayList<String> result = new ArrayList<String>();
		 
		for (Sentence s : this){
			result.add(s.getTokens());
		}
		
		return result;
		
	}
}
