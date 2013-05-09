package ie.dcu.cngl.tokenizer;

import ie.dcu.cngl.summarizer.SummarizerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;

/**
 * Static class containing shared variables and methods.
 * @author Shane
 *
 */
public class TokenizerUtils {

	public static String tokenModelFile;
	public static String sentenceModelFile;
	public static final String COMMENT = "#";
	public static final String WHITE_SPACE = " ";


	static{
		try{
			XMLConfiguration config = new XMLConfiguration(SummarizerUtils.class.getResource("/config/tokeniser.xml"));

			List<Object> supportedLanguages = config.getList("languages.language");

			String langISO3 = Locale.getDefault().getLanguage();
			//set to english if the language is not supported
			langISO3 = supportedLanguages.contains(langISO3) ? langISO3 : "en";
			
			tokenModelFile = "/data/" + langISO3 + "/" + config.getString("models.token");
			sentenceModelFile = "/data/" + langISO3 + "/" + config.getString("models.sentence");
			
		}catch(ConfigurationException e){
			e.printStackTrace();
		}
	}

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
