kreshmoiSummarizer
==================

After cloning the repository :

$ mvn compile

If you have errors, you may want to try :

$ mvn resources:resources

To execute the Test class (runs summarization on the corpus, using all implemented features) :

$ mvn exec:java -Dexec.mainClass="ie.dcu.cngl.summarizer.main.Test"
