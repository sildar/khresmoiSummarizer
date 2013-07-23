#!/usr/bin/python
# -*- coding: utf-8 -*-
import codecs
import os


for root, dir, files in os.walk(os.getcwd()):
    for name in files:
        with codecs.open(name,"r","utf-8") as currentfile:
            basename = name[0:name.find('.')]
            content = currentfile.readlines()
            out = ""
            for num,line in enumerate(content):
                if num == 1 and u"Mots clés" in line:
                    out += line[8:line.find(u"Mots clés")]
                else:
                    out += line
            outfile = codecs.open(basename + "kk.txt", "w", "utf-8")
            outfile.write(out)




















