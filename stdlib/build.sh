#!/bin/sh 

echo "Compiler ..."

gcc -g -c stdlib.c
#gcc -g cmain.c lib.o -octest
gcc -g -c asmmain.s 
gcc -g asmmain.o stdlib.o -oasmtest
