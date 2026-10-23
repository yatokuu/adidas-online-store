#!/bin/sh
set -e
mkdir -p build/classes
javac -d build/classes $(find src -name "*.java" ! -path "src/test/*")
echo "Build successful."
