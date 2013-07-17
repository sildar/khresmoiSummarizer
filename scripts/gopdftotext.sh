#!/bin/bash

cwd=`pwd`
pdftotloc="/home/sildar/programmation/interships/dcuSummarization/mySummarizer/corpus/lapdftext"

for file in `ls *.pdf`; do
    filename=$(basename "$file")
    filename="${filename%.*}"
    $pdftotloc/extractFullText $cwd/$file $cwd $pdftotloc/rules.drl
    cat $cwd/${filename}_fullText.txt >> $cwd/${filename}_body.txt
done

echo "removing tmp files"

rm *_fullText.txt
    
