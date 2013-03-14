#!/bin/bash
rm -f ../../../luckysurvivor/*.png ../../../luckysurvivor/*.html

cp -v ../website/* ../../../luckysurvivor/
for file in ../../../luckysurvivor/*.jar ; do sed "s/GDL/<a href=\"`basename $file`\">`basename $file`<\/a><br\/>GDL/g" ../../../luckysurvivor/index.html > ../../../luckysurvivor/index.html.new && mv ../../../luckysurvivor/index.html.new ../../../luckysurvivor/index.html ; done
for file in ../../../luckysurvivor/*.zip ; do sed "s/GDL/<a href=\"`basename $file`\">`basename $file`<\/a><br\/>GDL/g" ../../../luckysurvivor/index.html > ../../../luckysurvivor/index.html.new && mv ../../../luckysurvivor/index.html.new ../../../luckysurvivor/index.html ; done
sed "s/GDL//g" ../../../luckysurvivor/index.html > ../../../luckysurvivor/index.html.new && mv ../../../luckysurvivor/index.html.new ../../../luckysurvivor/index.html