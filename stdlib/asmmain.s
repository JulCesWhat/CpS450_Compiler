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

# Line 8: ans: int
	.comm	ans,4,4

.text



# -----------------------------------------
# Line 10: gcd()
# -----------------------------------------
.global	gcd
gcd:
        pushl    %ebp
        movl    %esp, %ebp
        subl    $4, %esp
        movl    $0, -4(%ebp)

# -----------------------------------------
# Line 12: if b=0 then
# -----------------------------------------
        pushl    12(%ebp)
        pushl    $0
        call    eq
        addl    $8, %esp
        pushl    %eax
        popl    %eax
        cmpl    $0, %eax
        jne    _doif00
        jmp    _else00
_doif00:

# -----------------------------------------
# Line 13: -4(%ebp) := a
# -----------------------------------------
        # Evaluate RHS ...
        pushl    8(%ebp)
        # Now, do the assignment...
        popl    -4(%ebp)
        jmp    _endif00

# -----------------------------------------
# Line 14: else
# -----------------------------------------
_else00:

# -----------------------------------------
# Line 15: 8(%ebp) := gcd(b,a-(a/b)*b)
# -----------------------------------------
        # Evaluate RHS ...
        pushl    8(%ebp)
        pushl    8(%ebp)
        pushl    12(%ebp)
        call    div
        addl    $8, %esp
        pushl    %eax
        pushl    12(%ebp)
        call    mul
        addl    $8, %esp
        pushl    %eax
        call    sub
        addl    $8, %esp
        pushl    %eax
        pushl    12(%ebp)
        call    gcd
        addl    $8, %esp
        pushl    %eax
        # Now, do the assignment...
        popl    8(%ebp)

# -----------------------------------------
# Line 16: -4(%ebp) := a
# -----------------------------------------
        # Evaluate RHS ...
        pushl    8(%ebp)
        # Now, do the assignment...
        popl    -4(%ebp)
_endif00:

        movl    -4(%ebp), %eax
        movl    %ebp, %esp
        popl    %ebp
        ret    



# -----------------------------------------
# Line 20: displayres()
# -----------------------------------------
.global	displayres
displayres:
        pushl    %ebp
        movl    %esp, %ebp

# -----------------------------------------
# Line 22: out.writeint()
# -----------------------------------------
        pushl    8(%ebp)
        call    writeint
        addl    $4, %esp

        movl    %ebp, %esp
        popl    %ebp
        ret    



# -----------------------------------------
# Line 25: start()
# -----------------------------------------
.global	main
main:
        pushl    %ebp
        movl    %esp, %ebp

# -----------------------------------------
# Line 27: x := in.readint()
# -----------------------------------------
        # Evaluate RHS ...
        call    readint
        pushl    %eax
        # Now, do the assignment...
        popl    x

# -----------------------------------------
# Line 28: y := in.readint()
# -----------------------------------------
        # Evaluate RHS ...
        call    readint
        pushl    %eax
        # Now, do the assignment...
        popl    y

# -----------------------------------------
# Line 29: ans := gcd(x,y)
# -----------------------------------------
        # Evaluate RHS ...
        pushl    y
        pushl    x
        call    gcd
        addl    $8, %esp
        pushl    %eax
        # Now, do the assignment...
        popl    ans

# -----------------------------------------
# Line 30: displayres()
# -----------------------------------------
        pushl    ans
        call    displayres
        addl    $4, %esp

        movl    %ebp, %esp
        popl    %ebp
        ret    

# -----------------------------------------
# Line 31: end start
# -----------------------------------------
        pushl     $0
        call    exit
