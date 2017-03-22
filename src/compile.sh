#!/bin/sh

file="KnightsTour"

echo "Compiling " $file
javac $file.java

echo "Running " $file
java $file

echo "Finished running"

for file in *.class
do
        echo "Removing $file"
        rm $file
done
