package ie.dcu.cngl.summarizer.main;

import org.apache.commons.io.FileUtils;
import java.io.File;
import dragon.ir.summarize.ROUGE;

/**
 * <p>A Program for Summarizaiton Evaluation</p>
 * <p>We only implemented and tested the ROUGE-N metric so far</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Drexel University</p>
 * @author Xiaodan Zhang, Davis Zhou
 * @version 1.0
 */
public class ROUGEEval {

    public static void main(String[] args) throws Exception {
        //Loading summary and reference
        String summary_path = "expData/summaries/";
        String abs_path = "expData/abstracts/";
        String filename = "";

        String abs_text = "";
        String summary_text = "";
        String[] refSummaries = new String[1];
        ROUGE r1 = new ROUGE();
        r1.setMultipleReferenceMode(3);
        r1.useRougeN(2);

        Double precisionSum = 0.0;
        Double recalSum = 0.0;
        //r1.setStopwordFile("rouge.stopwordfr");
        int numOfFiles = 0;
         
        try {
            File folder = new File(summary_path);
            File[] listOfFiles = folder.listFiles();
            numOfFiles = listOfFiles.length;
            System.out.println("The number of documents: " + numOfFiles);

            for (int i = 0; i < numOfFiles; i++) {
                filename = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 5);
                abs_text = FileUtils.readFileToString(new File(abs_path + filename + ".abs"), "UTF-8");
                refSummaries[0] = abs_text;
                summary_text = FileUtils.readFileToString(new File(summary_path + filename + ".summ"), "UTF-8");

                r1.evaluate(summary_text, refSummaries);

                precisionSum += r1.getPrecision();
                recalSum += r1.getRecall();
                

                if (i % 100 == 0) {
                    System.out.println("Processed: " + (i + 1) + " files");
                } else if (i == numOfFiles - 1) {
                    System.out.println("Completing: " + numOfFiles + " files");
                }
            }

        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        

        //compute the Fscore
        System.out.println("-------------------------------------");
        System.out.println("Avg presision: " + precisionSum/numOfFiles) ;
        System.out.println("Avg Recall: "  + recalSum/numOfFiles);

        
        double pre_ = precisionSum/numOfFiles;
        double recal_ = recalSum/numOfFiles;
        double sum_ = pre_ + recal_;
        double F1 = 2*pre_*recal_/sum_;

        System.out.println("F measure: " + F1) ;
      
    }
}
