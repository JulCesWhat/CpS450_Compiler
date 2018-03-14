#!/bin/sh 
# Look at C compiler's STABS output
#gcc -gstabs -S simple.c

# Compile C library with debug info
#gcc -g -c lib.c

# Compile Assembly without STABS debugging info
# Note: Need -g switch in the following command
#gcc -g floydDemo_nosourcedebug.s lib.o -odemo_asmsource

# Compile Assembly containing STABS debugging info for source-level debug
# Note: NO -g switch in the following command
#gcc floydDemo_sourcedebug.s lib.o -odemo_floydsource

gcc -g -c lib.c
gcc -g cmain.c lib.o -octest
gcc -g -c asmmain.s 
gcc -g asmmain.o lib.o -oasmtest
