# -----------------------------------------
        # Jwhat331 ...
        # CpS450 - Phase4 ...
        # demo.floyd ...
# -----------------------------------------


.data
# Line 6: x: int
	.comm	x,4,4

# Line 7: y: int
	.comm	y,4,4

# Line 8: z: int
	.comm	z,4,4

# Line 9: b1: boolean
	.comm	b1,4,4

# Line 10: b2: boolean
	.comm	b2,4,4

.text
# -----------------------------------------
# Line 12: start()
# -----------------------------------------
.global	main
main:

# -----------------------------------------
# Line 14: x := 5
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $5
        # Now, do the assignment...
        popl    x

# -----------------------------------------
# Line 15: y := x*(8-x)
# -----------------------------------------
        # Evaluate RHS ...
        pushl    x
        pushl    $8
        pushl    x
        call    sub
        addl    $8, %esp
        push    %eax
        call    mul
        addl    $8, %esp
        push    %eax
        # Now, do the assignment...
        popl    y

# -----------------------------------------
# Line 17: b1 := true
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $1
        # Now, do the assignment...
        popl    b1

# -----------------------------------------
# Line 18: b2 := notfalse
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $0
        popl    %eax
        xorl    $1, %eax
        pushl    %eax
        # Now, do the assignment...
        popl    b2

# -----------------------------------------
# Line 19: b2 := not(notb2orb1)
# -----------------------------------------
        # Evaluate RHS ...
        pushl    b2
        popl    %eax
        xorl    $1, %eax
        pushl    %eax
        pushl    b1
        call    or
        addl    $8, %esp
        push    %eax
        popl    %eax
        xorl    $1, %eax
        pushl    %eax
        # Now, do the assignment...
        popl    b2

# -----------------------------------------
# Line 21: out.writeint()
# -----------------------------------------
        pushl    y
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 22: out.writeint()
# -----------------------------------------
        pushl    x
        pushl    $2
        call    div
        addl    $8, %esp
        push    %eax
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 24: y := y-x
# -----------------------------------------
        # Evaluate RHS ...
        pushl    y
        pushl    x
        call    sub
        addl    $8, %esp
        push    %eax
        # Now, do the assignment...
        popl    y

# -----------------------------------------
# Line 25: out.writeint()
# -----------------------------------------
        pushl    y
        pushl    $4
        call    add
        addl    $8, %esp
        push    %eax
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 26: out.writeint()
# -----------------------------------------
        pushl    $9
        pushl    $5
        call    sub
        addl    $8, %esp
        push    %eax
        call    neg
        addl    $4, %esp
        pushl    %eax
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 27: out.writeint()
# -----------------------------------------
        pushl    $5
        pushl    $2
        pushl    $3
        call    mul
        addl    $8, %esp
        push    %eax
        call    sub
        addl    $8, %esp
        push    %eax
        pushl    $1
        call    add
        addl    $8, %esp
        push    %eax
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 28: end start
# -----------------------------------------
        pushl     $0
        call    exit
