#!/bin/sh
set -e
mkdir -p build/test-classes
javac -d build/test-classes $(find src -name "*.java")
java -cp build/test-classes test.TestRunner
