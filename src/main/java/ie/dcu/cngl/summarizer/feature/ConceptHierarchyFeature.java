/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.dcu.cngl.summarizer.feature;

import ie.dcu.cngl.WordNetUtils.DepthFinder;
import ie.dcu.cngl.WordNetUtils.ICFinder;
import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.tokenizer.Paragraph;
import ie.dcu.cngl.tokenizer.Sentence;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;

/**
 *
 * @author Dat Tien Nguyen
 */
public class ConceptHierarchyFeature extends Feature {

    private IDictionary dict = null;
    private String icfile = "";
    String vers = "";
    String wnhome = "";
    URL url = null;

    public ConceptHierarchyFeature() throws IOException {
        super();
        vers = "3.0";
        wnhome = "C:/Program Files/WordNet/" + vers + "/dict";
        icfile = "C:/Program Files/WordNet/" + vers + "/WordNet-InfoContent-" + vers + "/ic-semcor.dat";
        url = null;
        try {
            url = new URL("file", null, wnhome);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) {
            return;
        }
        IDictionary dict = new Dictionary(url);
        try {
            dict.open();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Double[] calculateRawWeights(Double[] weights) {
    	//!!Useless
        int sentenceNumber = 0;
        for (Paragraph paragraph : structure.getStructure()) {
            for (Sentence sentence : paragraph) {
                double heirarchy_depth = 0;
                double numTerms = numberOfTerms(sentence);
                for (int i = 0; i < sentence.size(); i++) {
                    String token = sentence.get(i).getValue();
                    token.toLowerCase();

                   //Calculate depth of concept
                    ICFinder icfinder = new ICFinder(icfile);
                    DepthFinder depthfinder = new DepthFinder(dict, icfile);
                    /*
                     * re-compute depth of a concept later
                     * heirarchy_depth += depthfinder.getSynsetDepth(token, 2, "n");
                     */                  
                  
                }
                weights[sentenceNumber++] = heirarchy_depth;
            }
        }
        return weights;
    }

   
    @Override
    public double getMultiplier() {
        return SummarizerUtils.conceptHierarchyMultiplier;
    }
}
