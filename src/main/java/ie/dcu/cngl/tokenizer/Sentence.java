package ie.dcu.cngl.tokenizer;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class Sentence extends ArrayList<TokenInfo>{

	public String getTokens() {

		return StringUtils.join(this," ");
	}

}
