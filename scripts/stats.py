#!/usr/bin/python
# -*- coding: utf-8 -*-

# By Rémi Bois
# This script is designed to give some statistics about text files.
# It is not ment to give precise stats but better give an idea of the
# number of words and sentences in an homogeneous corpus of text
# files. The sentences stats won't work on a non UNIX machine. You'll
# have to replace the '\n' by the proper character.

import codecs
import os
from sys import argv


def usage(num):
    if num == 1:
        print "Usage : "
        print "This script prints some statistics about text files in a folder."
        print "It doesn't compute exact statistics, but allows to compare different folders."
        print "It needs only one argument : the folder to consider"
        print "Ex : python stats.py ./corpus/"
    elif num == 2:
        print "The argument must be a path to the folder to analyse. You seem to have given an incorrect path"
    exit(1)


sent = 0
maxsent = 0
minsent = 99999
word = 0
maxword = 0
minword = 99999
nbfiles = 0

if len(argv) == 2:
    cwd = argv[1]
else:
    usage(1)

if os.path.exists(cwd):
    for name in os.path.listdir(cwd):
        nbfiles += 1
        with codecs.open(cwd+name, "r", "utf-8") as currentfile:
            content = currentfile.readlines()
            nbsentinfile = 0
            nbwordinfile = 0
            for line in content:
                # we suppose a sentence is finished by ". " or ".\n"
                nbsentinfile += line.count(". ") + line.count(".\n")
                nbwordinfile += line.count(" ")
            #update min/max
            maxsent = max(nbsentinfile, maxsent)
            minsent = min(nbsentinfile, minsent)
            maxword = max(nbwordinfile, maxword)
            minword = min(nbwordinfile, minword)
            sent += nbsentinfile
            word += nbwordinfile

else:
    usage(2)

print "Results for", cwd, ":\n"
print "Sents : ", sent
print "Sents mean : ", sent/nbfiles
print "Sents max : ", maxsent
print "Sents min : ", minsent
print "-------------------------------"
print "Words : ", word
print "Words mean : ", word/nbfiles
print "Words max : ", maxword
print "Words min : ", minword
                
                




















