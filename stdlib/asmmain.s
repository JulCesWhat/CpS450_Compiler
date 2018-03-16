# --------------------------
# Line 7: inside class testnum
# --------------------------
   
.data  
.comm _x,4,4  
# --------------------------
# Line 10: inside method start
# --------------------------
   
.text
.global main
main:  
# --------------------------
# Line 12: x:=in.readint()
# --------------------------
   
# Evaluating RHS    
# --------------------------
# Line 12: in.readint()
# --------------------------
   
 call  readint
 addl  $4, %esp
 pushl  %eax 
# Do Assignment
 popl _x
 pushl _x
 pushl $0 
# relational '>'
 popl %ebx
 popl %eax
 cmpl %ebx, %eax
 setgb %al
 movzbl %al, %eax
 pushl %eax 
# Ifing
 popl %eax
 cmp $1, %eax
 je _iftrue1
 jmp _iffalse1
_iftrue1:  
# --------------------------
# Line 14: out.writeint(1)
# --------------------------
   
 pushl $1
 call  writeint
 addl  $4, %esp
 jmp _endif1
_iffalse1:  
 pushl _x
 pushl $0 
# relational '='
 popl %ebx
 popl %eax
 cmpl %eax, %ebx
 seteb %al
 movzbl %al, %eax
 pushl %eax 
# Ifing
 popl %eax
 cmp $1, %eax
 je _iftrue2
 jmp _iffalse2
_iftrue2:  
# --------------------------
# Line 17: out.writeint(0)
# --------------------------
   
 pushl $0
 call  writeint
 addl  $4, %esp
 jmp _endif2
_iffalse2:  
# --------------------------
# Line 19: out.writeint(-1)
# --------------------------
   
 pushl $-1
 call  writeint
 addl  $4, %esp
 jmp _endif2
_endif2:  
# --------------------------
# Line 21: out.writeint(9)
# --------------------------
   
 pushl $9
 call  writeint
 addl  $4, %esp
 jmp _endif1
_endif1:  
# --------------------------
# Line 23: out.writeint(9)
# --------------------------
   
 pushl $9
 call  writeint
 addl  $4, %esp