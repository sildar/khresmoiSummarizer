#!/usr/bin/python
# -*- coding: utf-8 -*-

# By Rémi Bois
# This script is designed to rehyphenate words that have been
# split. Its efficiency largely depends on the size of the dictionnary
# you'll feed into it. Each times a word containing an hyphen ('-') is
# found, the program tries to find its dehyphenated version inside the
# dictionnary. If found, the word is replaced (eg : ex-ample ->
# example), else, it is kept as is (eg : X-ray -> X-ray). It considers
# newline split (eg : ex-\nample -> example) but doesn't try work if
# any other character is inserted (eg : "ex-\n\nample" ->
# "ex-\n\nample"). Feel free to improve this behavior.

import os, codecs, string
from sys import argv

def usage(num):
    if num == 1:
        print "Usage : "
        print "This script recompose hyphenated words based on a dictionnary. The scripts writes the dehyphenated files with suffix \"_ox.txt\""
        print "It needs two arguments :"
        print "The folder to consider (where all the files will be processed)"
        print "The dictionnary to use as reference."
        print "Ex : python rehyphenize.py ./corpus/ ./dic.txt"
    elif num == 2:
        print "The first argument must be a path to the folder to analyse. You seem to have given an incorrect path"
    elif num == 3:
        print "The second argument must be an existing dictionnary as a text file, with one word per line."
    exit(1)

def isindict(s,dic):
    """
    Checks if a word is in the dictionnary (tries to get the radical
    of the word before checking)
    """
    i = len(s)-1
    core = s
    while i>0 and s[i] in set(string.punctuation):
        core = core[:-1]
        i-=1
    #remove d', l', ... to check in the dictionnary
    if "'" in core:
        core = core[core.find("'")+1:]
    if u"’" in core:
        core = core[core.find(u"’")+1:]
    return core.lower() in dic 


def dehyphenate(s):
    """
    Takes an hyphenated word as parameter and gives the dehyphenated
    word as result. eg "foo-bar" -> "foobar"
    """
    end = ""
    #remember there's a punctuation in the end of the word
    i = len(s)-1
    while  i > 0 and s[i] in set(string.punctuation):
        end = s[i] + end
        i-=1
    core = s.replace("-","")
    return core + end


if len(argv) == 3:
    cwd = argv[1]
else:
    usage(1)

filetoprocess = []

#store the files names
if os.path.exists(cwd):
    for name in os.listdir(cwd):
        filetoprocess.append(name)
else:
    usage(2)

print len(filetoprocess), "files to process"
print "Creating dictionnary"

#read dictionnary file and stores it into a dict structure
try:
    with codecs.open(argv[2], "r", "utf-8") as dicfile:
        dicwords = dicfile.readlines()
        frdict = []
        for word in dicwords:
            frdict.append(word[:-1].lower())
except IOError:
    usage(3)

print "Processing corpus"

for aFile in filetoprocess:
    content = []
    with codecs.open(aFile, "r", "utf-8") as f:
        content = f.readlines()

    #out will store the output (all processed lines)
    out = ""
    #beginning will store the first half of a word (the other half may
    #be on the next line )
    beginning = ""

    for line in content:
        for ind,word in enumerate(line.split(" ")):
            #if there was a broken word on the previous line, try to
            #dehyphenate it
            if beginning != "":
                fullword = beginning + word
                if isindict(fullword, frdict):
                    out += beginning + word
                else:
                    out += beginning + "-" + word
                beginning = ""
            else:
                if "-" in word:
                    dehyphenated = dehyphenate(word)
                    #if last word of the line :
                    if ind == len(line.split(" "))-1:
                        beginning = dehyphenated
                    elif isindict(dehyphenated,frdict):
                        out += dehyphenated
                    else:
                        out += word
                else:
                    out += word
            out += " "

    basename = aFile[:aFile.rfind(".")]
    #write the output in a file
    with codecs.open(basename + "_ok.txt", "w", "utf-8") as outf:
        outf.write(out)
                




















