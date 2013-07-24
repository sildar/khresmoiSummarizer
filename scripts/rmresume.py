#!/usr/bin/python
# -*- coding: utf-8 -*-

#arg[1] folder
#arg[2] keyword
#arg[3] linenumber

import codecs
import os
from sys import argv

def usage(num):
    pass

if len(argv) !=  4:
    print usage(1)
else:
    if os.exists(argv[1]):
        cwd = argv[1]
    else:
        usage(2)
    keyword = argv[2]
    try:
        linenumber = int(argv[3])
    except ValueError:
        usage(4)


for filename in os.path.listdir(cwd):
    if os.path.isfile(filename):
        with codecs.open(filename,"r","utf-8") as currentfile:
            basename = filename[0:filename.find('.')]
            content = currentfile.readlines()
            out = ""
            for num,line in enumerate(content):
                if num == 1 and keyword in line:
                    out += line[8:line.find(keyword)]
                else:
                    out += line
            with codecs.open(basename + "_clean.txt", "w", "utf-8") as outfile:
                outfile.write(out)




















