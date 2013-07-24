#!/usr/bin/python
# -*- coding: utf-8 -*-

# By Rémi Bois
# This script is designed to remove some keywords from a specific line
# in a list of files. Each file processed gives a result put in a new
# file ending with "_rmsec.txt" where only the line given as argument
# will have been modified.


import codecs
import os
from sys import argv


def usage(num):
    #basic usage
    if num == 1:
        print "Usage : "
        print "This script removes some keywords from a specific line in a set of source texts"
        print "This script needs two arguments."
        print "First, the path of the folder to consider. All files in the folder will be processed."
        print "Secondly, the number of the line where to remove the keywords."
        print "eg : $ python rmsections.py ./corpus/ 2 will remove keywords from all second lines of the files in ./corpus/."
        print "A third optional argument is available to feed the keywords list from a file. The keywords file must be one word per line. If it isn't set, a default French list will be used."
        print "eg : $ python rmsections.py ./corpus/ 3 ./keywords.txt will remove keywords present in keywords.txt from all third lines of the files in corpus."
    #Problem with folder path
    elif num == 2:
        print "Error : It seems you gave an incorrect path for the first argument. The first argument is supposed to be a path to a folder containing text files taht will be processed by this script."
    #Problem with number given
    elif num == 3:
        print "Error : It seems you didn't gave a number as second argument. The second argument is supposed to be an number (eg : 2). It corresponds to the line to process in each document."
        #Problem with the optionnal argument (list of keywords file)
    elif num == 4:
        print "Error : It seems you didn't gave a correct path for the file containing the keywords. This argument isn't mandatory, a French list of keywords will be used by default."
    exit(1)


def extractListFromFile(fileLocation):
    """
    Reads a file and stores its content in a list.
    The file is expected to be one item per line.
    """
    with codecs.open(fileLocation,"r","utf-8") as filecontent:
        return filecontent.readlines()

def stripKeyword(line, keywordslist):
    """
    Removes all keywords in keywordslist from line
    """
    res = line
    for word in keywordslist:
        res = res.replace(word, "")
    return res

#Checks the arguments and initializes variables
if len(argv) not in [3,4]:
    usage(1)
else:
    if os.exists(argv[2]):
        cwd = argv[2]
    else:
        usage(2)
    try:
        linenumber = int(argv[3])
    except ValueError:
        usage(3)
    if len(argv) == 4:
        if os.exists(argv[4]):
            keywords = extractListFromFile(argv[4])
        else:
            usage(4)
    #default
    else:
#these are French keywords corresponding to section names sometimes
#found in the abstract
        keywords = [u"But : ",
                    u"Introduction : ",
                    u"Objectif : ",
                    u"Objectifs : ",
                    u"Conclusion : ",
                    u"Conclusions : ",
                    u"Méthode : ",
                    u"Méthodes : ",
                    u"Résultat : ",
                    u"Résultats : ",
                    u"Discussion : ",
                    u"Observation : ",
                    u"Observations : ",
                    u"Matériels et méthodes : ",
                    u"Patients et méthodes : ",
                    ]

for filename in os.path.listdir(cwd):
    if os.path.isfile(filename):
        with codecs.open(filename,"r","utf-8") as currentfile:
            basename = filename[0:filename.find('.')]
            content = currentfile.readlines()
            out = ""
            for num,line in enumerate(content):
                if num == linenumber :
                    out += stripKeyword(line,keywords)
                else:
                    out += line
            with codecs.open(basename + "_rmsec.txt", "w", "utf-8") as outfile:
                outfile.write(out)












