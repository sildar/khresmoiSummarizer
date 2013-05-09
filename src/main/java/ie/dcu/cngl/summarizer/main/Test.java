package ie.dcu.cngl.summarizer.main;

import ie.dcu.cngl.summarizer.Aggregator;
import ie.dcu.cngl.summarizer.Summarizer;
import ie.dcu.cngl.summarizer.Weighter;
import ie.dcu.cngl.tokenizer.Structurer;
import java.io.File;


import org.apache.commons.io.FileUtils;


public class Test {
	public static void main(String [] args) throws Exception {
		long startTime = System.nanoTime();
		String text = FileUtils.readFileToString(new File(Test.class.getResource("/data/en/textExample.txt").getFile()), "UTF-8");
		
		Structurer structurer = new Structurer();
		Weighter weighter = new Weighter();
		Aggregator aggregator = new Aggregator();
               
		Summarizer summarizer = new Summarizer(structurer, weighter, aggregator);
		summarizer.setNumSentences(5);
		String summary = summarizer.summarize(text);
                System.out.println("****** Print summary after ******");
               	System.out.println(summary);
               	assert(false);
        		long endTime = System.nanoTime();

        		long duration = endTime - startTime;
        		System.out.println(duration/1000000);
	}
}
