#!/usr/bin/python
# -*- coding: utf-8 -*-

import os, codecs, string

def isindict(s,dic):
    i = len(s)-1
    core = s
    while i>0 and s[i] in set(string.punctuation):
        core = core[:-1]
        i-=1
    #remove d', l', ...
    if "'" in core:
        core = core[core.find("'")+1:]
    if u"’" in core:
        core = core[core.find(u"’")+1:]
    return core.lower() in dic 

def dehyphenate(s):
    end = ""
    #remember there's a punctuation in the end of the word
    i = len(s)-1
    while  i > 0 and s[i] in set(string.punctuation):
        end = s[i] + end
        i-=1
    core = s.replace("-","")
    return core + end

cwd = os.getcwd()

fileend = "_final.txt"

filetoprocess = []

for root, dir, files in os.walk(cwd):
    for name in files:
        if name.endswith(fileend):
            filetoprocess.append(name)

print len(filetoprocess), "files to process"
print "Creating dictionnary"

dicfile = codecs.open("frdict.txt", "r", "utf-8").readlines()
frdict = []
for word in dicfile:
    frdict.append(word[:-1].lower())

print "processing corpus"

for aFile in filetoprocess:
    content = []
    with codecs.open(aFile, "r", "utf-8") as f:
        content = f.readlines()

    out = ""

    beginning = ""

    for line in content:
        for ind,word in enumerate(line.split(" ")):
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
        #out += "\n"

    basename = aFile[:aFile.find("_final")]
    with codecs.open(basename + "_ok.txt", "w", "utf-8") as outf:
        outf.write(out)
                




















