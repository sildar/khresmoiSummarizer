/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.dcu.cngl.summarizer.main;

import edu.smu.tspell.wordnet.*;
import edu.smu.tspell.wordnet.impl.*;
import edu.smu.tspell.wordnet.impl.file.*;
import edu.smu.tspell.wordnet.impl.file.synset.*;

/**
 *
 * @author Kid
 */
public class SynsetCounter {

    public static void main(String[] args) {
        
        NounSynset nounSynset;
        NounSynset[] hyponyms;

        System.setProperty("wordnet.database.dir", "C:\\Program Files\\WordNet\\3.0\\dict\\");
        WordNetDatabase database = WordNetDatabase.getFileInstance();
       
        Synset[] synsets = database.getSynsets("cat");

        System.out.println("There are: " + synsets.length + " synsets of word 'cat'");

       // for (int i = 0; i < synsets.length; i++) {
           // nounSynset = (NounSynset) (synsets[i]);
          //  hyponyms = nounSynset.getHyponyms();
            //System.err.println(nounSynset.getWordForms()[0]
           //         + ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");

      //  }

    }
}
