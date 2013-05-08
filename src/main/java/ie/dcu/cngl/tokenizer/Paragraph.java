package ie.dcu.cngl.tokenizer;

import java.util.ArrayList;

public class Paragraph extends ArrayList<Sentence>{

	public Paragraph(ArrayList<Sentence> sentences) {
		this.addAll(sentences);
	}

	public Paragraph() {
		// TODO Auto-generated constructor stub
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
