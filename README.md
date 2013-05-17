kreshmoiSummarizer
==================

After cloning the repository, you'll need to install locally the two libraries (jaws and jwi), from the root of the repo :

$ mvn install:install-file -Dfile=./libs/jaws-bin.jar -DgroupId=jaws-bin -DartifactId=jaws-bin -Dversion=1.0 -Dpackaging=jar

$ mvn install:install-file -Dfile=./libs/edu.mit.jwi_2.2.3.jar -DgroupId=jwi -DartifactId=jwi -Dversion=2.2.3 -Dpackaging=jar

Then

$ mvn compile

If you have errors, you may want to try :

$ mvn resources:resources

To execute the Test class (runs summarization on the corpus, using all implemented features) :

$ mvn exec:java -Dexec.mainClass="ie.dcu.cngl.summarizer.main.Test"
