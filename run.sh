#!/bin/sh
set -e
./build.sh
java -cp build/classes Main
