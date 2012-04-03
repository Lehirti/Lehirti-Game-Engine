#!/bin/bash
build=$(grep build= version)
mv -v ../../../mindcraft/Mindcraft.jar ../../../mindcraft/Mindcraft-${build:6}.jar
cp -v ../../../mindcraft/Mindcraft-${build:6}.jar ../../../mindcraft/Mindcraft-${build:6}.zip
