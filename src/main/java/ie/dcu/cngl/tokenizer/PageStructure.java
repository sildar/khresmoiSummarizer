package ie.dcu.cngl.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Encapsulates the 3-dimensional structure of a page. The content's
 * paragraphs, the paragraphs' sentences, the sentences' tokens.
 * @author Shane
 *
 */
public class PageStructure extends ArrayList<Paragraph>{
	
	private ArrayList<SectionInfo> sentences;
	private ArrayList<SectionInfo> paragraphs;
	private HashMap<Integer, Integer> sentenceToParagraph;
	private HashMap<Integer, Integer> sentenceToRelativePosition;
	
	public PageStructure(){
		
	}

	public PageStructure(ArrayList<? extends Paragraph> structure) {
		this.addAll(structure);
		this.sentences = getSentencesPriv();
		this.paragraphs = getParagraphsPriv();
		this.sentenceToParagraph = new HashMap<Integer, Integer>();
		this.sentenceToRelativePosition = new HashMap<Integer, Integer>();
		mapSentencesToParagraphs();
	}

	/**
	 * Maps each sentence to its paragraph to aid later retrieval
	 */
	private void mapSentencesToParagraphs() {
		int sentenceNumber = 0, paragraphNumber = 0, sentenceParagraphStarter = 0;
		for(Paragraph paragraph : this) {
			int numSentences = paragraph.size();
			for(int i = 0; i < numSentences; i++) {
				sentenceToParagraph.put(sentenceNumber, paragraphNumber);
				sentenceToRelativePosition.put(sentenceNumber, sentenceParagraphStarter);
				sentenceNumber++;
			}
			sentenceParagraphStarter = sentenceNumber;
			paragraphNumber++;
		}
	}

	private ArrayList<SectionInfo> getSentencesPriv() {
		return getSectionInfo(this);
	}
	
	private ArrayList<SectionInfo> getParagraphsPriv() {
		//Prior to calling getSectionInfo we need all tokens of each paragraph in one array
		
		ArrayList<Paragraph> paragraphsHolder = new ArrayList<Paragraph>();
		
		for(Paragraph paragraph : this) {
			paragraphsHolder.add(paragraph);
		}
		
		return getSectionInfo(paragraphsHolder);
	}
	
	/**
	 * Gets the tokens of the specified tokens in the specified paragraph.
	 * @param sentenceNumber The sentence number of the desired tokens.
	 * @param paragraphNumber The paragraph number containing the desired sentence.
	 * @return The tokens of the desired sentence, or null if it doesn't exist.
	 */
	public Sentence getSentenceFromParagraphTokens(int sentenceNumber, int paragraphNumber) {
		try {
			return this.get(paragraphNumber).get(sentenceNumber);
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Retrieve the tokens of the specified sentence.
	 * @param sentenceNumber The absolute sentence number of the desired sentence.
	 * @return
	 */
	public Sentence getSentenceTokens(int sentenceNumber) {
		return getSentenceFromParagraphTokens(sentenceNumber-sentenceToRelativePosition.get(sentenceNumber), sentenceToParagraph.get(sentenceNumber));
	}
	
	/**
	 * Get the raw 3-demensional page structure.
	 * @return The 3-dimensional page structure.
	 */
	public PageStructure getStructure() {
		return this;
	}
	
	/**
	 * Get the content sentences.
	 * @return An array of the content sentences with their absolute positions.
	 */
	public ArrayList<SectionInfo> getSentences() {
		return this.sentences;
	}
	
	/**
	 * Get the content paragraphs.
	 * @return An array of the content paragraphs with their absolute positions.
	 */
	public ArrayList<SectionInfo> getParagraphs(){
		return this.paragraphs;
	}
	
	/**
	 * @return The number of sentences in the content.
	 */
	public int getNumSentences() {
		return this.sentences.size();
	}

	/**
	 * @return The number of paragraphs in the content.
	 */
	public int getNumParagraphs() {
		return this.paragraphs.size();
	}
	
	private ArrayList<SectionInfo> getSectionInfo(ArrayList<Paragraph> paragraphsHolder) {
		ArrayList<SectionInfo> sections = new ArrayList<SectionInfo>();
		int sectionCount = 0;
		for(Paragraph paragraph : paragraphsHolder) {
			ArrayList<String> strSentences = paragraph.getSentences();
			for(String sentence : strSentences) {
				SectionInfo sentenceInfo = new SectionInfo(sentence, sectionCount);
				sections.add(sentenceInfo);
				sectionCount++;
			}
		}
		return sections;
	}
	

}
