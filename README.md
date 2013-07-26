kreshmoiSummarizer
==================

Introduction
------------

This is a summarizer for the medical domain that I ported to the French Language during my internship at the CNGL, Dublin.
You can see my reports in the "reports" folder. They explain the process I followed to port and evaluate the summarizer.


Install
-------

After cloning the repository, you'll need to install locally the two libraries (jaws and jwi), from the root of the repo :

	$ mvn install:install-file -Dfile=./libs/jaws-bin.jar -DgroupId=jaws-bin -DartifactId=jaws-bin -Dversion=1.0 -Dpackaging=jar

	$ mvn install:install-file -Dfile=./libs/edu.mit.jwi_2.2.3.jar -DgroupId=jwi -DartifactId=jwi -Dversion=2.2.3 -Dpackaging=jar

	$ mvn install:install-file -Dfile=./libs/dragontool.jar -DgroupId=dragon -DartifactId=dragon -Dversion=1.3.3 -Dpackaging=jar

Then

	$ mvn compile

If you have errors, you may want to try :

	$ mvn resources:resources

To execute the Test class (runs summarization on the corpus, using all implemented features. Unfortunately,
the corpus can not be distributed yet, but you can create one and adapt the Test class to it) :

	$ mvn exec:java -Dexec.mainClass="ie.dcu.cngl.summarizer.main.Test"
	
The results are put in an expData/abstract and expData/summaries folders. You may need to create them if they don't exist.
Then, you can evaluate the result :

	$ mvn exec:java -Dexec.mainClass="ie.dcu.cngl.summarizer.main.ROUGEEval"
	
The results will be output on the Console/Terminal.


Licence
-------

The License is somewhat restrictive. Read it and contact the CNGL in Dublin if you want more information.
