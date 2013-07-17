#!/usr/bin/python
# -*- coding: utf-8 -*-

import os, codecs, re


cwd = os.getcwd()

fileend = "_head.txt"

filetoprocess = []

for root, dir, files in os.walk(cwd):
    for name in files:
        if name.endswith(fileend):
            filetoprocess.append(name)

titleandabstract = dict()

#print len(filetoprocess), "files to process"

for aFile in filetoprocess:

    #shortname
    filename = aFile[:aFile.find(".pdf")]

    content = codecs.open(aFile, "r", "utf-8").readlines()
    
    start = 0
    if content[start].startswith("Cas"):
        start += 2
    elif content[start].startswith("Acta"):
        start = 5
        while content[start].isupper() or content[start].isspace():
            start += 1


    title = content[start]
    
    """
    #used to check incoherent files (not french, no title)
    filetodel = []
    if not (title[0].isupper()):
        if re.match("^[a-z].*", title):
            print aFile
    """

    finished = False
    while not finished:
        if content[start][0].isupper():
            finished = True
        else:
            title += content[start]
            start += 1

    finaltitle = []
    for line in title.split("\n"):
        if line not in finaltitle:
            finaltitle.append(line)
    
    finaltitle = " ".join(finaltitle)

    """
    if re.match("^.*( [a-zA-Z] [a-zA-Z] [a-zA-Z] ).*$", finaltitle):
        print filename
    if finaltitle.count(" ") > len(finaltitle)/3:
        print filename
    if len(finaltitle) < 30:
        print filename
    if "Best" in finaltitle:
        print filename
    if re.match("^[0-9 ].*",finaltitle):
        print filename, finaltitle
    if finaltitle[-2] == ':':
        print filename
    if "I'" in finaltitle:
        print filename
    """

    start = 0
    end = 0

    for iline in range(len(content)):
        if u"résumé" in content[iline].lower():
            start = iline
        elif "summary" in content[iline].lower() or "abstract" in content[iline].lower():
            end = iline - 1
            break

    abstract = content[start:end]

    """
    if len(abstract) == 0:
        print filename
    """

    finalabstract = " ".join(abstract).replace("\n"," ")

    titleandabstract[filename] = (finaltitle, finalabstract)


cwd = os.getcwd()

fileend = "_body.txt"

filetoprocess = []

for root, dir, files in os.walk(cwd):
    for name in files:
        if name.endswith(fileend):
            filetoprocess.append(name)

for aFile in filetoprocess:
    filename = aFile[:aFile.find(fileend)]
    content = codecs.open(aFile, 'r', "utf-8").readlines()
    i=0
    while i < len(content):
        #either it's a long sentence (and part of the article), or it's the beginning of the article (intro)
        if u"introduction" in content[i].lower() or len(content[i]) > 200 and not u"résumé" in content[i-1].lower() and not u"summary" in content[i-1].lower():
            break
        else:
            del content[i]

    """
    if len(content) == 0:
        print filename
    todel = set()
    for line in content:
        if re.match("^.*( [a-zA-Z] [a-zA-Z] [a-zA-Z] [a-zA-Z] ).*$",line):
            todel.add(filename)
    for it in todel:
        print it 
    """

 
    writer = codecs.open(filename+"_final.txt", "w", "utf-8")
    writer.write(titleandabstract[filename][0] + "\n")
    writer.write(titleandabstract[filename][1] + "\n")
    writer.write(" ".join(content))
    writer.close()












