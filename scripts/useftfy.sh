#!/bin/bash

#By Rémi Bois
#Very simple script converting a folder filled with broken text files
#(bad encoding) to a new folder (with prefix UTF-8). Uses ftfy which
#has to be installed
#https://github.com/LuminosoInsight/python-ftfy. This script is ment
#to be used in a folder containing folders of texts to fix.


for folder in `ls`
do
    if [ -d $folder ]; then

	if [ ! -e UTF8-$folder ]; then
	    mkdir UTF8-$folder
	fi

	for file in `ls $folder`
	do
	    ftfy $folder/$file >> UTF8-$folder/$file
	done
    fi
done
