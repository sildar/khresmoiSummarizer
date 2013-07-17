#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Usage: $0 <language code>"
    echo "The language code can be FR, DA, NE, EN, DE, IT, PO, ES."
    echo "This script downloads and extracts the medical terms from http://users.ugent.be/~rvdstich/eugloss/ in the language given in parameter."
    exit 1
fi

alphabet=(a b c d e f g h i j k l m n o p q r s t u v w x y z lef)
baseurl="http://users.ugent.be/~rvdstich/eugloss/"
language=$1
endurl="/lijst"

#gets the html pages

for letter in "${alphabet[@]}"; do
    wget -q $baseurl$language$endurl$letter".html"
done


if [ -f terms_$language.txt ];
then
    rm terms_$language.txt
fi

#gets the terms from the html pages

for file in `ls`; do
    cat $file | grep -o "<b>.*</b>" | sed -r "s/^<b>(.*)<\/b>$/\1/g" | sed -r "s/([^(]*)[^\)]+\)/\1/g" | sed -r "s/[()?]//g" | sed -r "s/.\.//g" | recode HTML_4.0 | sed -E "s/[./,]/\n/g" | sed -r "s/ - /\n/g" | sed -r "s/^ (.*)/\1/g" | sed -r "s/^(.*) $/\1/g" | sed -r "s/^[ 0-9*]*//g" >> terms_$language.txt
done

sort -d terms_$language.txt | sed '/^$/d' | uniq > terms_$language.txt

#removes the html files as they are no longer needed
rm lij*
