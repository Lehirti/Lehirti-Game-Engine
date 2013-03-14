#!/bin/bash
mv -v ../../../luckysurvivor/LuckySurvivor-*.jar ../../../luckysurvivor/archive/

build=$(grep build= version)
mv -v ../../../luckysurvivor/LuckySurvivor.jar ../../../luckysurvivor/LuckySurvivor-${build:6}.jar
