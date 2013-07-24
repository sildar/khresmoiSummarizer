package ie.dcu.cngl.summarizer.main;

import ie.dcu.cngl.summarizer.Aggregator;
import ie.dcu.cngl.summarizer.Summarizer;
import ie.dcu.cngl.summarizer.SummarizerUtils;
import ie.dcu.cngl.summarizer.Weighter;
import ie.dcu.cngl.tokenizer.Structurer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;


public class Test {
	public static void main(String [] args) throws Exception {

		ArrayList<String> fileList = new ArrayList<String>();
		ArrayList<String> texts = new ArrayList<String>();
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> abstracts = new ArrayList<String>();
		/*
		File folder = new File("./expData/en/4training/");
		for (File fileEntry : folder.listFiles()){
			fileList.add(fileEntry.getName());
		}
		
		for (String filename : fileList){
			texts.add(FileUtils.readFileToString(new File("./expData/en/4training/" + filename)));
			titles.add(FileUtils.readFileToString(new File("./expData/en/titles/" + filename)));
			abstracts.add(FileUtils.readFileToString(new File("./expData/en/abs/" + filename)));
		}
		*/
		
		final File folder = new File("./corpus/endook/");

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getName().contains("ok") && fileEntry.isFile()){
				fileList.add(fileEntry.getAbsolutePath());
			}
		}
		

		System.out.println(fileList.size() + " files to process");

		for (String filename : fileList){
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String title = br.readLine();
			titles.add(title);
			String abstr = br.readLine();
			abstracts.add(abstr);
			String text = br.readLine();
			while (text != null){
				sb.append(text);
				sb.append("\n");
				text = br.readLine();
			}
			texts.add(sb.toString());
			br.close();
		}
	
		System.out.println("Starting summarization");

		long startTime = System.nanoTime();

		Structurer structurer = new Structurer();
		Weighter weighter = new Weighter();
		weighter.addAllFeatures();
		Aggregator aggregator = new Aggregator();

		int i = 0;
		for (String text : texts){
			PrintWriter pwabs = new PrintWriter(new File("expData/abstracts/"+i+".abs"));
			PrintWriter pwsumm = new PrintWriter(new File("expData/summaries/"+i+".summ"));

			Summarizer summarizer = new Summarizer(structurer, weighter, aggregator);
			summarizer.setNumSentences(4);
			summarizer.setTitle(titles.get(i));

			String summary = summarizer.summarize(text);
			//System.out.println("****** Print summary after ******");
			//System.out.println(summary);
			//System.out.println("REFERENCE : " + abstracts.get(i));
			pwabs.append(abstracts.get(i));
			pwsumm.append(summary);
			pwabs.close();
			pwsumm.close();
			i++;
		}
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		System.out.println(duration/1000000000);
	}
}
