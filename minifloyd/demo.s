# -----------------------------------------
        # Jwhat331 ...
        # CpS450 - Phase4 ...
        # demo.floyd ...
# -----------------------------------------


.data
# Line 6: d: int
	.comm	d,4,4

.text



# -----------------------------------------
# Line 8: start()
# -----------------------------------------
.global	main
main:
        pushl    %ebp
        movl    %esp, %ebp
        subl    $12, %esp
        movl    $0, -4(%ebp)
        movl    $0, -8(%ebp)
        movl    $0, -12(%ebp)

# -----------------------------------------
# Line 13: -8(%ebp) := 10
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $10
        # Now, do the assignment...
        popl    -8(%ebp)

# -----------------------------------------
# Line 14: -12(%ebp) := 20
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $20
        # Now, do the assignment...
        popl    -12(%ebp)

# -----------------------------------------
# Line 15: out.writeint()
# -----------------------------------------
        pushl    -4(%ebp)
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 16: out.writeint()
# -----------------------------------------
        pushl    -8(%ebp)
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 17: out.writeint()
# -----------------------------------------
        pushl    -12(%ebp)
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 18: out.writeint()
# -----------------------------------------
        pushl    d
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 19: out.writeint()
# -----------------------------------------
        pushl    d
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 20: out.writeint()
# -----------------------------------------
        pushl    -4(%ebp)
        call    writeint
        addl    $4, %esp
        movl    -4(%ebp), %eax
        movl    %ebp, %esp
        popl    %ebp
        ret    

# -----------------------------------------
# Line 22: end start
# -----------------------------------------
        pushl     $0
        call    exit
