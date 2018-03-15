.data
# Line 2: x: int
	.com	x,4,4

# Line 3: y: int
	.com	y,4,4

.text
# -----------------------------------------
# Line 5: start()
# -----------------------------------------
.global	main
main:

# -----------------------------------------
# Line 7: x := 5
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $5
        # Now, do the assignment...
        popl    x

# -----------------------------------------
# Line 8: y := 3+x
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $3
        pushl    x
        call    add
        addl    $8, %esp
        push    %eax
        # Now, do the assignment...
        popl    y

# -----------------------------------------
# Line 9: out.writeint()
# -----------------------------------------
        pushl    x
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 5: end start
# -----------------------------------------
        pushl     $0
        call    exit
