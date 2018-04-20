#!/usr/bin/env bash

echo "Compiler ..."

gcc -g -c stdlib.c
#gcc -g stdlib.o cmain.c -octest
gcc -g -c asmmain.s 
gcc asmmain.o stdlib.o -oasmtest
