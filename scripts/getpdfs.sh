#!/bin/bash

basurl="http://www.revuemedecinetropicale.com/html/"

startyear=2004
startnum=64

#for i in {0..5}; do
#    currentyear=$[startyear+i]
#    currentnum=$[startnum+i]
#    for j in {1..6}; do
#	wget -q $basurl${currentyear}_${currentnum}-$j.html
#    done
#done

for file in `ls`; do
    text=`iconv -f "LATIN1" -t "UTF-8" $file | grep -o -E '<b>ARTICLES ORIGINAUX.*' | sed -r "s/([^<][^b][^>])*<b>(.*)/\2/" | sed -r "s/([^<][^b][^>])*<b>.*/\1/"`
    todl=`echo $text | grep -o "href=\"\.\.[^.]*\.pdf" | sed -r "s/href=\"(.*)/\1/g"`
    for pdf in $todl; do
	wget -q http://www.revuemedecinetropicale.com/html/$pdf
    done
done

#for file in `ls`; do
#    todl=`cat $file | grep -o "href=\".*pdf" | sed -r "s/href=\"(.*)/\1/g"`
#    for pdf in $todl; do
#	wget -q www.ata-journal.org$pdf
#    done
#done

