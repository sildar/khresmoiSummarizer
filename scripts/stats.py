import codecs
import os
from sys import argv

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
    cwd = os.getcwd()

for root, dir, files in os.walk(cwd):
    for name in files:
        nbfiles += 1
        with codecs.open(cwd+name, "r", "utf-8") as currentfile:
            content = currentfile.readlines()
            nbsentinfile = 0
            nbwordinfile = 0
            for line in content:
                nbsentinfile += line.count(". ")
                nbwordinfile += line.count(" ")
            #update min/max
            maxsent = nbsentinfile if nbsentinfile > maxsent else maxsent
            minsent = nbsentinfile if nbsentinfile < minsent else minsent
            maxword = nbwordinfile if nbwordinfile > maxword else maxword
            minword = nbwordinfile if nbwordinfile < minword else minword
            sent += nbsentinfile
            word += nbwordinfile

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
                
                




















