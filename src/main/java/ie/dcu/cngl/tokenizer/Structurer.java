package ie.dcu.cngl.tokenizer;

import java.util.ArrayList;

/**
 * A Structurer uses a Paragrapher and Sentenizer
 * to provide 3-dimensional content structure.
 * @author Shane
 *
 */
public class Structurer implements IStructurer {

	private ISentenizer sentenizer;
	private IParagrapher paragrapher;

	/**
	 * Creates a new Struturer.
	 */
	public Structurer() {
		this.sentenizer = Sentenizer.getInstance();
		this.paragrapher = Paragrapher.getInstance();
	}
	
	/**
	 * Gets content structure. A vector of paragraphs, containing a vector
	 * of sentences, containing a vector of tokens.
	 * @param content
	 * @return PageStructure of provided String
	 */
	public PageStructure getStructure(String content) {
		
		PageStructure structure = new PageStructure();
		ArrayList<String> paragraphs = paragrapher.paragraph(content);
		
		for(String paragraph : paragraphs) {
			structure.add(new Paragraph(sentenizer.sentenize(paragraph)));
		}
		
		return new PageStructure(structure);
	}
}
