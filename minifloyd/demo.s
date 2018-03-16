# -----------------------------------------
        # Jwhat331 ...
        # CpS450 - Phase4 ...
        # demo.floyd ...
# -----------------------------------------


	.file	"demo.floyd"
	.stabs	"demo.floyd",100,0,0,.Ltext0
	.text	
.Ltext0:
	.stabs	"int:t(0,1)=r(0,1);-2147483648;2147483647;",128,0,0,0
.data
# Line 2: x: int
	.comm	x,4,4
	.stabs	"x:G(0,1)",32,0,0,0

# Line 3: y: int
	.comm	y,4,4
	.stabs	"y:G(0,1)",32,0,0,0

.text
# -----------------------------------------
# Line 5: start()
# -----------------------------------------
.global	main
	.stabs	"main:F",36,0,0,main
main:

# -----------------------------------------
# Line 7: x := 5
# -----------------------------------------
	.stabn	68,0,7,.line7-main
.line7:
        # Evaluate RHS ...
        pushl    $5
        # Now, do the assignment...
        popl    x

# -----------------------------------------
# Line 8: y := 3+x
# -----------------------------------------
	.stabn	68,0,8,.line8-main
.line8:
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
	.stabn	68,0,9,.line9-main
.line9:
        pushl    x
        call    writeint
        addl    $4, %esp

# -----------------------------------------
# Line 10: end start
# -----------------------------------------
	.stabn	68,0,10,.line10-main
.line10:
        pushl     $0
        call    exit
