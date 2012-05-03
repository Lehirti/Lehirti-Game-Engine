#!/bin/bash
build=$(grep build= version)
mv -v ../../../luckysurvivor/LuckySurvivor.jar ../../../luckysurvivor/LuckySurvivor-${build:6}.jar
cp -v ../../../luckysurvivor/LuckySurvivor-${build:6}.jar ../../../luckysurvivor/LuckySurvivor-${build:6}.zip
