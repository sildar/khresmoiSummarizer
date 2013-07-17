#!/bin/bash

#Fixes encoding of all files in the folders of the cwd

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
