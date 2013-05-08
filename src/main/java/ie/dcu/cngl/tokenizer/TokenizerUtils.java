package ie.dcu.cngl.tokenizer;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

/**
 * Static class containing shared variables and methods.
 * @author Shane
 *
 */
public class TokenizerUtils {
	
	public static final String COMMENT = "#";
	public static final String WHITE_SPACE = " ";
	/**
	 * Recombines 2d array of tokens into a String array. Does this based on location information
	 * derived during tokenization.
	 * @param sections
	 * @return
	 */
	public static ArrayList<String> recombineTokens2d(ArrayList<ArrayList<TokenInfo>> sections) {
		
		ArrayList<String> strSections = new ArrayList<String>();		
		for(ArrayList<TokenInfo> tokens : sections) {
			strSections.add(recombineTokens1d(tokens));
		}		
		return strSections;
	}

	/**
	 * Recombines array of tokens into string form. Does this based on location information
	 * derived during tokenization.
	 * @param sections
	 * @return
	 */
	public static String recombineTokens1d(ArrayList<TokenInfo> tokens) {
		
		return StringUtils.join(tokens, " ");
		
	}
	
}
