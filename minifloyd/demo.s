.data
# Line 2: x: int
	.comm	x,4,4

.text
# -----------------------------------------
# Line 4: start()
# -----------------------------------------
.global	main
main:

# -----------------------------------------
# Line 6: x := 3
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $3
        # Now, do the assignment...
        popl    x

# -----------------------------------------
# Line 7: if (x>0) then
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
# Line 8: loop x>0
# -----------------------------------------
_while1:
        pushl    x
        pushl    $0
        call    gtr
        addl    $8, %esp
        push    %eax
        popl    %eax
        cmpl    $0, %eax
        jne    _startwhilebody1
        jmp    _endwhile1
_startwhilebody1:

# -----------------------------------------
# Line 9: out.writeint()
# -----------------------------------------
        pushl    x
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 10: x := x-1
# -----------------------------------------
        # Evaluate RHS ...
        pushl    x
        pushl    $1
        call    sub
        addl    $8, %esp
        push    %eax
        # Now, do the assignment...
        popl    x
        jmp    _while1
_endwhile1:
        jmp    _endif00

# -----------------------------------------
# Line 12: else
# -----------------------------------------
_else00:

# -----------------------------------------
# Line 13: out.writeint()
# -----------------------------------------
        pushl    $999
        call    writeint
        addl    $4, %esp
_endif00:

# -----------------------------------------
# Line 4: end start
# -----------------------------------------
        pushl     $0
        call    exit
