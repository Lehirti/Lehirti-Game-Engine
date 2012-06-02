#!/bin/bash
flavor=$(grep flavor= version)
build=$(grep build= version)
git tag -a -f -F version LuckySurvivor-${flavor:7}-${build:6}
