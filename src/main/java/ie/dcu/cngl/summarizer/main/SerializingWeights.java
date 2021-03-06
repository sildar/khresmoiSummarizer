package ie.dcu.cngl.summarizer.main;

import ie.dcu.cngl.summarizer.Weighter;
import ie.dcu.cngl.tokenizer.PageStructure;
import ie.dcu.cngl.tokenizer.Structurer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

public class SerializingWeights {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String text = FileUtils.readFileToString(new File("C:\\Users\\Kid\\Desktop\\Intern\\long.txt"), "UTF-8");
		Structurer structurer = new Structurer();
		PageStructure structure = structurer.getStructure(text);	
		
		Weighter weighter = new Weighter();
		HashMap<String, Double[]> weights = new HashMap<String, Double[]>();
		weighter.setStructure(structure);
		weighter.addAllFeatures();
		weighter.calculateWeights(weights);
		
		try {
			FileOutputStream fileOut = new FileOutputStream(new File("test"));
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(weights);
			out.close();
			fileOut.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
