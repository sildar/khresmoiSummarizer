#!/bin/bash

for file in `ls *.pdf`; do

    nb=`pdftk $file dump_data | grep NumberOfPages | sed "s/NumberOfPages: //g"`

    if [ $nb -lt 4 ] 
    then
	rm $file
    fi

done

