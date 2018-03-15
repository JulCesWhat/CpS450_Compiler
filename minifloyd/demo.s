.data
# Line 8: x: int
	.comm	x,4,4

.text
# -----------------------------------------
# Line 10: start()
# -----------------------------------------
.global	main
main:

# -----------------------------------------
# Line 12: x := in.readint()
# -----------------------------------------
        # Evaluate RHS ...
        call    readint
        pushl    %eax
        # Now, do the assignment...
        popl    x

# -----------------------------------------
# Line 13: if (x>0) then
# -----------------------------------------
        pushl    x
        pushl    $0
        call    gtr
        addl    $8, %esp
        push    %eax
        popl    %eax
        cmpl    $0, %eax
        jne    _doif00
        jmp    _else00
_doif00:

# -----------------------------------------
# Line 14: out.writeint()
# -----------------------------------------
        pushl    $1
        call    writeint
        addl    $4, %esp
        jmp    _endif00

# -----------------------------------------
# Line 15: else
# -----------------------------------------
_else00:

# -----------------------------------------
# Line 16: if (x=0) then
# -----------------------------------------
        pushl    x
        pushl    $0
        call    eq
        addl    $8, %esp
        push    %eax
        popl    %eax
        cmpl    $0, %eax
        jne    _doif01
        jmp    _else01
_doif01:

# -----------------------------------------
# Line 17: out.writeint()
# -----------------------------------------
        pushl    $0
        call    writeint
        addl    $4, %esp
        jmp    _endif01

# -----------------------------------------
# Line 18: else
# -----------------------------------------
_else01:

# -----------------------------------------
# Line 19: out.writeint()
# -----------------------------------------
        pushl    $-1
        call    writeint
        addl    $4, %esp
_endif01:

# -----------------------------------------
# Line 21: out.writeint()
# -----------------------------------------
        pushl    $9
        call    writeint
        addl    $4, %esp
_endif00:

# -----------------------------------------
# Line 23: out.writeint()
# -----------------------------------------
        pushl    $9
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 10: end start
# -----------------------------------------
        pushl     $0
        call    exit
