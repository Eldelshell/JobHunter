#!/bin/bash

echo "Building JobHunter plugin"
cd jobhunter
mvn clean install
cd ..

echo "Building CareerBuilder plugin"
cd cb-plugin
mvn clean install
cd ..

echo "Building Dice.com plugin"
cd dice-plugin
mvn clean install
cd ..

echo "Building InfoEmpleo plugin"
cd infoempleo-plugin
mvn clean install
cd ..

# Must do it manually because of private key
# echo "Building InfoJobs plugin"
# cd infojobs-plugin
# mvn clean install
# cd ..

echo "Building Monster plugin"
cd monster-plugin
mvn clean install
cd ..
 
echo "Building JobHunter plugin"
cd jobhunter
mvn assembly:assembly
cd ..

