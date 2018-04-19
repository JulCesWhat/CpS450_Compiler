# -----------------------------------------
        # Jwhat331 ...
        # CpS450 - Phase4 ...
        # demo.floyd ...
# -----------------------------------------


.data
# Line 5: num: int
	.comm	num,4,4

# Line 6: num2: int
	.comm	num2,4,4

# Line 7: isOk: boolean
	.comm	isOk,4,4

.text



# -----------------------------------------
# Line 9: Fact()
# -----------------------------------------
.global	Fact
Fact:
        pushl    %ebp
        movl    %esp, %ebp
        subl    $8, %esp
        movl    $0, -4(%ebp)
        movl    $0, -8(%ebp)

# -----------------------------------------
# Line 12: -8(%ebp) := 1
# -----------------------------------------
        # Evaluate RHS ...
        pushl    $1
        # Now, do the assignment...
        popl    -8(%ebp)

# -----------------------------------------
# Line 13: loop num>0
# -----------------------------------------
_while0:
        pushl    8(%ebp)
        pushl    $0
        call    gtr
        addl    $8, %esp
        pushl    %eax
        popl    %eax
        cmpl    $0, %eax
        jne    _startwhilebody0
        jmp    _endwhile0
_startwhilebody0:

# -----------------------------------------
# Line 14: -8(%ebp) := answer*num
# -----------------------------------------
        # Evaluate RHS ...
        pushl    -8(%ebp)
        pushl    8(%ebp)
        call    mul
        addl    $8, %esp
        pushl    %eax
        # Now, do the assignment...
        popl    -8(%ebp)

# -----------------------------------------
# Line 15: 8(%ebp) := num-1
# -----------------------------------------
        # Evaluate RHS ...
        pushl    8(%ebp)
        pushl    $1
        call    sub
        addl    $8, %esp
        pushl    %eax
        # Now, do the assignment...
        popl    8(%ebp)
        jmp    _while0
_endwhile0:

# -----------------------------------------
# Line 17: -4(%ebp) := answer
# -----------------------------------------
        # Evaluate RHS ...
        pushl    -8(%ebp)
        # Now, do the assignment...
        popl    -4(%ebp)

# -----------------------------------------
# Line 18: out.writeint()
# -----------------------------------------
        pushl    $0
        pushl    -8(%ebp)
        call    add
        addl    $8, %esp
        pushl    %eax
        pushl    -8(%ebp)
        call    sub
        addl    $8, %esp
        pushl    %eax
        call    writeint
        addl    $4, %esp

        movl    -4(%ebp), %eax
        movl    %ebp, %esp
        popl    %ebp
        ret    



# -----------------------------------------
# Line 21: start()
# -----------------------------------------
.global	main
main:
        pushl    %ebp
        movl    %esp, %ebp

# -----------------------------------------
# Line 23: num := in.readint()
# -----------------------------------------
        # Evaluate RHS ...
        call    readint
        pushl    %eax
        # Now, do the assignment...
        popl    num

# -----------------------------------------
# Line 24: if num>0 then
# -----------------------------------------
        pushl    num
        pushl    $0
        call    gtr
        addl    $8, %esp
        pushl    %eax
        popl    %eax
        cmpl    $0, %eax
        jne    _doif00
        jmp    _endif00
_doif00:

# -----------------------------------------
# Line 25: num := Fact(num)
# -----------------------------------------
        # Evaluate RHS ...
        pushl    num
        call    Fact
        addl    $4, %esp
        pushl    %eax
        # Now, do the assignment...
        popl    num

# -----------------------------------------
# Line 26: out.writeint()
# -----------------------------------------
        pushl    num
        call    writeint
        addl    $4, %esp
        jmp    _endif00
_endif00:

        movl    %ebp, %esp
        popl    %ebp
        ret    

# -----------------------------------------
# Line 28: end start
# -----------------------------------------
        pushl     $0
        call    exit
