#!/bin/sh 

echo "Compiler ..."

gcc -g -c stdlib.c
#gcc -g stdlib.o cmain.c -octest
gcc -g -c asmmain.s 
gcc -g asmmain.o stdlib.o -oasmtest
